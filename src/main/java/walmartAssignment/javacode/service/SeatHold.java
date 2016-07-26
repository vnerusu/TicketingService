/**
 * 
 */
package walmartAssignment.javacode.service;

import java.util.List;

import walmartAssignment.javacode.info.SeatInfo;
import walmartAssignment.javacode.info.SeatingArrangement;
import walmartAssignment.javacode.info.SystemData;

/**
 *  * @author vnerusu
 *
 * Holds the booking information
 */
public class SeatHold {

	private int seatHoldId;
	private String sEmailId;
	private int numSeats;
	private SeatingArrangement venueLevel;
	private List<SeatInfo> seats;
	private long timestamp;
	private String sConfirmCode; 
	private double totalAmount;
	
	public SeatHold(int numSeats, SeatingArrangement venueLevel) {
		this.seatHoldId = SystemData.generateSeatHoldId();
		this.numSeats = numSeats;
		this.venueLevel = venueLevel;
		this.timestamp = System.currentTimeMillis();
		this.sConfirmCode = null;
		this.totalAmount = venueLevel.getPrice() * numSeats;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public String getEmailId() {
		return sEmailId;
	}

	public void setEmailId(String sEmailId) {
		this.sEmailId = sEmailId;
	}

	public int getNoOfSeats() {
		return numSeats;
	}
	
	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}
	
	public SeatingArrangement getVenueLevel() {
		return venueLevel;
	}
	
	public void setVenueLevel(SeatingArrangement venueLevel) {
		this.venueLevel = venueLevel;
	}

	public List<SeatInfo> getSeats() {
		return seats;
	}

	public void setSeats(List<SeatInfo> seats) {
		this.seats = seats;
	}
	
	
	public String getConfirmationCode() {
		return sConfirmCode;
	}

	public void setConfirmationCode(String sConfirmCode) {
		this.sConfirmCode = sConfirmCode;
	}
	
	public double getTotalAmount() {
		return totalAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("\n****** Booking Details ******");
		builder.append("\nBooking ID:  "+ seatHoldId);
		
		if( sConfirmCode != null )
			builder.append("\n COnfirmation Code :"+sConfirmCode);
		
		builder.append("\nEmailID:  "+sEmailId);
		builder.append("\nNumber of seats reserved:  "+numSeats);
		builder.append("\nVenue Level:  "+venueLevel);
		builder.append("\nSeats Details :");
		int rowId = seats.get(0).getRowId();
		builder.append(" Row :"+(rowId+1)+" Seat Numbers :");
		for( SeatInfo seat : seats ){
			if( rowId != seat.getRowId() ){
				rowId = seat.getRowId();
				builder.append("\n                Row :"+(rowId+1)+" Seat Numbers :");
			}
			builder.append(seat+" ");
		}
		builder.append("\nStatus :"+seats.get(0).getStatus());
		builder.append("\nTotal Amount :"+totalAmount);
		builder.append("\n****** ***************** ******\n");
		return builder.toString();
	}


}
