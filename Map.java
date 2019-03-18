package Match;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class Map{
	//路口集合，道路集合，车辆集合，未出发的车辆集合和已经到达目的地的车辆集合，时间片，各种信息的总数。
	HashMap<Integer,Cross> crosses;
	HashMap<Integer,Road> roads;
	HashMap<Integer,Car> cars;
	HashSet<Car> notMoved, arrived;
	int time;
	int carTotal,crossTotal,roadTotal;
	
	//读入文件，初始化车辆、路口以及道路信息。读入答案。
	public Map(String crossfile,String roadfile,String carfile, String ansfile) throws IOException{
		this.initCross(crossfile);
		this.initRoad(roadfile);
		this.initCar(carfile);
		this.time = 0;	
		this.carTotal = cars.size();
		this.crossTotal = crosses.size();
		this.roadTotal = roads.size();
		//把符合调度顺序的路口赋值
		int count;
		for(int i:crosses.keySet()) {
			Cross tempCross = crosses.get(i);
			int[] temp = {tempCross.road1,tempCross.road2,tempCross.road3,tempCross.road4};
			Arrays.sort(temp);
			count = 0;
			while(temp[count] == -1) {
				count++;
			}
			while(count<4) {
				tempCross.crossRoads.add(roads.get(temp[count]));
			}
		}
		this.initMethod(ansfile);
	}
	public void initCross(String file) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempLine = reader.readLine();
			Cross temp;
			while(tempLine != null) {
				if(tempLine.charAt(0) == '#') {
					tempLine = reader.readLine();
					continue;
				}
				temp = new Cross(deal(tempLine));
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
				temp = new Road(tempLineArray,crosses.get(tempLineArray[4]),crosses.get(tempLineArray[5]));
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
				temp = new Car(tempLineInArray,crosses.get(tempLineInArray[1]),crosses.get(tempLineInArray[2]));
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
			while(tempLine != null) {
				if(tempLine.charAt(0) == '#') {
					tempLine = reader.readLine();
					continue;
				}
				tempInfo = deal(tempLine);
				tempCar = cars.get(tempInfo[0]);
				tempCar.startMoveTime = tempInfo[1];
				for(int i =2;i<tempInfo.length;i++) {
					tempCar.ansRoads.add(roads.get(tempInfo[i]));
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
//	//判断是否完成调度
//	public int isFinished() {
//		if (this.Arrived.size()==carTotal) {
//			return 
//		}
//	}
	
	
	//车辆从车库出发
	public void startMove() {
		for(Car tempCar:notMoved) {
			if(tempCar.startMoveTime == this.time) {
				
			}
			
		}
	}
	
	//首先处理每条道路
	public void markCars() {
		for(int i:roads.keySet()) {
			Road tempRoad = roads.get(i);
			if(tempRoad.carNum == 0) {
				continue;
			}
			for(ArrayDeque<Car> tempLane:tempRoad.roadStatus) {
				if(tempLane.size() == 0) {
					continue;
				}
				Iterator<Car> iter = tempLane.iterator();
				for(Car tempCar:tempLane) {
					
				}
			}
		}
	}
	
	
	
}
