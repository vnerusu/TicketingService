/**
 * 
 */
package walmartAssignment.javacode.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import walmartAssignment.javacode.info.BookingStatus;
import walmartAssignment.javacode.info.ClearHoldQueue;
import walmartAssignment.javacode.info.SeatInfo;
import walmartAssignment.javacode.info.SystemData;
import walmartAssignment.javacode.info.SeatingArrangement;


/**
 * @author vnerusu
 *
 */
public class BookingTicketService implements TicketService {

	// Timer to keep checking the expiration of the seatHold objects 
    Timer timer;
		
	public BookingTicketService() {
	   timer = new Timer();
	   timer.schedule(new ClearHoldQueue(), 100, 100);
	}
			
	/**
	* The number of seats in the requested level that are neither held nor reserved
	*
	* @param SeatingArrangement a numeric venue level identifier to limit the search
	* @return the number of tickets available on the provided level
	*/
	public int numSeatsAvailable(Integer venueLevel) {
		if( venueLevel < 1 || venueLevel > SeatingArrangement.values().length ){
			System.out.println("Invalid Venue level");
		}		
		return SystemData.getSeatsAvailability(SeatingArrangement.valueOf(venueLevel));
	}

	/**
	* Find and hold the best available seats for a customer
	*
	* @param numSeats the number of seats to find and hold
	* @param minLevel the minimum venue level
	* @param maxLevel the maximum venue level
	* @param customerEmail unique identifier for the customer
	* @return a SeatHold object identifying the specific seats and related information
	*/
	public SeatHold findAndHoldSeats(int numSeats, Integer minLevel,
			Integer maxLevel, String customerEmail) {
		if( minLevel > maxLevel || minLevel < 1 || maxLevel > SeatingArrangement.values().length ){
			System.out.println("Invalid Lowest level and/or Highest level values");
		}
		
		SeatHold sHold = findAndHoldSeats(numSeats, SeatingArrangement.valueOf(minLevel), SeatingArrangement.valueOf(maxLevel));
		
		if( sHold != null ){
			sHold.setEmailId(customerEmail);
			SystemData.enqueue(sHold);
			SystemData.addToHoldMap(sHold);
		}
		else{
			System.out.println(numSeats + " not available");
		}
		
		return sHold;
	}

	
	/**
	 * Reserve seats when no hold id
	 */
	public String reserveSeats(int numSeats, Integer minLevel, Integer maxLevel, String customerEmail) {
		SeatHold sHold = findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
		int seatHoldId = sHold.getSeatHoldId();
		return reserveSeats(seatHoldId, customerEmail);
	}
	/**
	* Commit seats held for a specific customer
	*
	* @param seatHoldId the seat hold identifier
	* @param customerEmail the email address of the customer to which the seat hold is assigned
	* @return a reservation confirmation code
	*/
	public String reserveSeats(int seatHoldId, String customerEmail) {
		SeatHold seatHold = SystemData.getFromHoldMap(seatHoldId);
		String sConfirmCode = null;
		// If the corresponding seatHold object is present in the seatHold queue then confirm
		if( !SystemData.isPresentInQueue(seatHold) ){
			System.out.println("Seat Hold expired");
		}
		else{
			sConfirmCode = confirmSeats(SystemData.getQueue().poll());
		}
		
		return sConfirmCode;
	}

	/**
	* gets booking details based on confirmationCode
	*
	* @param confirmationCode booking details identifier
	* @return a SeatHold object containing the booking information
	*/
	public SeatHold getBookingDetails(String confirmationCode) {
        SeatHold sHold = SystemData.getFromBookingMap(confirmationCode);
		if( sHold == null ){
			System.out.println("No booking exists for this confirmation code.");
		}
		return sHold;
	}

	/**
	* Find and hold the best available seats starting from minLevel for a customer
	*
	* @param numSeats the number of seats to find and hold
	* @param minLevel the minimum venue level
	* @param maxLevel the maximum venue level
	* @return a SeatHold object identifying the specific seats and related information
	*/
	private SeatHold findAndHoldSeats(int numSeats, SeatingArrangement minLevel,
			SeatingArrangement maxLevel){
		SeatHold sHold = null;
		
		SeatingArrangement firstLevelWithSeats = null;
		
		SeatingArrangement currentLevel = minLevel;
		// Search best contiguous seats possible for the user stating from minLevel 
		while( currentLevel.getLevelId() <= maxLevel.getLevelId() ){
			if( SystemData.getSeatsAvailability(currentLevel) >= numSeats ){
				SeatInfo startSeat = findBestSeatsAtLevel(numSeats , currentLevel);
				
				if( startSeat != null ){
					sHold = new SeatHold(numSeats, currentLevel);
					sHold.setSeats(getSeatsList(startSeat , numSeats));
					SystemData.reduceAvailability(numSeats, currentLevel);
					
					return sHold;
				}
				else if( firstLevelWithSeats == null ){
					firstLevelWithSeats = currentLevel;
				}
			}
			
			currentLevel = currentLevel.getNext();
		}
		
		/* If the best combination is not possible but the required number of seats are 
		 * available at a level then assign seats based on availability randomly. 
		 */
		if( firstLevelWithSeats != null ){
			sHold = new SeatHold(numSeats, firstLevelWithSeats);
			sHold.setSeats(findRandomSeats( numSeats, firstLevelWithSeats ));
			SystemData.reduceAvailability(numSeats, firstLevelWithSeats);
		}
		
		return sHold;
	}
	
	
	/**
	* Find best seats available at venue level
	*
	* @param numSeats the number of seats to find and hold
	* @param level the level at which seats have to be searched
	* @return a Seat object identifying the specific starting seats from the group of contiguous seats
	*/
	private SeatInfo findBestSeatsAtLevel(int numSeats, SeatingArrangement level){
		for(int counter = level.startIndex(); counter <= level.endIndex(); counter++){
			SeatInfo startSeat = findSeatsAtRow(numSeats , level , counter);
			if( startSeat != null ){
				startSeat.setRowId(counter);
				return startSeat;
			}
		}
		
		return null;
	}
	
	
	
	/**
	* Find best seats available at a particular row of a level
	*
	* @param numSeats the number of seats to find and hold
	* @param level the level at which seats have to be searched
	* @param rowId the row at which seats have to be searched
	* @return a Seat object identifying the specific starting seats from the group of contiguous seats
	* @exception throws AlgorithmException for all algorithm related exceptions
	*/
	private SeatInfo findSeatsAtRow(int numSeats, SeatingArrangement level , int rowId){
		SeatInfo[] seats = SystemData.getSeats()[rowId];
		int countSeats = 0;
		
		for(int counter = 0; counter < seats.length; counter++){
			if( seats[counter].getStatus().equals(BookingStatus.AVAILABLE) ){
				countSeats++;
			}
			else{
				countSeats = 0;
			}
			
			if( countSeats == numSeats ){
				int startSeatId = counter - numSeats + 1;
				seats[startSeatId].setSeatId(startSeatId);
				return seats[startSeatId];
			}
		}
		
		return null;
	}

	
	/**
	* Generate the list of all contiguous seats to be assigned starting with the given startSeat
	*
	* @param startSeat the startSeat where the best possibility is found
	* @param numSeats the number of seats to find and hold
	* @return a list of seats to be assigned to the user
	*/
	private List<SeatInfo> getSeatsList(SeatInfo startSeat, int numSeats){
		List<SeatInfo> seatList = new ArrayList<SeatInfo>();
		int rowId = startSeat.getRowId();
		int seatId = startSeat.getSeatId();
		SeatInfo[] seats = SystemData.getSeats()[rowId];
		int counter = 0;
		while( counter < numSeats ){
			SeatInfo seat = seats[seatId+counter];
			seat.setStatus(BookingStatus.HOLD);
			seat.setSeatId(seatId+counter);
			seat.setRowId(rowId);
			seatList.add(seat);
			counter++;
		}
		
		return seatList;
	}
	
	/**
	* Find random seats at a level based on sequential access and availability
	*
	* @param numSeats the number of seats to find and hold
	* @param level the level at which seats have to be searched
	* @return a list of seats to be assigned to the user
	* @exception throws AlgorithmException for all algorithm related exceptions
	*/
	private List<SeatInfo> findRandomSeats( int numSeats , SeatingArrangement level ){
		List<SeatInfo> list = new ArrayList<SeatInfo>();
		boolean seatAssigned = false;
		int counter = level.startIndex();
		while( !seatAssigned && counter <= level.endIndex()){
			SeatInfo[] seats = SystemData.getSeats()[counter];
			int innerCounter = 0;
			while( !seatAssigned && innerCounter < seats.length){
				SeatInfo seat = seats[innerCounter];
				if( seat.getStatus().equals(BookingStatus.AVAILABLE) ){
					seat.setStatus(BookingStatus.HOLD);
					seat.setSeatId(innerCounter);
					seat.setRowId(counter);
					list.add(seat);
					
					if( list.size() == numSeats ){
						seatAssigned = true;
					}
				}
				
				innerCounter++;
			}
			
			counter++;
		}
		
		return list;
	}
	
	
	/**
	* Commit seats and update the status of the seats from HOLD to RESERVED and generates a confirmationCode
	*
	* @param seatHold the seat hold object containing details of the seats hold
	* @return a reservation confirmation code
	*/
	public String confirmSeats(SeatHold seatHold){
		List<SeatInfo> seatList = seatHold.getSeats();
		SeatInfo[][] seats = SystemData.getSeats();
		for( SeatInfo seat : seatList ){
			seat.setStatus(BookingStatus.RESERVED);
			seats[seat.getRowId()][seat.getSeatId()].setStatus(BookingStatus.RESERVED);
		}
		
		String confirmationCode = String.valueOf(seatHold.getTimestamp());
		seatHold.setConfirmationCode(confirmationCode);
		SystemData.addToBookingMap(seatHold);
		SystemData.removeFromHoldMap(seatHold.getSeatHoldId());
		
		return confirmationCode;
	}

	

}
