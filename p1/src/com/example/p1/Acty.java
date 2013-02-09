package com.example.p1;


import java.util.ArrayList;

import android.content.Context;

public abstract class Acty {
	public String name;			 
	public String desc;	
	public String actyType;            // P=plan B=basic, A=aggregate
	public String actyKind;				// T=template I=instance
	//public ArrayList<Acty> arrayListOfTasks=new ArrayList<Acty>();
	public void printout(){
	
		//	System.out.println(" name=" + name + "type=" + actyType);
		
		System.out.println(String.format("Acty name= %s type=%s",  name, actyType ));

	}
}



