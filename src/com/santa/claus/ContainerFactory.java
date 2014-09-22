package com.santa.claus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * 
 * Given a rectangular 2D space of area XY
 * Identifies and returns rectangular containers
 * 
 * 
 */

public class ContainerFactory {
	
	// Containers returned to client
	List<Container> containers;
	
	// object that maps a rectangular 2D space to filled or unfilled cells
	// unfilled = 0, filled = 1
	Map<Integer, Integer> space2DMap;
	
	public ContainerFactory(Map<Integer,Integer> newSpace2DMap){
		
		space2DMap = newSpace2DMap;
		containers = new ArrayList<Container>();
		space2DMap = new HashMap<Integer, Integer>();
	}	
	
	public void createContainer(){
		
	
		/*
		 * 
		 * Approach #1
		 * 
		 * iterate down rows and find the first 0
		 * move up
		 * 
		 */
		
		//for(Integer space : space2DMap.keySet())
			
		
		
	}

}
