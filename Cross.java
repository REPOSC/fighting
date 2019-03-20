package Match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Cross {	
	
	//路口id，道路数量
	int id, roadNum;
	
	//初始反映相互之间关系的路口
	int[] crossRoadsId;
	
	//初始反映相互之间关系的路口
	ArrayList<Road> originCrossRoads;
	
	//符合调度顺序的路口
	ArrayList<Road> sortedCrossRoads;

	//初始化路口和其连接的道路
	public Cross(int id, int roadNum, int[] roadId, ArrayList<Road> init) {
		this.id = id;
		this.roadNum = roadNum;
		this.crossRoadsId = roadId;
		this.originCrossRoads = init;
		this.sortedCrossRoads = init;
		//把符合调度顺序的路口赋值
		Collections.sort(sortedCrossRoads,new Comparator<Road>() {
			public int compare(Road o1, Road o2) {
				if(o1.id>o2.id) {
					return 1;
				}
				else {
					return -1;
				}
			};
		});
	}
		
	//通过两个道路号码判断车辆过路口向哪里转,其中01234分别代表到达，直行，左转，右转
	public char nextDirection(int now,int next) {
		if(next == -2)
			return 'a';
		int nowIndex=0,nextIndex = 0;
		for(int i =0;i<4;i++) {
			if(now == crossRoadsId[i]) {
				nowIndex = i;
			}
			else if(next == crossRoadsId[i]) {
				nextIndex = i;
			}
		}
		int judge = getPositive(nextIndex-nowIndex);
		switch(judge) {
		case 1:
			return 'l';
		case 2:
			return 'd';
		case 3:
			return 'r';
		default:
			return 'e';
		}
	}

	//获取需要直行才能进入的 "从某条路经过某种方法后进入的道路" 的道路，其中某方法是左转时way是1，右转时way是3
	public Road getNextRoadDirect(Road originRoad, int way) {
		for(int i =0;i<4;i++) {
			if(this.originCrossRoads.get(i) == originRoad) {
				return this.originCrossRoads.get(getPositive(i+way-5));
			}
		}
		return null;
	}
	
	//获取需要左转才能进入的 "从某条路经过某种方法后进入的道路" 的道路，其中某方法是左转时way是1，右转时way是3
	public Road getNextRoadLeft(Road originRoad, int way) {
		for(int i =0;i<4;i++) {
			if(this.originCrossRoads.get(i) == originRoad) {
				return this.originCrossRoads.get(getPositive(i+way-6));
			}
		}
		return null;
	}
	
	public static int getPositive(int a) {
		return a>0?a:a+4;
	}
	
	public boolean equals(Cross c) {
		return this.id == c.id;
	}

}

