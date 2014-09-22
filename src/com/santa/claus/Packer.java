package com.santa.claus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Packer {

	/*
	 * 
	 * Target Height: 806030
	 * 
	 */

	// List of IDs in descending order
	private static ArrayList<Integer> listOfPresents = new ArrayList<Integer>();

	// presents HashMap values = int[0] = min, int[1] = mean, int[2] = max
	public static HashMap<Integer, int[]> presentsMap = new HashMap<Integer, int[]>();
	
	// 
	public static HashMap <Integer, Integer> spaceMap2D = new HashMap <Integer, Integer>();
	
	HashMap <Integer, HashMap <Integer, Integer> > spaceMap3D = new HashMap <Integer, HashMap <Integer, Integer> >();

	public static void main(String[] args) throws IOException {
		
		int[] vertex1 = {1,1,1};
		Container sleigh = new Container(1000,1000,0, vertex1);
		
		// Initialize spaceMap2D
		
		for(int y=1; y<=1000; y++){
			for (int x=1; x<=1000; x++)
				spaceMap2D.put(1000*(y-1)+x, 0);
		}
		
		
		Timer.start("DataReader");
		// Read in data from CSV file
		presentsMap = DataReader.readData(args[0]);
		Timer.stop();
	
		// Create List of IDs in descending order
		listOfPresents = DataReader.createListOfIDs(presentsMap);
	
		Timer.start("PresentsSelector");
		// Select workingBatch of presents to be placed in sleigh
		List<Integer> workingBatch = PresentsSelector.selectPresents(listOfPresents, sleigh);
		Timer.stop();

		
		
		//*************** load working batching into Algos from this point on ************
		Timer.start("NFDH");
		
		NFDH algoNFDH = new NFDH(workingBatch, sleigh);

		algoNFDH.process();
		
		Timer.stop();
		//DEBUG
		//printList(workingBatch);
		
		Timer.start("print2DSpace");
		//DEBUG 
		//print2DSpace();
	
		/*
		printCoordinates(998029);
		printCoordinates(997818);
		printCoordinates(999744);
		printCoordinates(998909);
		printCoordinates(997856);
		printCoordinates(998818);
		printCoordinates(998085);
		printCoordinates(999353);
		printCoordinates(999679);
		printCoordinates(999460);
		printCoordinates(998271);
		
		printCoordinates(997859);
		printCoordinates(999756);
		printCoordinates(999949);
		printCoordinates(999830);
		printCoordinates(999030);
		printCoordinates(997799);
		printCoordinates(998695);
		printCoordinates(998150);
		printCoordinates(997926);
		printCoordinates(998833);
		printCoordinates(999969);
		
		printCoordinates(999715);
		printCoordinates(998435);
		printCoordinates(999193);
		printCoordinates(999769);
		printCoordinates(999022);
		printCoordinates(999972);
		printCoordinates(999680);
		printCoordinates(999250);
		printCoordinates(998471);
		printCoordinates(999462);
		printCoordinates(997816);
		printCoordinates(998132);
		
		printCoordinates(997932);
		printCoordinates(998860);
		printCoordinates(998966);
		printCoordinates(997870);
		printCoordinates(998971);
		printCoordinates(999971);
		printCoordinates(999562);
		printCoordinates(998629);
		printCoordinates(998730);
		printCoordinates(999600);
		printCoordinates(998470);
		
		printCoordinates(999443);
		printCoordinates(999973);
		printCoordinates(999225);
		printCoordinates(998189);
		printCoordinates(999534);
		printCoordinates(999272);
		printCoordinates(998551);
		printCoordinates(999028);
		printCoordinates(999643);
		printCoordinates(999877);
		printCoordinates(999984);
	*/
	
		
		//print2DSpace(997932);
		
		
		Timer.stop();
		 
	}
	

	public static void printHashValue(int key){
		System.out.println("id = "+key+" "+spaceMap2D.get(key));
	}
	
	// DEBUG Print out 2D Map
	public static void print2DSpace(){
		
		for(int y=1000; y>=1; y--){
			for (int x=1; x<=1000; x++)
				
				System.out.printf("%d",spaceMap2D.get(1000*(y-1)+x));
			System.out.printf("%n");
		}
	}
	
	public static void print2DSpace(int id){
		
		int startX= NFDH.coords[id][1];
		int stopX = NFDH.coords[id][10];
		int startY = NFDH.coords[id][2];
		int stopY = NFDH.coords[id][14];
		
		for (int y=stopY; y>=startY; y--){
			
			for (int x=startX; x<=stopX; x++)
				System.out.printf("%d",spaceMap2D.get(1000*(y-1)+x));
			System.out.printf("%n");
		}

	}
	
	public static void printCoordinates(int id){

		int vertex=0;		
		if(NFDH.coords[id][1]!=0){
			System.out.printf("id=%d",id);
			for (int index=1; index<25; index+=3){
				vertex++;
				System.out.printf(" X%d=%d Y%d=%d Z%d=%d "
						,vertex, NFDH.coords[id][index]
						,vertex, NFDH.coords[id][index+1]
						,vertex, NFDH.coords[id][index+2] );
			}
			System.out.printf("%n");
		}
	
	}
	
	public static void printCoordinates(List<Integer> ids, int to){
		int vertex=0, count=0;
		
		Iterator<Integer> it = ids.iterator();
		while(it.hasNext()){
			count++;
			if (count < to){
				int newID = it.next();
				
				if(NFDH.coords[newID][1]!=0){
					System.out.printf("id=%d",newID);
					for (int index=1; index<25; index+=3){
						vertex++;
						System.out.printf(" X%d=%d Y%d=%d Z%d=%d "
								,vertex, NFDH.coords[newID][index]
								,vertex, NFDH.coords[newID][index+1]
								,vertex, NFDH.coords[newID][index+2] );
					}
					vertex=0;
					System.out.printf("%n");
				}
			}
		}		
	}
	
	
	// DEBUG Print out list using for-each loop
	public static void printList(List<Integer> list){
		
		for(Integer id : list){
			
			System.out.printf("ID = %d min = %d median = %d max = %d%n", id, 
					Packer.presentsMap.get(id)[0],
					Packer.presentsMap.get(id)[1],
					Packer.presentsMap.get(id)[2]);
		}
		System.out.println("Total: "+list.size());
			
	}
	
	// DEBUG Print out list in a range
	public static void printList(ArrayList<Integer> list, int begin, int end){
		
		for(int i = begin; i<=end; i++){
			
			System.out.printf("ID = %d%n", list.get(i));
		}
			
	}

	// Timer Helper class (NO ACCESS to Outerclass members)
	private static class Timer{
		
		static long start, stop;
		
		static double elapsedTime;
		
		static String process;
		
		private static void start(String newProcess){
			
			process = newProcess;
			start = System.nanoTime();
			System.out.printf("%nTimer started for %s process.%n", process);
			
		}
		
		private static void stop(){
			
			stop = System.nanoTime();
			elapsedTime = (stop - start)/1E9;
			
			System.out.printf("%n%.5f seconds to complete %s process.%n", elapsedTime, process);
		}
		
		
	}	
	
	// DataReader Helper class (NO ACCESS to Outerclass members)
	private static class DataReader{

		// ALL LISTS REFERENCES this hashmap
		private static HashMap<Integer, int[]> readData(String csvFile) throws IOException{
			
			BufferedReader in = null;
			String line = null;
			String[] numberString; 
			int[] numbers, values;
			HashMap<Integer, int[]> presents = new HashMap<Integer, int[]>();
			
			try {
		
				in = new BufferedReader(new FileReader(csvFile));
				in.readLine(); // Skip Labels Row - the first row
				
				while ((line = in.readLine()) != null){
					
					// Split data into strings
					numberString = line.split(",");
					
					// For each value create a new object to add to presents HashMap
					// otherwise, arraylist will contain references to only the last entry
					numbers = new int[4];
					values = new int[3];

					// convert from string values to integers
					for(int col=0; col<4; col++){		
						numbers[col] = Integer.parseInt(numberString[col]);
						
						// Column 0 of the CSV file contains the id
						if (col != 0)
							values[col-1] = numbers[col];
					}
					// arrange dimensions into Min, Mean, Max order
					Arrays.sort(values);
					
					// store in hashmap
					presents.put(numbers[0], values);
				}
			
			} catch (FileNotFoundException e) {

				System.out.println("File not found.");
				e.printStackTrace();
			}
			
			finally{
				in.close();
			}
			
			return presents; 
		}
		
		
		//RETURNS A LIST OF DESCENDING IDS
		private static ArrayList<Integer> createListOfIDs(HashMap<Integer, int[]> presents){
			
			ArrayList<Integer> reversed = new ArrayList<Integer>();
			
			int index = presents.size();
			while(index>0){
				
				reversed.add(index);
				index--;
			}
			
			return reversed;
		}
		
	}
	
	/*
	 *  PresentsSelector Helper class (NO ACCESS to Outerclass members)
	 *  
	 *  Given a list of presents (MAX, MIN, MEAN) and a container:
	 *  
	 *  Contains a copy of the master list that is updated each time a new selection is made
	 *  
	 *  return a list of presents whose total combined area is less than or equal to
	 *  the area of the given container and updates the given list of presents.
	 *  
	 */
	private static class PresentsSelector {
			
		private static ArrayList<Integer> selectPresents(ArrayList<Integer> newPresentsList, 
				Container newContainer){
			
			int totalArea, area;
			// Initialize & reset presentsList
			ArrayList<Integer> presentsList = new ArrayList<Integer>();
			
			// Initialize & reset presentsList iterator
			Iterator<Integer> present = newPresentsList.iterator();
			
			// Initialize & reset area of container
			area = newContainer.getArea();
		
			// Initialize & reset the totalArea
			totalArea = 0;

			int id;
			
			//while (totalArea <= area){ *****************************ADD BACK *********
			while(present.hasNext() && totalArea <= area){
				id = present.next();
				
				totalArea = totalArea + (presentsMap.get(id)[0]*presentsMap.get(id)[1]); 
				if (totalArea < area){
					presentsList.add(id);
					present.remove();	
				}
			}
			return presentsList;
	
		}
		
		// DEBUG 
		private static void printPresents(ArrayList<int[]> presents){
			
			for (int[] present : presents)
				System.out.printf("id: %d L: %d W: %d H: %d%n", present[0], present[1], present[2], present[3]);
			
		}
	}

}


