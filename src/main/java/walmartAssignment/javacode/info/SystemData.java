/**
 * 
 */
package walmartAssignment.javacode.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import walmartAssignment.javacode.service.SeatHold;

/**
 * @author vnerusu
 * Stores the status of the seating system
 * Acts as a temporary database for each session
 */
public class SystemData {
  
	// Seat hold timeout
		public static final int TIME_OUT = 120*1000;
		
		private static int seatsRemainingAtL1;
		private static int seatsRemainingAtL2;
		private static int seatsRemainingAtL3;
		private static int seatsRemainingAtL4;
		private static int id;
		
		// Map to hold non-confirmed SeatHold objects 
		private static Map<Integer, SeatHold> bookingHold;
		// Queue to hold non-confirmed SeatHold objects order be oldest first
		private static PriorityBlockingQueue<SeatHold> holdingQueue;
		// Map to hold confirmed SeatHold objects for booking information
		private static Map<String, SeatHold> bookingDetails;
		
		private static SeatInfo[][] seats;
		
		static{
			resetData();
		}
		
		
		/**
		 * Clean and Initialize the database to the default state
		 */
		public static void resetData(){
			
			seatsRemainingAtL1 = SeatingArrangement.ORCHESTRA.getRows() * SeatingArrangement.ORCHESTRA.getRowSeats();
			seatsRemainingAtL2 = SeatingArrangement.MAIN.getRows() * SeatingArrangement.MAIN.getRowSeats();
			seatsRemainingAtL3 = SeatingArrangement.BALCONY1.getRows() * SeatingArrangement.BALCONY1.getRowSeats();
			seatsRemainingAtL4 = SeatingArrangement.BALCONY2.getRows() * SeatingArrangement.BALCONY2.getRowSeats();
			
			id = 0;
			
			bookingHold = new HashMap<Integer, SeatHold>();
			holdingQueue = new PriorityBlockingQueue<SeatHold>(50, new SeatHoldComparator());
			bookingDetails = new HashMap<String, SeatHold>();
			
			int totalRows = SeatingArrangement.ORCHESTRA.getRows() + SeatingArrangement.MAIN.getRows() + SeatingArrangement.BALCONY1.getRows() + SeatingArrangement.BALCONY2.getRows();
			
			seats = new SeatInfo[totalRows][];
			int seatsPerRow = 0;
			
			int counter = 0;
			int start = 0;
			for( SeatingArrangement level : SeatingArrangement.values() ){
				int rows = level.getRows();
				seatsPerRow = level.getRowSeats();
				for( counter = start; counter < rows + start; counter++ ){
					seats[counter] = new SeatInfo[seatsPerRow];
					
					for( int innerCounter = 0; innerCounter < seatsPerRow; innerCounter++ ){
						seats[counter][innerCounter] = new SeatInfo();
					}
				}
				start = counter;
			}
			
		}
		
		/**
		 * Gets the seats availability at a Venue level
		 * 
		 * @param level Venue level at which availability has to found
		 * @return no of seats available
		 */
		public static int getSeatsAvailability(SeatingArrangement level){
			switch (level) {
				case ORCHESTRA:
					return seatsRemainingAtL1;
				case MAIN:
					return seatsRemainingAtL2;
				case BALCONY1:
					return seatsRemainingAtL3;
				default:
					return seatsRemainingAtL4;
			}
		}
		
		
		/**
		 * Reduce and update the seats availability at a Venue level
		 * 
		 * @param noOfSeats number of seats to be reduced
		 * @param level Venue level at which availability has to be updated
		 */
		public static void reduceAvailability(int noOfSeats, SeatingArrangement level){
			switch (level) {
				case ORCHESTRA:
					seatsRemainingAtL1 -= noOfSeats;
					break;
				case MAIN:
					seatsRemainingAtL2 -= noOfSeats;
					break;
				case BALCONY1:
					seatsRemainingAtL3 -= noOfSeats;
					break;
				default:
					seatsRemainingAtL4 -= noOfSeats;
			}
		}
		
		
		/**
		 * Increase and update the seats availability at a Venue level after expiration/release
		 * 
		 * @param noOfSeats number of seats to be increased
		 * @param level Venue level at which availability has to be updated
		 */
		public static void increaseAvailability(int noOfSeats, SeatingArrangement level){
			switch (level) {
				case ORCHESTRA:
					seatsRemainingAtL1 += noOfSeats;
					break;
				case MAIN:
					seatsRemainingAtL2 += noOfSeats;
					break;
				case BALCONY1:
					seatsRemainingAtL3 += noOfSeats;
					break;
				default:
					seatsRemainingAtL4 += noOfSeats;
			}
		}
		
		public static SeatInfo[][] getSeats(){
			return seats;
		}
		
		public static int generateSeatHoldId(){
			id++;
			return id;
		}
		
		public static boolean containsKey(Map<Integer, SeatHold> map, int key){
			return map.containsKey(key);
		}
		
		public static void enqueue(SeatHold seatHold){
			holdingQueue.add(seatHold);
		}
		
		public static void dequeue(SeatHold seatHold){
			holdingQueue.remove(seatHold);
		}
		
		public static boolean isPresentInQueue(SeatHold seatHold){
			return holdingQueue.contains(seatHold);
		}
		
		public static Queue<SeatHold> getQueue(){
			return holdingQueue;
		}
		
		public static void addToBookingMap(SeatHold seatHold){
			bookingDetails.put(seatHold.getConfirmationCode(), seatHold);
		}
		
		public static SeatHold getFromBookingMap(String confirmationCode){
			return bookingDetails.get(confirmationCode);
		}
		
		public static void addToHoldMap(SeatHold seatHold){
			bookingHold.put(seatHold.getSeatHoldId(), seatHold);
		}
		
		public static SeatHold getFromHoldMap(int getSeatHoldId){
			return bookingHold.get(getSeatHoldId);
		}
		
		public static void removeFromHoldMap(int seatHoldId){
			bookingHold.remove(seatHoldId);
		}
		
		public static int getSizeofBookingMap(){
			return bookingDetails.size();
		}
		
		public static int getSizeofHoldQ(){
			return holdingQueue.size();
		}


}
