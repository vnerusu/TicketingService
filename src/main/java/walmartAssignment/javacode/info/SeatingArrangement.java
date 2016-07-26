/**
 * 
 */
package walmartAssignment.javacode.info;

import java.util.*;

/**
 * @author vnerusu
 * Arrangement of seats in different predefined levels, 
 * number of seats allocated to each level and their
 * pricing
 */
public enum SeatingArrangement {
	
	ORCHESTRA(1, 100.00 , 25 , 50),
	MAIN(2, 75.0 , 20, 100),
	BALCONY1(3, 50.0, 15 , 100),
	BALCONY2(4, 25.0, 15 ,100);
	
	private int levelId;
	private double price;
	private int numRows;
	private int numRowSeats;
	
	private static HashMap<Integer, SeatingArrangement> mapLevel = new HashMap<Integer, SeatingArrangement>();
	
	static{
		for( SeatingArrangement level : SeatingArrangement.values() ){
			mapLevel.put(level.levelId, level);
		}
	}
	
	private SeatingArrangement(int levelId, double price, int numRows, int numRowSeats){
		this.levelId = levelId;
		this.price = price;
		this.numRows = numRows;
		this.numRowSeats = numRowSeats;
	}

	public int getLevelId() {
		return levelId;
	}

	public double getPrice() {
		return price;
	}

	public int getRows() {
		return numRows;
	}

	public int getRowSeats() {
		return numRowSeats;
	}
	
	public SeatingArrangement getNext(){
		SeatingArrangement[] levels = values();
		return levels[(this.ordinal()+1)%levels.length];
	}

	/**
	* Provide the the index of the first row of the level
	*/
	public int startIndex(){
		switch (this) {
			case ORCHESTRA:
				return 0;
			case MAIN:
				return ORCHESTRA.getRows();
			case BALCONY1:
				return ORCHESTRA.getRows()+MAIN.getRows();
			default:
				return ORCHESTRA.getRows()+MAIN.getRows()+BALCONY1.getRows();
		}
	}
	
	/**
	* Provide the the index of the last row of the level
	*/
	public int endIndex(){
		return this.startIndex()+this.getRows()-1;
	}
	
	public static SeatingArrangement valueOf(int levelId){
		return mapLevel.get(levelId);
	}
	
	@Override
	public String toString() {
		return this.getLevelId()+" - "+this.name();
	}
	
}