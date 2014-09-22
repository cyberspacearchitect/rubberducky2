package com.santa.claus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/*
 * 
 * Modified Next Fit Decreasing Height Algorithm
 * 

 * 
 * 
 */

public class NFDH {
	
	
	List<Integer> sortedList = new ArrayList<Integer>(); // this is returned to client
	
	List<Integer> workingBatch; 
	
	Container container;
	
	int x1, y1, z1; 
	int xStart, yStart, zStart;
	
	public NFDH(List<Integer> newWorkingList, Container newContainer){
		
		workingBatch = newWorkingList;
		container = newContainer;
		
		// Starting corner of first box placed
		x1 = container.getVertex1()[0];
		y1 = container.getVertex1()[1];
		z1 = container.getVertex1()[2];
		
		xStart = x1;
		yStart = y1;
		zStart = z1;
		
		coords = new int[1000001][25];
		
	}
	int sortCount;
	public List<Integer> process(){
		
		sortedList = preSort(workingBatch, 2); // Recursive function 
		
		// DEBUG Packer.printList(sortedList);
		
		
		// DEBUG COUNT
		System.out.println("sortedList Count: "+sortedList.size());
	
		
		pack();
	
		// DEBUG COUNT
		System.out.println("after sort count: "+sortCount);

		return sortedList;

	}
	
	// Pack one level only
	// Start at the lower left corner of container and 
	// Pack towards the right edge
	int yCeiling=0, zCeiling=0;
	int xProjected, yProjected;
	int watchingID;
	List<Integer> rowList = new ArrayList<Integer>();
	
	// UPDATE VERTEX1 
	private void moveX(int id){
		
		// Advance x direction (x1 is the starting x of the next box placed)
		x1 = x1 + Packer.presentsMap.get(id)[0];	

	}
	
	private void initNextRow(){
		
		// Reset x1 to far left
		x1 = 1; 
		y1 = yCeiling + 1;
		
		// Save initial x,y,z point (far left of row)
		xStart = x1;
		yStart = y1;
		zStart = z1;
	}
	
	int index, deltaY;

	private void pack(){
		
		boolean done = false;

		while(!done){
			
			
			ListIterator<Integer> it = sortedList.listIterator();
			
			int id;

			done = true;
			// Places Boxes in current Row (exhaust sortedList)
			while (it.hasNext()){
				
				id = it.next();
				xProjected = x1 + Packer.presentsMap.get(id)[0]-1;
				yProjected = y1 + Packer.presentsMap.get(id)[1]-1;
				
				// Create Y upper bound
				// sortedList already has Z in descending order
				if ((yCeiling < yProjected) && (yProjected <=1000))
					yCeiling = yProjected;
				
				int zTemp = z1 + Packer.presentsMap.get(id)[2]-1;
				if (zCeiling < zTemp)	
					zCeiling = zTemp;

				if ((xProjected <= 1000) && (yCeiling >= yProjected) && (yProjected <= 1000)){
					
					done = false;
					rowList.add(id);
					moveX(id);
					it.remove();
					// DEBUG
					sortCount++;		
				}
			}
			
			// Sort all items by descending Y in the row
			Sorter.sortByMax(rowList, 1);
			
			Iterator<Integer> itRowList = rowList.iterator();
			
			// Retrieve saved x1,y1,z1 (far bottom left position)
			x1 = xStart;
			y1 = yStart;
			z1 = zStart;
			
			while (itRowList.hasNext()){
				
				int item = itRowList.next();
				placeBox(item, x1, y1, z1);
				updateSpaceMap(item);
				moveX(item);
			}
			
			// DEBUG
			if(rowList.size()!=0)
				Packer.printCoordinates(rowList.get(0));
			rowList.clear();

			initNextRow();
			if (sortedList.size()==0)
				done = true;
		}
		
		
	}
				
			

	int previous, key;
	private void updateSpaceMap(int id){
		Random generator = new Random();
		int rand1 = generator.nextInt(8) + 1;
		int rand2 = generator.nextInt(8) + 1;
		
		if (rand1 == previous)
			rand1 = generator.nextInt(8) + 1;
		if (rand2 == previous)
			rand2 = generator.nextInt(8) + 1;
		
		
		// UPDATE SPACE MAP
		//for(int z=coords[id][3]; z<=coords[id][6]; z++)
			for(int y=coords[id][2]; y<=coords[id][14]; y++)
				for(int x=coords[id][1]; x<=coords[id][10]; x++){
					
					switch(id%2){
					
					case 0:
						key = convertTo2DSpace(x,y);
						Packer.spaceMap2D.put(key, rand1);
						previous = rand1;
						break;
						
					default:
						Packer.spaceMap2D.put(convertTo2DSpace(x,y), rand2);
						previous = rand2;
			
					}
			
					// ADD spaceMap3D here.........................
				
				}
	}
	
	
	private int convertTo2DSpace(int x, int y){
		
		int key = 1000*(y-1) + x;
		
		return key;
		
		
	}
	
	private boolean boxFits(int id){
		
		// Since this container is ALWAYS AN UNFILLED RECTANGLE
		// compare dimensions and area
		
		int width = Packer.presentsMap.get(id)[0];
		int length = Packer.presentsMap.get(id)[1];
		
		if(container.getArea() >= (width*length)){
			
			if ((container.getLength() >= width) &&
			   (container.getWidth() >= length))
			   
				return true;
		
			if ((container.getLength() >= length) &&
				   (container.getWidth() >= width))
				return true;

		}
		return false;

	}
	
	/*
	 * 
	 * Every Box is placed down at Lower left corner
	 * 
	 * 0 = id, 1 = X1, 2= Y1, 3 = Z1, ..., 22 = X8, 23 = Y8, 24 = Z8
	 * 
	 */
	public static int[][] coords; // add 1 extra cell so we can start at (1,1) instead of (0,0)

	private void placeBox(int id, int X1, int Y1, int Z1){
		
		
		// vertex 1
		coords[id][1] = X1; // X1
		coords[id][2] = Y1; // Y1
		coords[id][3] = Z1; // Z1
		
		// vertex 2
		coords[id][4] = X1; 										// X2
		coords[id][5] = Y1; 										// Y2
		coords[id][6] = Z1 + (Packer.presentsMap.get(id)[2] - 1); 	// Z2
		
		// vertex 3
		coords[id][7] = X1 + (Packer.presentsMap.get(id)[0] - 1);  	// X3
		coords[id][8] = Y1; 					 					// Y3
		coords[id][9] = coords[id][6];     							// Z3
		
		// vertex 4
		coords[id][10] = coords[id][7]; 	// X4
		coords[id][11] = Y1;				// Y4
		coords[id][12] = Z1;				// Z4
		
		// vertex 5
		coords[id][13] = X1; 										// X5
		coords[id][14] = Y1 + (Packer.presentsMap.get(id)[1] -1); 	// Y5
		coords[id][15] = Z1; 										// Z5
		
		// vertex 6
		coords[id][16] = X1; 				// X6
		coords[id][17] = coords[id][14]; 	// Y6
		coords[id][18] = coords[id][6]; 	// Z6
		
		// vertex 7
		coords[id][19] = coords[id][7]; 	// X7
		coords[id][20] = coords[id][14];  	// Y7
		coords[id][21] = coords[id][6];  	// Z7
		
		// vertex 8
		coords[id][22] = coords[id][7]; 	// X8
		coords[id][23] = coords[id][14]; 	// Y8
		coords[id][24] = Z1; 				// Z8
		
		
	}

	/*
	 * PREPROCESSING SORT
	 * 
	 * RECURSIVE METHOD that takes a list that is sorted by descending IDs
	 * and continues to sort all of its dimensions until
	 * min < median < max for all IDs
	 * 
	 * Given a list of UNSORTED presents:	
	 * 1. Sort by descending MAX values
	 * 2. From 1, sort by descending MEDIAN values
	 * 3. From 2, sort by descending MIN values
	 *  4. (1,2,3) sorts by descending IDs
	 */

	public List<Integer> preSort(List<Integer> sortedByD, int dimension){

		// BASE CASE
		if (sortedByD.size()==1 || dimension < 0) return sortedByD;
		
		// Sort List by Descending Dimension
		List<Integer> sortedByMax = Sorter.sortByMax(sortedByD, dimension);
	
		List<Integer> sortedTotal = new ArrayList<Integer>();	
		List<Integer> sortedPartial = new ArrayList<Integer>();
		List<Integer> temp = new ArrayList<Integer>();
		int dLast=0, d=0, count=0;

		// Partition current list into smaller subsets
		for(Integer id : sortedByMax){
			
			count++;
			d = Packer.presentsMap.get(id)[dimension];
	
			if(d < dLast){
				
				if (temp.size() > 1){
				
					sortedPartial = Sorter.sortByID(temp);
					sortedTotal.addAll(preSort(sortedPartial, dimension-1));
				}
				else {
					
					sortedPartial = temp;
					sortedTotal.addAll(sortedPartial);
				}
	
				sortedPartial.clear();
				
				/*
				 * 
				 * figure out why by clearing sortedPartial, temp clears as well
				 * 
				 */
	
				// Adds the first id entry of next batch of equal Ds
				temp.add(id);
				
				// Adds the last id entry of the list to sortedTotal
				if(count == sortedByMax.size()){
					
					sortedPartial = temp;
					sortedTotal.addAll(sortedPartial);
				}
			}
			else{
				
				temp.add(id);
			}
			
			dLast = d;
		}
		
		return sortedTotal;	
	}
	
}
/*
if ((yCeiling > 1000) && (deltaX > xPosThreshold)){
	
	// continue placing next item until end of list
	// regardless of yCeiling or xCeiling
	int tempIndex = index;
	
	// Save xCeiling & yCeiling to take up where it left off
	// Possibility that By gets placed and Ay > By so I need to
	// place By first in the new row.
	// NOT needed; I will take advantage of Java's pass by value
	int xCeilingTemp = xCeiling;
	int yCeilingTemp = yCeiling;
		
	while(tempIndex < sortedList.size()){
		
		int idTemp = sortedList.get(tempIndex);
		if ((xCeiling <= 1000) && (yCeiling <= 1000)){
			
			
			rowList.add(sortedList.get(tempIndex));	
			
			// UPDATE VERTEX1
			x1 = xCeiling + 1;	
			int yTemp = Packer.presentsMap.get(idTemp)[1]-1 + y1;
			int zTemp = z1 + Packer.presentsMap.get(idTemp)[2]-1;
			if (zTemp > zMax)
				zMax = zTemp;
			if (yTemp > yMax)
				yMax = yTemp;
			
		}
		
		tempIndex++;
		
		xCeilingTemp = xCeiling;
		yCeilingTemp = yCeiling;
		
		
		xCeiling = x1 + Packer.presentsMap.get(idTemp)[0]-1;
		yCeiling = y1 + Packer.presentsMap.get(idTemp)[1]-1;
		
		
	}
	
	xCeiling = xCeilingTemp;
	yCeiling = yCeilingTemp;
	
	
			
}
// 1st box that is unable to fit in remaining x space
// Fit as many boxes as possible in remaining x space


if ((deltaX < xNegThreshold) && (yCeiling <= 1000)){
	
	// Advance through the rest of sortedList and place if possible
	continueList(index, xCeiling, yCeiling);
	
}



else {
	// DEBUG System.out.println(id+" xCeiling:"+xCeiling+" yCeiling:"+yCeiling);
	

	// DEBUG System.out.println("\nnew row id = "+id);
	
	// Place boxes in Ys in descending order in X direction
	Sorter.sortByMax(rowList, 1);
	
	// DEBUG Packer.printList(rowList);
	
	x1 = xStart;
	y1 = yStart;
	z1 = zStart;
	
	for(Integer rowItem : rowList){
		
		placeBox(rowItem, x1, y1, z1);
		
		// DEBUG
		sortCount++;
		
		// DEBUG 
		updateSpaceMap(rowItem);
		
		// UPDATE VERTEX1
		x1 = x1 + Packer.presentsMap.get(rowItem)[0];
		
		
	}

	// DEBUG Packer.printCoordinates(rowList,rowList.size()) ;
	
	
	rowList.clear();
	//tempRowList.clear(); // NOT NEEDED rowLIst references tempRowList
	// UPDATE VERTEX1 AND START A NEW ROW
	x1 = 1; 
	xStart = x1;
	y1 = yMax + 1;
	yStart = y1;
	
	// If current box is placed will it fit?
	xCeiling = x1 + Packer.presentsMap.get(id)[0]-1;
	yCeiling = y1 + Packer.presentsMap.get(id)[1]-1;
	if ((xCeiling <= 1000) && (yCeiling <= 1000)){
		
		rowList.add(id);
		
		// UPDATE VERTEX1
		x1 = xCeiling + 1;	
		int yTemp = Packer.presentsMap.get(id)[1]-1 + y1;
		int zTemp = z1 + Packer.presentsMap.get(id)[2]-1;
		if (zTemp > zMax)
			zMax = zTemp;
		if (yTemp > yMax)
			yMax = yTemp;
		
	}
	
	
}		
}

index++;

}
}*/



/*
int id;
int xPosThreshold = 100;  // ADJUST to increase placement
int xNegThreshold = -100; // ADJUST to increase placement
boolean start = true;

ListIterator<Integer> it = sortedList.listIterator();

while(it.hasNext()){
	
	id = it.next();
	// If current box is placed will it fit?
	// y1 does NOT change for current row
	xCeiling = x1 + Packer.presentsMap.get(id)[0]-1;
	yCeiling = y1 + Packer.presentsMap.get(id)[1]-1;
	
	// Do Not place box beyond container edges

	while ((xCeiling <= 1000) && (yCeiling <= 1000)){

		rowList.add(id);	
		advanceVertex(id);
		id = it.next();
		xCeiling = x1 + Packer.presentsMap.get(id)[0]-1;
		yCeiling = y1 + Packer.presentsMap.get(id)[1]-1;
		
	}	

	// keep searching the list & fill x direction
	continueList();
	
	// Pack Presents into the row
	ListIterator<Integer> rowItem = rowList.listIterator();
	while (rowItem.hasNext()){
		
		int ID = rowItem.next();
		placeBox(ID, x1, y1, z1);
		
		// DEBUG
		sortCount++;
		
		// DEBUG 
		updateSpaceMap(ID);			
	}
	rowList.clear();
	initNextRow();	
}
*/
//z1 = zMax + 1;	