package Match;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class Match {
	public static void main(String[] args) throws IOException {
		Map myMap = new Map(".\\src\\Match\\Cross.txt", ".\\src\\Match\\Road.txt", ".\\src\\Match\\Car.txt", ".\\src\\Match\\ans.txt");
		System.out.println("总调度时间为： "+myMap.run());

	}
}
