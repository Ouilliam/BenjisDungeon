package com.pirogue.items;

public class Head extends Item{
	
	public Head(String name,int ID,String acces,double health, double health_level)
	{
		super(name,String.format("%08d", ID), "armor", acces,0,0,0,0,0,0,health,health_level);
		
	}

}