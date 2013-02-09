package com.example.p1;

import java.util.ArrayList;

public class PlanActy extends Acty {
	public ArrayList<Acty> actys=new ArrayList<Acty>();
	public PlanActy(){
		super.name = "UNKNOWN";
		super.actyType="P";

	}
	public PlanActy(String name){
		super.name = name;
		super.actyType="P";

	}
	public void printout(){
		System.out.println(String.format("Plan name= %s type=%s",  name, actyType ));
		super.printout();
	}
}
