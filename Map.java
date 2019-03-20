package Match;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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
		for(int i:this.roads.keySet()) {
			Road tempRoad = this.roads.get(i);
			tempRoad.getCross(this.crosses.get(tempRoad.begin), this.crosses.get(tempRoad.end));
		}
		this.initMethod(ansfile);
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
			ans[i] = Integer.parseInt(b[i].strip());
		}
		return ans;
	}	

	//运行
	public int run() {
		this.moveCarsInRoom();
		while(this.arrived.size()<this.carTotal) {
			this.markCars();
			this.moveCarsOnRoad();
			this.moveCarsInRoom();
		}
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
		for(int i:this.roads.keySet()) {
			Road nowRoad = this.roads.get(i);
			if(nowRoad.carNum == 0) {
				return;
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
		for(int crossIndex:this.crosses.keySet()) {
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
	public void moveCarsInRoom() {
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
		this.time ++;
	}
}
