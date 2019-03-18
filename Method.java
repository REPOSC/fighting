//package Match;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//class Method {
//	
//	
//	public void initMethod(String file) throws IOException {
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//			String templine = reader.readLine();
//			Car temp;
//			while(templine != null) {
//				if(templine.charAt(0) == '#') {
//					templine = reader.readLine();
//					continue;
//				}
//				temp = new Car(deal(templine));
//				this.cars.add(temp);
//				templine = reader.readLine();
//			}
//			reader.close();
//			this.notMoved = this.cars;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
