package com.santa.claus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/*
 * 
 * 
 * 
 * 
 * 
 * Helper Class
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */



public class Sorter {
	
	public static enum Size {MAX, MEDIAN, MIN};	
	/*
	 * Takes an array list of presents
	 * returns an array list of IDs that match with descending dimensions
	 * dimension is determined by client
	 * 
	 */
	public static void sortBySize(List<int[]> presents, Size size){
		
		ArrayList<int[]> byDimensionList;
		splitIntoTwoLists(presents,size);
			
		
		
		//printList(tempIDs, tempDimensions);
		
		// convert to arraylist
		
	
		
	}
	// ********************************* Used ***********************************

	public static List<Integer> sortByID(List<Integer> list){
		
		Integer[] presents = new Integer[list.size()];
		
		presents = list.toArray(presents);
		list.clear();

		Arrays.sort(presents);
		int index=presents.length-1;
		
		// place in reverse order
		for(; index>=0; index--){
			
			list.add(presents[index]); 
		}

		return list;
		
	}
	
	public static List<Integer> sortByMax(List<Integer> list, int dimension){
			
		// Reconstruct values for IDs
		List<Integer> maxValues = new ArrayList<Integer>();
		for (Integer id : list)
			maxValues.add(Packer.presentsMap.get(id)[dimension]);

		Integer[] tempIDs = new Integer[list.size()];
		tempIDs = list.toArray(tempIDs);
		
		Integer[] tempValues = new Integer[maxValues.size()];
		tempValues = maxValues.toArray(tempValues);
		
		// Sort IDs along with the values
		quickSort(tempValues, tempIDs, 0, tempIDs.length-1);
		
		list.clear();
		list.addAll(reverse(Arrays.asList(tempIDs)));

		return list;
	}	
	
	public static List<Integer> sortByMedian(List<Integer> list){
		
		// Reconstruct values for IDs
		List<Integer> medianValues = new ArrayList<Integer>();
		for (Integer id : list)
			medianValues.add(Packer.presentsMap.get(id)[1]);

		Integer[] tempIDs = new Integer[list.size()];
		tempIDs = list.toArray(tempIDs);
		
		Integer[] tempValues = new Integer[medianValues.size()];
		tempValues = medianValues.toArray(tempValues);
		
		quickSort(tempValues, tempIDs, 0, tempIDs.length-1);
		
		list.clear();
		list.addAll(Arrays.asList(tempIDs));

		return reverse(list);
	}
	

	
	public static void quickSort(Comparable[] a, Comparable[] b,  int lo, int hi){
		
		if (hi <= lo) return;
		int j = partition(a, b, lo, hi);
		quickSort(a, b, lo, j-1);
		quickSort(a, b, j+1, hi);
		
	}
	
	private static int partition(Comparable[] a, Comparable[] b, int lo, int hi){
		
		int i = lo, j = hi+1;
		Comparable v = a[lo];
		while(true){
			while (less(a[++i], v)) if (i == hi) break;
			while (less(v, a[--j])) if (j == lo) break;
			if (i >= j) break;
			exch(a,i,j);
			exch(b,i,j);
		}
		exch(a, lo, j);
		exch(b, lo, j);
		return j;
		
	}
	
	// Convert List to reflect descending IDs

	public static ArrayList<Integer> reverse(List<Integer> presents){
		
		ArrayList<Integer> reversed = new ArrayList<Integer>();
		
		ListIterator it = presents.listIterator(presents.size());
		
		while(it.hasPrevious()){
			
			reversed.add((Integer)it.previous());
			
		}	
		return reversed;
	}	
	// ********************************* Used **********************************

	public static boolean isSorted(List<Integer> list){
		
		Integer[] a = new Integer[list.size()];
		a = list.toArray(a);
		
		for (int i=1; i<a.length; i++)
			if (less(a[i], a[i-1])) 
				return false;
			return true;
		
	}
	
	
	
	
	
	/* Convert to a list of descending IDs array method
	private static ArrayList<Integer> reverse(Integer[] presents){
		
		ArrayList<Integer> reversed = new ArrayList<Integer>();
		
		int index = presents.length;
		for (Integer present: presents){
			index--;
			reversed.add(presents[index]);
		}
		
		return reversed;
	}
	*/

	private static List<Integer> ids = new ArrayList<Integer>();
	private static List<Integer> dimensions = new ArrayList<Integer>();
	
	
	private static void splitIntoTwoLists(List<int[]> presents, Size size){
		
		int dim=0;
		
		switch(size){
		
		case MIN:

			for (int[] present : presents){
		
				if (present[1] <= present[2]){
					if (present[1] <= present[3])
						dim = present[1];
					else
						dim = present[3];
				}
				else {
					if (present[2] <= present[3])
						dim = present[2];
					else
						dim = present[3];
				}
						
				ids.add(present[0]);
				dimensions.add(dim);	
			}	
			
			break;
			
		case MEDIAN:
			
			for (int[] present : presents){
				
				if (present[1] >= present[2]){
					if (present[1] >= present[3])
						if (present[2] >= present[3])
							dim = present[2];
						else
							dim = present[3];
				}
				else{
					if (present[1] >= present[3])
						dim = present[1];
					else
						if (present[2] >= present[3])
							dim = present[3];
						else
							dim = present[2];
				}
		
				ids.add(present[0]);
				dimensions.add(dim);	
			}
			
			break;
			
			
		case MAX:
			
			for (int[] present : presents){
				
				if (present[1] >= present[2]){
					if (present[1] >= present[3])
						dim = present[1];
					else
						dim = present[3];	
				}
				else {
					if (present[2] >= present[3])
						dim = present[2];
					else
						dim = present[3];
				}
				ids.add(present[0]);
				dimensions.add(dim);	
					
			}	
			
			break;
		
		}	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Sorts in descending order
	public static void mySelectionSort(Comparable[] a, Comparable[] b){
		
		int N = a.length;
		for (int i=0; i<N; i++){
			int min = i;
			
			for (int j=i+1; j<N; j++)
				if(more(a[j], a[min]))
					min = j;
			exch(a, i, min);
			exch(b, i, min);
						
		}
	}
	
	// Sorts in descending order
	public static void myInsertionSort(Comparable[] a, Comparable[] b){
		int N = a.length;
	
		for (int i=1; i<N; i++)
			for (int j=i; j>0 && more(a[j], a[j-1]); j--){
				exch(a, j, j-1);
				exch(b, j, j-1);
			}

	}
	
	// Sorts in descending order
	public static void myShellSort(Comparable[] a, Comparable[] b){
		
		int N = a.length;
		int h = 1;
		while (h<N/3) 
			h = 3*h + 1;
		while (h>=1){
			
			for (int i=h; i<N; i++){
				
				for (int j=i; j>=h && more(a[j], a[j-h]); j-=h){
					exch(a,j,j-h);
					exch(b,j,j-h);
				}
			}
			h = h/3;
	
		}
		
	}
	public static void selectionSort(Comparable[] a){
		
		int N = a.length;
		for (int i=0; i<N; i++){
			int min = i;
			
			for (int j=i+1; j<N; j++)
				if(less(a[j], a[min]))
					min = j;
			exch(a, i, min);
						
		}
	}
	

	
	public static void insertionSort(Comparable[] a){
		int N = a.length;
	
		for (int i=1; i<N; i++)
			for (int j=i; j>0 && less(a[j], a[j-1]); j--)
				exch(a, j, j-1);

	}
	
	public static void shellSort(Comparable[] a){
		
		int N = a.length;
		int h = 1;
		while (h<N/3) 
			h = 3*h + 1;
		while (h>=1){
			
			for (int i=h; i<N; i++){
				
				for (int j=i; j>=h && less(a[j], a[j-h]); j-=h)
					exch(a,j,j-h);
			}
			h = h/3;
	
			
		}
		
	}
	
	private static boolean less(Comparable v, Comparable w){
		return v.compareTo(w) < 0;
	}
	
	private static boolean more(Comparable v, Comparable w){
		return !(v.compareTo(w) < 0);
	}
	
	private static void exch(Comparable[] a, int i, int j){
		Comparable t=a[i];
		a[i]=a[j];
		a[j]=t;
	}
	
	private static void show(Comparable[] a){
		for (int i=0; i<a.length; i++)
			System.out.print(a[i] + " ");
		System.out.println();
	}
	
	public static boolean isSorted(Comparable[] a){
		for (int i=1; i<a.length; i++)
			if (less(a[i], a[i-1])) 
				return false;
			return true;
		
	}
	
	// DEBUG
	private static void printList(Integer[] tempIDs, Integer[] tempDimensions){
		
		for (int index=0; index<tempIDs.length; index++)
			System.out.printf("ID = %d DIM = %d%n", tempIDs[index], tempDimensions[index]);
		
	}

}
