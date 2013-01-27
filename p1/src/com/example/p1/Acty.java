package com.example.p1;

 
import java.util.ArrayList;

import android.content.Context;

public class Acty {
	public String name;			 
	public String desc;	
	public ArrayList<Acty> arrayListOfTasks=new ArrayList<Acty>();

	public static void proto1(int id, Context context)
			// throws IOException
	{
		System.out.println("======================================   Acty  ");
		Acty a = new Acty();
		a.name = "a";
		Acty b = new Acty();
		b.name = "b";
		Acty c = new Acty();
		c.name = "c";
		Acty d = new Acty();
		d.name = "d";
		Acty e = new Acty();
		e.name = "e";
		Acty f = new Acty();
		f.name = "f";
		ArrayList<Acty> array1=new ArrayList<Acty>();
		array1.add(b);
		array1.add(c);
		array1.add(f);
		ArrayList<Acty> array2=new ArrayList<Acty>();
		array2.add(d);
		array2.add(e);
		c.arrayListOfTasks = array2;
		a.arrayListOfTasks = array1;
//
// activity a is made up of b, c and f
// activity c is in trun made up of d and e
//		
//		a
//			b
//			c
//				d
//				e
//			f
		
	}

}
