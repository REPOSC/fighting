package com.huawei;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;


//每个车的对象
class Car {
	
	//车辆id，车辆出发地，车辆目的地，最高车速，出发时间 ，出发和到达路口
	int id, depart, arrive, speedLimit, time;
	Cross departCross,arriveCross;
	
	//读入的ans.txt
	int startMoveTime;
	ArrayList<Road> ansRoads;
	
	int arrivedTime;
	//车辆当前的状态
	CarStatus status;
	
	//初始化车辆信息
	public Car(int[] init,Cross c1, Cross c2) {
		this.id = init[0];
		this.depart = init[1];
		this.arrive = init[2];
		this.speedLimit = init[3];
		this.time = init[4];
		this.departCross = c1;
		this.arriveCross = c2;
		this.status= new CarStatus(this.departCross);
		this.ansRoads=new ArrayList<Road>();
	}
	
	
	//OK
	//标记在路上每条车道的第一辆车,return false是因为有些车到达车库，是不准确的
	//通过路口判断是否到达终点
	public boolean markFirstCar() {
		int SV1 = this.status.nowSpeed;
		int nowRoadLength = this.ansRoads.get(this.status.nowRoadIndex).length;
		int S1 = nowRoadLength - this.status.location;
		this.status.S1 = S1;
		if(S1>=SV1) {
			this.status.location +=SV1;
			this.status.isTermination = true;
			Map.waitCarNum--;
		}
		else {
			Road nowRoad = this.ansRoads.get(this.status.nowRoadIndex);
			Cross nextCross;
			if(this.status.laneNum<nowRoad.laneNum) {
				nextCross = nowRoad.endCross;
			}
			else {
				nextCross = nowRoad.beginCross;
			}
			if(nextCross == this.arriveCross) {
				this.ansRoads.get(this.status.nowRoadIndex).roadStatus.get(this.status.laneNum).pop();
				Map.waitCarNum--;
				this.status.location = 0;
				return false;
			}
			this.status.isTermination = false;	
		}
		return true;
	}
	
		
	//OK
	//是准确的判断，如果变成终止则为false
	//nextSpeed在刷新路线的时候有没有更新，actcross在刷新路线的时候有没有更新
	//每辆车的S1在mark的时候更新，S2在remark的时候更新，而且只有能够过路的车有S2
	public boolean reMarkWaitingFirstCar() {
		int SV2 = this.status.nextSpeed;
		int nowRoadLength = this.ansRoads.get(this.status.nowRoadIndex).length;
		int S2 = SV2-this.status.S1;
		if(S2>0) {
			this.status.isTermination = false;
			this.status.S2 = S2;
			return true;
		}
		else{
			this.status.isTermination = true;
			this.status.location = nowRoadLength;
			Map.waitCarNum--;
			return false;
		}
	}
	
	
	//OK
	//标记在路上的每条车道的第二辆以及后面的车
	public void markFollowCar(Car frontCar) {
		int distance = frontCar.status.location-this.status.location-1;
		int SV1 = this.status.nowSpeed;
		if (distance < SV1) {
			if(frontCar.status.isTermination) {
				this.status.location = frontCar.status.location-1;
				this.status.isTermination = true;
				Map.waitCarNum--;
			}
			else {
				this.status.isTermination = false;
			}
		}
		else {
			this.status.isTermination = true;
			this.status.location += distance;
			Map.waitCarNum--;
		}		
	}

    
    //OK
    //当车道上第一辆车为terminal时，更新当前车道上所有车辆为terminal
    public static void reMarkTerminal(ArrayDeque<Car> tempLane) {
        Iterator<Car> iter = tempLane.iterator();
        Car frontCar = null, tempCar;
        boolean count = true;
        while (iter.hasNext()) {
            if (count) {
                frontCar = iter.next();
                count = false;
            } 
            else {
                tempCar = iter.next();
                tempCar.markFollowCar(frontCar);
                frontCar = tempCar;
            }
        }
    }
	
    
    
    
	//如果没有pass，那么说明前方路上满了，那么就将当前的车辆移动到车道开头
	//返回1：成功进入下一个路口，0：处于等待状态，-1：下一个路上已满，并且进行调度到路口处
    
	//重大问题，没有考虑生成回路死锁的状况
	//车辆过路口，返回是否成功进入路口，前提是已经判断可以行动，即没有其他路口车辆限制
	//传入路口参数是不合理的
	public int passCross(Cross crossToPass) {
		Road nextRoad = this.ansRoads.get(this.status.nextRoadIndex);
		int base;
		if(nextRoad.beginCross.equals(crossToPass)) {
			base = 0;
		}
		else {
			base = nextRoad.laneNum;
		}
		for(int i =base;i<base+nextRoad.laneNum;i++) {
			ArrayDeque<Car> tempLane = nextRoad.roadStatus.get(i);
			if(!tempLane.isEmpty()) {
				int rest = tempLane.getLast().status.location-1;
				//如果放不进去
				if(rest==0) {
					continue;
				}
				//如果能放进去
				else {
					if(rest>=this.status.S2) {
						this.status.location = this.status.S2;
					}
					else {
						if (tempLane.getLast().status.isTermination) {
							this.status.location = rest;
						}
						else {
							return 0;
						}
					}
				}
			}
			else {
				this.status.location = this.status.S2;
			}
			//更新驶入新的车道后车的状态已经驶出和驶入的两个车道的信息。
			if(this.ansRoads.get(this.status.nowRoadIndex).roadStatus.get(this.status.laneNum).size()==0){
				throw new NumberFormatException();
			}
			this.ansRoads.get(this.status.nowRoadIndex).roadStatus.get(this.status.laneNum).pop();
			this.ansRoads.get(this.status.nowRoadIndex).carNum--;
			this.status.nowRoadIndex++;
			this.status.nextRoadIndex++;
			this.status.isTermination = true;
			Map.waitCarNum--;
			this.status.hasPlan = false;
			this.status.laneNum = i;
			this.status.nowSpeed = this.status.nextSpeed;
//			if(this.status.nextRoadIndex>=this.ansRoads.size()) {
//				this.status.nextRoadIndex = -2;
//				this.status.actCross = 'a';
//			}
//			else {
//				this.status.nextSpeed = Math.min(this.ansRoads.get(this.status.nextRoadIndex).speedLimit, this.speedLimit);//低效
//				if(base==0) {
//					this.status.actCross = nextRoad.endCross.nextDirection(this.ansRoads.get(this.status.nowRoadIndex).id, this.ansRoads.get(this.status.nextRoadIndex).id);
//				}
//				else {
//					this.status.actCross = nextRoad.beginCross.nextDirection(this.ansRoads.get(this.status.nowRoadIndex).id, this.ansRoads.get(this.status.nextRoadIndex).id);	
//				}
//			}
			tempLane.add(this);
			this.ansRoads.get(this.status.nowRoadIndex).carNum++;
			return 1;
		}
		this.status.isTermination = true;
		Map.waitCarNum --;
		this.status.location=this.ansRoads.get(this.status.nowRoadIndex).length;
		return -1;
	}
		
	
	//移动在车库的车辆
	public boolean moveCarInRoom() {
		Road nextRoad = this.ansRoads.get(this.status.nextRoadIndex);
		this.status.nowSpeed = Math.min(this.speedLimit, nextRoad.speedLimit);
		this.status.S2 = this.status.nowSpeed;
		int base;
		if(nextRoad.beginCross.equals(this.departCross)) {
			base = 0;
		}
		else {
			base = nextRoad.laneNum;
		}
		for(int i =base;i<base+nextRoad.laneNum;i++) {
			ArrayDeque<Car> tempLane = nextRoad.roadStatus.get(i);
			if(!tempLane.isEmpty()) {
				int rest = tempLane.getLast().status.location-1;
				//如果放不进去
				if(rest==0) {
					continue;
				}
				//如果能放进去
				else {
					this.status.location = rest<this.status.S2?rest:this.status.S2;
				}
			}
			else {
				this.status.location = this.status.S2;
			}
			//更新驶入新的车道后车的状态已经驶出和驶入的两个车道的信息。
			this.status.isTermination = true;
			this.status.laneNum = i;
			this.status.nextRoadIndex++;
			this.status.hasPlan = false;
			tempLane.add(this);
			this.ansRoads.get(this.status.nowRoadIndex).carNum++;
			return true;
		}
		return false;	
	}
	
	public boolean equals(Car c) {
		return this.id == c.id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(this.status.nextRoadIndex<0) {
			return "id:,"+this.id+",isterminal:,"+this.status.isTermination+",nowRoad:,"+this.ansRoads.get(this.status.nowRoadIndex).id+
					",Lane:,"+this.status.laneNum+",Location:,"+this.status.location+",nextRoad:,"+"arrived";

		}
		return "id:,"+this.id+",isterminal:,"+this.status.isTermination+",nowRoad:,"+this.ansRoads.get(this.status.nowRoadIndex).id+
				",Lane:,"+this.status.laneNum+",Location:,"+this.status.location;//+",nextRoad:,"+this.ansRoads.get(this.status.nextRoadIndex).id;
	}
}


//描述当前车辆的状态
class CarStatus{
		
	//车辆当前的状态，终止true和等待false
	boolean isTermination;
	
	//判断是否规划过路线
	boolean hasPlan;
	
	//车辆当前的路在ansRoad的地址和下一个路在ansRoad的地址，大小相差1
	int nowRoadIndex,nextRoadIndex;
	
	//当前的路的第几车道,当前车道的几个单位处
	int laneNum,location;

	//车辆当前的速度
	int nowSpeed,nextSpeed;
	
	//车辆通过下次路口时怎么前进，l为左转，r为右转，d为直行，a为到达，f为出错
	char actCross;
	
	//当前车辆将要通过路口时下条路上走得距离
	int S1,S2;
	
	//初始化车辆状态为-1
	public CarStatus(Cross tempCross) {
		this.hasPlan=false;
		this.isTermination = true;
		this.nowRoadIndex =0;
		this.nextRoadIndex = 0;
		this.location = 0;
	}

}