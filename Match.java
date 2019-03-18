package Match;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Match {
	public static void main(String[] args) {
		ArrayDeque<String> a = new ArrayDeque(6);
		a.add("ss");
		a.add("qq");
		a.add("ww");
		System.out.println(a.size());
		a.pop();
		System.out.println(a.peek());
	}
}
