package onetest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Match {

	public static void main(String[] args) {
		
	}

}

class Road {
	//每条道路对象
	//道路id，道路长度，道路速度限制，道路的条数，道路的出发路口，道路的结束路口,  道路是否为双向
	int id, length, speed_limit, road_num, begin, end, is_double;

	Road(int[] init) {
		this.id = init[0];
		this.length = init[1];
		this.speed_limit = init[2];
		this.road_num = init[3];
		this.begin = init[4];
		this.end = init[5];
		this.is_double = init[6];
	}
}

class Cross {
	//每个路口对象
	//路口id，（从上面开始顺时针数）第一条路id，第二条路id，第三条路id，第四条路id
	int id, road1, road2, road3, road4;

	Cross(int[] init) {
		this.id = init[0];
		this.road1 = init[1];
		this.road2 = init[2];
		this.road3 = init[3];
		this.road4 = init[4];
	}
}

class Car {
	//每个车的对象
	//车辆id，车辆出发地，车辆目的地，最高车速，出发时间
	int id, depart, arrive, speed_limit, time;
	
	Car(int[] init) {
		this.id = init[0];
		this.depart = init[1];
		this.arrive = init[2];
		this.speed_limit = init[3];
		this.time = init[4];
	}
}

class CarStatus{
	//描述当前车辆的状态
	//车辆所在的路的id，当前路的出发点（表明方向），当前的路的第几车道，当前车道的第几个单位处
	int road_id,road_start,road_num,location;
}

class Map{
	HashSet<Cross> crosses;
	HashSet<Road> roads;
	HashSet<Car> cars;
	
	Map(String crossfile,String roadfile,String carfile) throws IOException{
		initCross(crossfile);
		initRoad(roadfile);
		initCar(carfile);
	}
	
	public int[] deal(String raw) {
		String a = raw.substring(1,raw.length()-1);
		String[] b = a.split(",");
		int[] ans = new int[b.length];
		for(int i = 0;i < b.length;i++) {
			ans[i] = Integer.parseInt(b[i].strip());
		}
		return ans;
	}
	
	public void initCross(String file) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String templine = reader.readLine();
			Cross temp;
			while(templine != null) {
				if(templine.charAt(0) == '#') {
					templine = reader.readLine();
					continue;
				}
				temp = new Cross(deal(templine));
				this.crosses.add(temp);
				templine = reader.readLine();
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
			String templine = reader.readLine();
			Road temp;
			while(templine != null) {
				if(templine.charAt(0) == '#') {
					templine = reader.readLine();
					continue;
				}
				temp = new Road(deal(templine));
				this.roads.add(temp);
				templine = reader.readLine();
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
			String templine = reader.readLine();
			Car temp;
			while(templine != null) {
				if(templine.charAt(0) == '#') {
					templine = reader.readLine();
					continue;
				}
				temp = new Car(deal(templine));
				this.cars.add(temp);
				templine = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class ToMove{
	
}

class Moved{
	
}