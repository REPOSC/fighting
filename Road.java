package com.huawei;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;


//每条道路对象
class Road {
	
	//道路id，道路长度，道路速度限制，道路的条数，道路的出发路口，道路的结束路口， 道路是否为双向，当前道路上的车数
	int id, length, speedLimit, laneNum, begin, end, isDouble,carNum;
	
	//开始的路口
	Cross beginCross = null,endCross = null;
	
	//表明每个车道上的车的情况，从起点到终点的方向上，从1开始编号且不重复
	ArrayList<ArrayDeque<Car>> roadStatus = null;

	//暂存map
	Map map;

	//初始化道路信息
	public Road(int[] init, Map tempMap) {
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
		map = tempMap;
	}
	public Road() {
		this.id = -1;
	}

	private static final int wLen = 1, wCarNum = 1, wSpeed = -1, wLocation = -1;
	//获得当前加权路径长度  nowcross是车即将进入的入口
	public int getVirtualLength(Cross nowCross) {
        if (id == -1){
            throw new NullPointerException();
        }
		int virtuallength = 0, laneCarNum = 0, lastCarSpeed = 0, lastCarPosition = 0;
		int base;
		if(nowCross == this.beginCross) {
			base = 0;
		}else {
			base = this.laneNum;
		}
		for(int i=base;i<base+this.laneNum;i++) {
			laneCarNum = this.roadStatus.get(i).size();
			if(laneCarNum != length) {
                if (this.roadStatus.get(i).size() == 0){
                    lastCarPosition = 0;
                    lastCarSpeed = this.speedLimit;
                }
                else {
                    lastCarPosition = this.roadStatus.get(i).getLast().status.location;
                    lastCarSpeed = this.roadStatus.get(i).getLast().status.nowSpeed;
                }				
				break;
			}
		}
		virtuallength = wCarNum*laneCarNum + wLen*this.length + wSpeed*lastCarSpeed + wLocation*lastCarPosition;
		if (virtuallength <= 0){
			return 0;
		}
		return virtuallength;
	}
	
	//将cross对象传进来
	public void getCross(Cross begin,Cross end) {
		this.beginCross = begin;
		this.endCross = end;
	}
	
	//取得每条路准备经过传入路口的下一辆车
	public Car getNextCar(Cross nowCross) {
		int base;
		if(nowCross == this.beginCross) {
			base = this.laneNum;
		}
		else {
			base = 0;
		}
		for(int i=base;i<base+this.laneNum;i++) {
			for(Car tempCar:this.roadStatus.get(i)) {
				if(tempCar.status.isTermination==false) {
					map.algorithm.OperateTheCar(tempCar);
					return tempCar;
				}
				else {
					break;
				}
			}
		}
		return null;
	}

	//Just have a look
	public Car seeNextCar(Cross nowCross) {
		int base;
		if(nowCross == this.beginCross) {
			base = this.laneNum;
		}
		else {
			base = 0;
		}
		for(int i=base;i<base+this.laneNum;i++) {
			for(Car tempCar:this.roadStatus.get(i)) {
				if(tempCar.status.isTermination==false) {
					return tempCar;
				}
				else {
					break;
				}
			}
		}
		return null;
	}

	
	public boolean equals(Road r) {
		return this.id == r.id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(id)+" "+ length+" "+ speedLimit+" "+  laneNum+" "+  begin+" "+  end+" "+  isDouble + " "+ carNum;
	}
}
