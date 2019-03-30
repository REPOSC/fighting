package com.huawei;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;


//每条道路对象
class Road {

	public static final int BIG_VALUE = 100007;
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
	//private int startblocktime = 0;
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
		int i;
		for(i=base;i<base+this.laneNum;i++) {
			laneCarNum = this.roadStatus.get(i).size();
			if(laneCarNum!=0 && this.roadStatus.get(i).getLast().status.location != 1) {
                if (this.roadStatus.get(i).size() == 0){
                    lastCarPosition = length;
                    lastCarSpeed = this.speedLimit;
                }
                else {
                    lastCarPosition = this.roadStatus.get(i).getLast().status.location;
                    lastCarSpeed = this.roadStatus.get(i).getLast().status.nowSpeed;
                }				
				break;
			}
		}
		if (i == base + laneNum){
			virtuallength = BIG_VALUE;
		}
		else {
			virtuallength = wCarNum * laneCarNum + wLen * this.length + wSpeed * lastCarSpeed + wLocation * lastCarPosition;
		}
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
	
	
	//OK
	//取得第一优先级的车辆，如果没有规划路线则进行规划
	//规划后remark，如果仍然是等待车辆，则返回该车辆，如果是终止状态，那么remarktermianl当前车道，并且继续寻找第一优先级的车
	//取得每条路准备经过传入路口的下一辆车
	public Car getNextCar(Cross nowCross) {
		int base;
		if(nowCross == this.beginCross) {
			base = this.laneNum;
		}
		else {
			base = 0;
		}
		int[] finishedLane = new int[this.laneNum];
		int count = 0;
		for(int loc = this.length;loc>0;loc--) {
			if(count == this.laneNum) {
				break;
			}
			for(int i=base;i<base+this.laneNum;i++) {
				if(finishedLane[i-base]==1) {
					continue;
				}
				for(Car tempCar:this.roadStatus.get(i)) {
					if(tempCar.status.location == loc) {
						if(tempCar.status.isTermination==false) {
							if(!tempCar.status.hasPlan) {
								map.algorithm.OperateTheCar(tempCar);
								tempCar.status.hasPlan = true;
								
								//System.out.println(tempCar.id+" "+tempCar.speedLimit+" "+tempCar.ansRoads.size()+" "+tempCar.ansRoads+" "+tempCar.status.nextRoadIndex+" "+tempCar.ansRoads.get(tempCar.status.nextRoadIndex));
								tempCar.status.nextSpeed = Math.min(tempCar.speedLimit,tempCar.ansRoads.get(tempCar.status.nextRoadIndex).speedLimit);
								tempCar.status.actCross = nowCross.nextDirection(tempCar.ansRoads.get(tempCar.status.nowRoadIndex).id, tempCar.ansRoads.get(tempCar.status.nextRoadIndex).id);
							}
							if(tempCar.reMarkWaitingFirstCar()) {
								return tempCar;
							}
							else {
								Car.reMarkTerminal(this.roadStatus.get(i));
								return null;
							}
							
						}
						else {
							finishedLane[i-base] = 1;
							count++;
							break;
						}
					}
					else if(tempCar.status.location>loc) {
						continue;
					}
					else {
						break;
					}	
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
