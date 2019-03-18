package Match;

import java.util.ArrayList;

//每个车的对象
class Car {
	
	//车辆id，车辆出发地，车辆目的地，最高车速，出发时间
	int id, depart, arrive, speedLimit, time;
	
	//读入的ans.txt
	int startMoveTime;
	ArrayList<Road> ansRoads;
	
	//需要更新的量
	//车辆当前的状态
	CarStatus status;
	//车辆当前的状态，终止和非终止
	boolean isTermination;
	//车辆当前的速度
	int tempSpeed;
	
	//初始化车辆信息
	public Car(int[] init) {
		this.id = init[0];
		this.depart = init[1];
		this.arrive = init[2];
		this.speedLimit = init[3];
		this.time = init[4];
		this.status= new CarStatus();
	}
	
//	//判断是否能够过路口
//	public boolean canPassCross() {
//		
//	}
	public void move() {
		if(this.status.notMoved)
			this.status.notMoved = false;
		
	}
}


//描述当前车辆的状态
class CarStatus{
	
	//当前车辆是否出发和到达
	boolean notMoved,arrived;
	
	//当前车辆所在的路以及进入当前路段的路口
	Cross startCross;
	Road tempRoad;
	
	//当前的路的第几车道，当前车道的第几个单位处
	int laneNum,location;

	//车辆通过下次路口时怎么前进，l为左转，r为右转，d为直行，a为到达，f为出错
	char nextRoad;
	
	//初始化车辆状态为-1
	public CarStatus() {
		this.notMoved = true;
		this.arrived = false;
	}

}