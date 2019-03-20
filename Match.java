package Match;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class Match {
	public static void main(String[] args) throws IOException {
		Map myMap = new Map(".\\Cross.txt", ".\\Road.txt", ".\\Car.txt", ".\\ans.txt");
		System.out.println(myMap.run());

	}
}
