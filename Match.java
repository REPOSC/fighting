package Match;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class Match {
	public static void main(String[] args) {
		ArrayDeque<test> a = new ArrayDeque(6);
		a.add(new test(1,2));
		a.add(new test(3,4));
		Iterator<test> i = a.iterator();
		
		i.next().a = 9;

		System.out.println(a.getFirst().a);

	}
}

class test{
	int a=0;
	int b =0;
	test(int a,int b){
		this.a=a;
		this.b =b;
	}
}