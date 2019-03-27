package Match;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class Map{
	//路口集合，道路集合，车辆集合，未出发的车辆集合和已经到达目的地的车辆集合，时间片，各种信息的总数。
	HashMap<Integer,Cross> crosses;
	HashMap<Integer,Road> roads;
	HashMap<Integer,Car> cars;
	ArrayList<Car> notMoved, arrived;
	int time;
	int carTotal;
	
	//读入文件，初始化车辆、路口以及道路信息。读入答案。
	public Map(String crossfile,String roadfile,String carfile, String ansfile) throws IOException{
		this.notMoved = new ArrayList<Car>();
		this.arrived = new ArrayList<Car>();
		this.initRoad(roadfile);
		this.initCross(crossfile);
		this.initCar(carfile);
		this.time = 0;	
		this.carTotal = this.cars.size();
		for(int i:sortIndex(this.roads.keySet())) {
			Road tempRoad = this.roads.get(i);
			tempRoad.getCross(this.crosses.get(tempRoad.begin), this.crosses.get(tempRoad.end));
		}
                Algorithm A = new Algorithm();
//                System.out.println(A);
                A.Apply();
//                for (Integer key:cars.keySet()){
//                    Car car = cars.get(key);
//                    String result = Integer.toString(car.id);
//                    result += ":";
//                    for (int i = 0; i < car.ansRoads.size(); ++i){
//                        result += Integer.toString(car.ansRoads.get(i).id) + " ";
//                    }
//                    result += "\n";
//                    System.out.println(result);
//                }
                //this.initMethod(ansfile);
	}
	public void initCross(String file) throws IOException {
		this.crosses = new HashMap<Integer, Cross>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempLine = reader.readLine();
			Cross temp;
			while(tempLine != null) {
				if(tempLine.charAt(0) == '#') {
					tempLine = reader.readLine();
					continue;
				}
				int [] tempLineArray = deal(tempLine);
				int [] tempId = new int[4];
				ArrayList<Road> tempRoads = new ArrayList<Road>();
				int roadNum=0;
				for(int i=1;i<tempLineArray.length;i++) {
					tempId[i-1] = tempLineArray[i];
					if(tempLineArray[i] == -1) {
						tempRoads.add(new Road());
					}
					else {
						roadNum++;
						tempRoads.add(this.roads.get(tempLineArray[i]));
					}
				}
				temp = new Cross(tempLineArray[0],roadNum,tempId,tempRoads);
				this.crosses.put(temp.id, temp);
				tempLine = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initRoad(String file) throws IOException {
		this.roads = new HashMap<Integer, Road>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempLine = reader.readLine();
			Road temp;
			while(tempLine != null) {
				if(tempLine.charAt(0) == '#') {
					tempLine = reader.readLine();
					continue;
				}
				int [] tempLineArray = deal(tempLine);
				temp = new Road(tempLineArray);
				this.roads.put(temp.id,temp);
				tempLine = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initCar(String file) throws IOException {
		this.cars = new HashMap<Integer, Car>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempLine = reader.readLine();
			Car temp;
			while(tempLine != null) {
				if(tempLine.charAt(0) == '#') {
					tempLine = reader.readLine();
					continue;
				}
				int [] tempLineInArray = deal(tempLine);
				temp = new Car(tempLineInArray,this.crosses.get(tempLineInArray[1]),this.crosses.get(tempLineInArray[2]));
				this.cars.put(temp.id,temp);
				this.notMoved.add(temp);
				tempLine = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
        
	public void initMethod(String file) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempLine = reader.readLine();
			int[] tempInfo;
			Car tempCar;
			while(tempLine != null && tempLine!="") {
				if(tempLine.charAt(0) == '#') {
					tempLine = reader.readLine();
					continue;
				}
				tempInfo = deal(tempLine);
				tempCar = this.cars.get(tempInfo[0]);
				tempCar.startMoveTime = tempInfo[1];
				for(int i =2;i<tempInfo.length;i++) {
					tempCar.ansRoads.add(this.roads.get(tempInfo[i]));
				}
				tempLine = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int[] deal(String raw) {
		String a = raw.substring(1,raw.length()-1);
		String[] b = a.split(",");
		int[] ans = new int[b.length];
		for(int i = 0;i < b.length;i++) {
			ans[i] = Integer.parseInt(b[i].trim());
		}
		return ans;
	}	

	//运行
	public int run() {
		this.moveCarsInRoom();
		this.time++;
		boolean stillRoom = true;
		while(this.arrived.size()<this.carTotal) {
//			System.out.println(this.time + " "+this.arrived.size());
			this.markCars();
//			for(int tempCar:sortIndex(this.cars.keySet())) {
//				System.out.println(tempCar+" "+cars.get(tempCar));
//			}
//			System.out.println();
			this.moveCarsOnRoad();
//			for(int tempCar:sortIndex(this.cars.keySet())) {
//				System.out.println(tempCar+" "+cars.get(tempCar));
//			}
//			System.out.println();
			if(stillRoom) {
				stillRoom=this.moveCarsInRoom();
			}
			this.moveCarsInRoom();
//			for(int tempCar:sortIndex(this.cars.keySet())) {
//				System.out.println(tempCar+" "+cars.get(tempCar));
//			}
			this.time++;
		}
		System.out.println("调度结束时间为： "+time);
		int ans=0;
		for(Car oneCar:this.arrived) {
			ans+=(oneCar.arrivedTime-oneCar.startMoveTime);
		}
		return ans;
	}
	
	//对某一条车道进行标记
	public void markSingleLane(ArrayDeque<Car> tempLane) {
		if(tempLane.size() == 0) {
			return;
		}
		Iterator<Car> iter = tempLane.iterator();
		Car frontCar = null,tempCar;
		boolean count = true;
		while(iter.hasNext()) {
			if (count) {
				frontCar = iter.next();
				if(frontCar.markFirstCar(true)){
					count =false;
				}
				else {
					this.arrived.add(frontCar);
					frontCar.arrivedTime = this.time+1;
				}
			}
			else {
				tempCar = iter.next();
				tempCar.markFollowCar(frontCar);
				frontCar = tempCar;
			}
		}
	}
	
	//如果前面的路满了，那么更新当前道路上所有车辆为terminal
	public void markTerminal(Road nowRoad) {
		for(ArrayDeque<Car> tempLane:nowRoad.roadStatus) {
			if(tempLane.size() == 0) {
				continue;
			}
			Iterator<Car> iter = tempLane.iterator();
			Car frontCar = null,tempCar;
			boolean count = true;
			while(iter.hasNext()) {
				if (count) {
					frontCar = iter.next();
					if(frontCar.markFirstCar(false)){
						count =false;
					}
					else {
						this.arrived.add(frontCar);
						frontCar.arrivedTime = this.time+1;
					}
				}
				else {
					tempCar = iter.next();
					tempCar.markFollowCar(frontCar);
					frontCar = tempCar;
				}
			}
		}
	}
	
	//首先处理每条道路，对车辆进行标记
	public void markCars() {
		for(int i:sortIndex(this.roads.keySet())) {
			Road nowRoad = this.roads.get(i);
			if(nowRoad.carNum == 0) {
				continue;
			}
			for(ArrayDeque<Car> tempLane:nowRoad.roadStatus) {
				this.markSingleLane(tempLane);
			}
		}
	}
	
	//标记后处理每一个路口
	public void moveCarsOnRoad() {
		Car tempCar;
		Road tempRoad;
		int roadIndex;
		int tempLaneNum;
		boolean judge;
		for(int crossIndex:sortIndex(this.crosses.keySet())) {
			Cross nowCross = this.crosses.get(crossIndex);
			judge = true;
			boolean [] roadFinished = {false,false,false,false};
			boolean canLeft,canRight;
			while(judge) {
				judge = false;
				for(roadIndex = 4-nowCross.roadNum;roadIndex<4;roadIndex++) {
					if(roadFinished[roadIndex]) {
						continue;
					}
					tempRoad = nowCross.sortedCrossRoads.get(roadIndex);
					tempCar = tempRoad.getNextCar(nowCross);
					canLeft = false;
					canRight = false;
					while(tempCar != null) {
						tempLaneNum = tempCar.status.laneNum;
						judge = true;
						if(tempCar.status.actCross=='d') {
							if(tempCar.passCross(nowCross)) {
								markSingleLane(tempRoad.roadStatus.get(tempLaneNum));
								tempCar = tempRoad.getNextCar(nowCross);
							}
							else {
								markTerminal(tempRoad);
								roadFinished[roadIndex] = true;
								break;
							}
						}
						else if(tempCar.status.actCross=='l') {
							if(!canLeft) {
								Car otherCar = nowCross.getNextRoadDirect(tempRoad, 1).getNextCar(nowCross);
								if(otherCar!=null && otherCar.status.actCross=='d') {
									break;
								}
							}
							canLeft = true;
							if(tempCar.passCross(nowCross)) {
								markSingleLane(tempRoad.roadStatus.get(tempLaneNum));
								tempCar = tempRoad.getNextCar(nowCross);
							}
							else {
								markTerminal(tempRoad);
								roadFinished[roadIndex] = true;
								break;
							}
						}
						else if(tempCar.status.actCross=='r') {
							if(!canRight) {
								Car otherCar = nowCross.getNextRoadDirect(tempRoad, 3).getNextCar(nowCross);
								if(otherCar!=null && otherCar.status.actCross=='d') {
									break;
								}
								otherCar = nowCross.getNextRoadLeft(tempRoad, 3).getNextCar(nowCross);
								if(otherCar!=null && otherCar.status.actCross=='l') {
									break;
								}
							}
							canRight = true;
							if(tempCar.passCross(nowCross)) {
								markSingleLane(tempRoad.roadStatus.get(tempLaneNum));
								tempCar = tempRoad.getNextCar(nowCross);
							}
							else {
								markTerminal(tempRoad);
								roadFinished[roadIndex] = true;
								break;
							}
						}
						
					}
				}
				
			}
		}
	}
	
	//处理未出发的车辆
	public boolean moveCarsInRoom() {
		//假设提供的车辆id为顺序
		int length = this.notMoved.size();
		for(int i=0;i<length;i++) {
			Car tempCar = this.notMoved.get(i);
			if(this.time == tempCar.startMoveTime) {
				if(tempCar.moveCarInRoom()) {
					this.notMoved.remove(tempCar);
					length--;
					i--;
				}
			}
		}
		if(this.notMoved.size()>0) {
			return true;
		}
		return false;
	}
	
	public static ArrayList<Integer> sortIndex(Set<Integer> a){
		ArrayList<Integer> ans=new ArrayList<Integer>();
		for(int i:a) {
			ans.add(i);
		}
		Collections.sort(ans);
		return ans;
	}
        
        
        class Algorithm{
            private HashMap<Cross, HashMap<Cross, Integer> > CrossGraph;
            private HashMap<PairCross, PairMinDistance > CrossMinDistances;
            private HashMap<PairCross, Road> CrossesToRoad;
            private void DIJKeverything(){
                CrossMinDistances = new HashMap<>();
                for (Cross cross1: CrossGraph.keySet()){
                    for (Cross cross2: CrossGraph.keySet()){
                        PairCross pair = new PairCross();
                        pair.begin = cross1;
                        pair.end = cross2;
                        PairMinDistance minWay = new PairMinDistance();
                        minWay.ThroughCrosses.add(pair.begin);                        
                        if (pair.begin.equals(pair.end)){
                            minWay.distance = 0;
                        }
                        else {
                            minWay.ThroughCrosses.add(pair.end);
                            if (CrossGraph.get(cross1).containsKey(cross2)){
                                minWay.distance = CrossGraph.get(cross1).get(cross2);
                            }
                            else {
                                minWay.distance = Integer.MAX_VALUE;
                            }
                        }
                        CrossMinDistances.put(pair, minWay);
                    }
                }
                for (Cross cross1: CrossGraph.keySet()){
                    HashMap<Cross, Integer> mindistances = new HashMap<>();
                    HashMap<Cross, Boolean> visited = new HashMap<>();
                    for (Cross cross2: CrossGraph.keySet()){
                        if (cross1.id == cross2.id){
                            mindistances.put(cross2, 0);
                        }
                        else {
                            mindistances.put(cross2, Integer.MAX_VALUE);
                        }
                        visited.put(cross2, false);
                    }
                    while (true){
                        Cross minCross = null;
                        int minValue = Integer.MAX_VALUE;
                        for (Cross cross2: CrossGraph.keySet()){
                            if (visited.get(cross2) == false && mindistances.get(cross2) < minValue){
                                minValue = mindistances.get(cross2);
                                minCross = cross2;
                            }
                        }
                        if (minValue == Integer.MAX_VALUE){
                            break;
                        }
                        visited.put(minCross, true);
                        for (Cross cross2: CrossGraph.get(minCross).keySet()){
                            if (CrossGraph.get(minCross).get(cross2) != Integer.MAX_VALUE &&
                                    mindistances.get(minCross) != Integer.MAX_VALUE &&
                                    CrossGraph.get(minCross).get(cross2) + mindistances.get(minCross) < mindistances.get(cross2)){
                                mindistances.put(cross2, CrossGraph.get(minCross).get(cross2) + mindistances.get(minCross));
                                PairCross pair = new PairCross();
                                pair.begin = cross1;
                                pair.end = cross2;
                                PairMinDistance minWay = CrossMinDistances.get(pair);
                                PairCross another = new PairCross();
                                another.begin = cross1;
                                another.end = minCross;
                                PairMinDistance anotherWay = CrossMinDistances.get(another);
                                minWay.ThroughCrosses.clear();
                                minWay.ThroughCrosses.addAll(anotherWay.ThroughCrosses);
                                minWay.ThroughCrosses.add(cross2);
                                minWay.distance = mindistances.get(cross2);
                            }
                        }
                    }
                }
//                for (Cross cross1: CrossGraph.keySet()){
//                    for (Cross cross2: CrossGraph.keySet()){
//                        for (Cross cross3: CrossGraph.keySet()){
//                            PairCross pair1 = new PairCross();
//                            pair1.begin = cross2; 
//                            pair1.end = cross3;  
//                            PairCross pair2 = new PairCross();
//                            pair2.begin = cross2;  
//                            pair2.end = cross1; 
//                            PairCross pair3 = new PairCross();
//                            pair3.begin = cross1;   
//                            pair3.end = cross3; 
//                            if (CrossMinDistances.get(pair2).distance < Integer.MAX_VALUE &&
//                                    CrossMinDistances.get(pair3).distance < Integer.MAX_VALUE &&
//                                    CrossMinDistances.get(pair2).distance + CrossMinDistances.get(pair3).distance <
//                                    CrossMinDistances.get(pair1).distance){
//                                PairMinDistance minWay = CrossMinDistances.get(pair1);
//                                minWay.ThroughCrosses.add(1, cross1);
//                                minWay.distance = CrossMinDistances.get(pair2).distance + CrossMinDistances.get(pair3).distance;
//                            }
//                        }
//                    }
//                }
            }
            private void initgraph(){
                //初始化图
                CrossGraph = new HashMap<>();
                for (Integer key: crosses.keySet()){
                    Cross nowCross = crosses.get(key);
                    HashMap<Cross, Integer> nextCrossInfo = new HashMap<>();
                    for (Road nowRoad:nowCross.originCrossRoads){
                        if (nowRoad.endCross != null){
                            nextCrossInfo.put(nowRoad.endCross, nowRoad.length);
                        }
                    }
                    CrossGraph.put(nowCross, nextCrossInfo);
                }
            }
            public Algorithm(){
                bufferroads();
                initgraph();
                DIJKeverything();
            }
            public void Apply(){
                for (Integer key:cars.keySet()){
                    Car nowCar = cars.get(key);
                    nowCar.startMoveTime = nowCar.time;
                    nowCar.ansRoads.clear();
                    PairCross pairCross = new PairCross();
                    pairCross.begin = nowCar.departCross;
                    pairCross.end = nowCar.arriveCross;
                    ArrayList<Cross> floydans = CrossMinDistances.get(pairCross).ThroughCrosses;
                    for (int i = 0; i < floydans.size() - 1; ++i){
                        PairCross pair = new PairCross();
                        pair.begin = floydans.get(i);
                        pair.end = floydans.get(i + 1);
                        nowCar.ansRoads.add(CrossesToRoad.get(pair));
//                        System.out.println("!!!!"+ pair.begin.id + "!!!!" + pair.end.id);//
//                        System.out.println("!!!!"+ CrossesToRoad.get(pair));//
                    }
                }
            }
            @Override
            public String toString(){
                StringBuilder result = new StringBuilder();
                result.append("----------------------------\n");
                for (Cross keybegin:CrossGraph.keySet()){
                    result.append(keybegin.id);
                    result.append(":\n");
                    HashMap<Cross, Integer> pairnexts = CrossGraph.get(keybegin);
                    for (Cross keyend:pairnexts.keySet()){
                        result.append(keyend.id + ": " + pairnexts.get(keyend));
                        result.append("\n");
                    }
                    result.append("\n");
                }
                result.append("----------------------------\n");
                for (Cross cross1: CrossGraph.keySet()){
                    for (Cross cross2: CrossGraph.keySet()){
                        PairCross pair = new PairCross();
                        pair.begin = cross1;
                        pair.end = cross2;
                        PairMinDistance minWay = CrossMinDistances.get(pair);
                        result.append("[");
                        if (minWay.ThroughCrosses.size() > 0){
                            result.append(minWay.ThroughCrosses.get(0).id);
                            for (int i = 1; i < minWay.ThroughCrosses.size(); ++i){
                                result.append(",");
                                result.append(minWay.ThroughCrosses.get(i).id);
                            }
                        }
                        result.append("]:");
                        result.append(minWay.distance);
                        result.append("\n");
                    }
                }
                return result.toString();
            }

            private void bufferroads() {
                CrossesToRoad = new HashMap<>();
                for (Integer key:crosses.keySet()){
                    Cross nowCross = crosses.get(key);                    
                    for (int i = 0; i < nowCross.originCrossRoads.size(); ++i){
                        if (nowCross.originCrossRoads.get(i).endCross != null){
                            PairCross pair = new PairCross();
                            pair.begin = nowCross;
                            pair.end = nowCross.originCrossRoads.get(i).endCross;
                            CrossesToRoad.put(pair, nowCross.originCrossRoads.get(i));
                        }
                    }
                }
            }
        }	
}
