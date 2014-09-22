package com.santa.claus;

public class Container {
	
	private int[] vertex1;
	
	private int length;
	private int width;
	private int height;

	private int area, volume;
	
	public Container(int newLength, int newWidth, int newHeight, int[] newVertex1){
		
		length = newLength;
		width = newWidth;
		height = newHeight;
		
		vertex1 = newVertex1;
		
		area = length * width;
		volume = area * height;
		
	}
	
	
	public int[] getVertex1(){
		
		return vertex1;
		
	}

	public  int getLength(){
		
		return length;
		
	}
	
	public  int getWidth(){
		
		return width;
		
	}
	
	public  int getHeight(){
		
		return height;
		
	}
	
	public int getArea(){
		
		return area;
	}
	
	public int getVolume() {
		
		if (volume<1)
			System.out.println("Volume is infinite."); // possibly use a throw here
		
		return volume;
	}
	
	

}
