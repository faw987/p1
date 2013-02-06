package com.example.p1;

public class BasicActy extends Acty {
	public String plan;		// if associated this is corresponding plan
	public int seq;
	public String duration;
	public String urls;
	public String location;
	public BasicActy(){
		super.name = "UNKNOWN";
		super.actyType="B";
	}
	public BasicActy(String name){
		super.name = name;
		super.actyType="B";
	}

}
