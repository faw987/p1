package com.example.p1;

import java.util.ArrayList;

import android.content.Context;

public class ActyTest {
	protected static final String TAG = "ActyTest";

	public static void proto1(int id, Context context){

		//// throws IOException

		System.out.println(TAG + "======================================   START  ");
		Acty a = new PlanActy();
		//												a.name = "a";
		BasicActy b = new BasicActy("a");
		b.name = "b";
		AggActy c = new AggActy();
		c.name = "c";
		Acty d = new BasicActy();
		d.name = "d";
		Acty e = new BasicActy();
		e.name = "e";
		Acty f = new BasicActy();
		f.name = "f";
		ArrayList<BasicActy> array1=new ArrayList<BasicActy>();
		
		System.out.println(TAG + " -- a=" + a);

		System.out.println(TAG + " -- b=" + b);

		System.out.println(TAG + "======================================   END  ");

//		array1.add(b);	
//		array1.add(c);
//		array1.add(f);
//		ArrayList<bact> array2=new ArrayList<bact>();
//		array2.add(d);
//		array2.add(e);
//		c.arrayListOfTasks = array2;
//		a.arrayListOfTasks = array1;
		//	//
		//	// activity a is made up of b, c and f
		//	// activity c is in trun made up of d and e
		//	//		
		//	//		a
		//	//			b
		//	//			c
		//	//				d
		//	//				e
		//	//			f
		//

	}
}
