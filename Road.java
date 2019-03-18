package Match;

import java.util.ArrayDeque;
import java.util.ArrayList;


//每条道路对象
class Road {
	
	//道路id，道路长度，道路速度限制，道路的条数，道路的出发路口，道路的结束路口， 道路是否为双向，当前道路上的车数
	int id, length, speedLimit, laneNum, begin, end, isDouble,carNum;
	
	//表明每个车道上的车的情况，从起点到终点的方向上，从1开始编号且不重复
	ArrayList<ArrayDeque<Car>> roadStatus;

	//初始化道路信息
	public Road(int[] init) {
		this.id = init[0];
		this.length = init[1];
		this.speedLimit = init[2];
		this.laneNum = init[3];
		this.begin = init[4];
		this.end = init[5];
		this.isDouble = init[6];
		this.carNum = 0;
		roadStatus = new ArrayList<ArrayDeque<Car>>();
		//可能浪费一般内存，可以使用laneNum*(1.5+0.5*(-1)^isDouble)
		for(int i =0;i<laneNum*2;i++) {
			ArrayDeque<Car> tempLane = new ArrayDeque<Car>();
			roadStatus.add(tempLane);
		}
	}
	public Road() {
		this.id = -1;
	}

}