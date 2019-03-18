package Match;

import java.util.ArrayList;

//每个路口对象
class Cross {	
	
	//路口id，（从上面开始顺时针数）第一条路id，第二条路id，第三条路id，第四条路id ，可行道路数
	int id, road1, road2, road3, road4, roadCount=4;
	
	//符合调度顺序的路口
	ArrayList<Road> crossRoads;

	//初始化路口和其连接的道路
	public Cross(int[] init) {
		this.id = init[0];
		this.road1 = init[1];
		this.road2 = init[2];
		this.road3 = init[3];
		this.road4 = init[4];
		for(int i=1;i<5;i++) {
			if(init[i] ==-1) {
				roadCount--;
			}
		}
	}
		
	//通过两个道路号码判断车辆过路口向哪里转
	public char nextDirection(int now,int next) {
		if (now == this.road1) {
			if(next == this.road2) return 'l';
			else if(next == this.road3) return 'd';
			else if(next == this.road4) return 'r';
			else return 'a';
		}
		else if(now == this.road2) {
			if(next == this.road3) return 'l';
			else if(next == this.road4) return 'd';
			else if(next == this.road1) return 'r';
			else return 'a';
		}
		else if(now == this.road3) {
			if(next == this.road4) return 'l';
			else if(next == this.road1) return 'd';
			else if(next == this.road2) return 'r';
			else return 'a';
		}
		else if(now == this.road4) {
			if(next == this.road1) return 'l';
			else if(next == this.road2) return 'd';
			else if(next == this.road3) return 'r';
			else return 'a';
		}
		else return 'e';
	}
	public char nextDirection(int now) {
		return this.nextDirection(now, -2);
	}
	
}

