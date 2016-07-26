package walmartAssignment.testcode;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import walmartAssignment.javacode.info.BookingStatus;
import walmartAssignment.javacode.info.SeatingArrangement;
import walmartAssignment.javacode.info.SystemData;
import walmartAssignment.javacode.info.SeatInfo;
import walmartAssignment.javacode.service.BookingTicketService;
import walmartAssignment.javacode.service.SeatHold;

public class BookingTicketServiceTest {

	BookingTicketService bookTicket;
	
	
	/**
	 * Initialize the database before each test
	 */
	@Before
	public void initialize(){
		bookTicket = new BookingTicketService();
		SystemData.resetData();
	}
	
	/**
	 * Tests numSeatsAvailable() method after booking seats at each level
	 */
	@Test
	public void testNumSeatsAvailable(){
		
		assertEquals(1250, bookTicket.numSeatsAvailable(1));
		assertEquals(2000, bookTicket.numSeatsAvailable(2));
		assertEquals(1500, bookTicket.numSeatsAvailable(3));
		assertEquals(1500, bookTicket.numSeatsAvailable(4));
		
		bookTicket.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		assertEquals(1245, bookTicket.numSeatsAvailable(1));
		bookTicket.findAndHoldSeats(11, 1, 2, "xyz@gmail.com");
		assertEquals(1234, bookTicket.numSeatsAvailable(1));
		bookTicket.findAndHoldSeats(30, 1, 2, "pqr@gmail.com");
		assertEquals(1204, bookTicket.numSeatsAvailable(1));
		
		bookTicket.findAndHoldSeats(5, 2, 2, "abc@gmail.com");
		assertEquals(1995, bookTicket.numSeatsAvailable(2));
		bookTicket.findAndHoldSeats(11, 2, 2, "xyz@gmail.com");
		assertEquals(1984, bookTicket.numSeatsAvailable(2));
		bookTicket.findAndHoldSeats(30, 2, 2, "pqr@gmail.com");
		assertEquals(1954, bookTicket.numSeatsAvailable(2));
		
		bookTicket.findAndHoldSeats(5, 3, 3, "abc@gmail.com");
		assertEquals(1495, bookTicket.numSeatsAvailable(3));
		bookTicket.findAndHoldSeats(11, 3, 3, "xyz@gmail.com");
		assertEquals(1484, bookTicket.numSeatsAvailable(3));
		bookTicket.findAndHoldSeats(30, 3, 3, "pqr@gmail.com");
		assertEquals(1454, bookTicket.numSeatsAvailable(3));
		
		bookTicket.findAndHoldSeats(5, 4, 4, "abc@gmail.com");
		assertEquals(1495, bookTicket.numSeatsAvailable(4));
		bookTicket.findAndHoldSeats(11, 4, 4, "xyz@gmail.com");
		assertEquals(1484, bookTicket.numSeatsAvailable(4));
		bookTicket.findAndHoldSeats(30, 4, 4, "pqr@gmail.com");
		assertEquals(1454, bookTicket.numSeatsAvailable(4));
		
	}
	
	
	/**
	 * Tests the seats status before confirmation
	 */
	@Test
	public void testFindAndHoldSeats_seatsStatusHold(){
		SeatHold hold = null;
		
		hold = bookTicket.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		
		for( SeatInfo seat : hold.getSeats() ){
			assertEquals(BookingStatus.HOLD, seat.getStatus());
		}
		
	}
	
	
	/**
	 * Tests the seats allocation based on priority of the levels
	 */
	@Test
	public void testFindAndHoldSeats_levelPriority(){
		
		SeatHold hold = null;
		int counter = 0;
		
		while( counter < 25 ){
			bookTicket.findAndHoldSeats(25, 1, 4, "abc"+counter+"@gmail.com");
			bookTicket.findAndHoldSeats(25, 1, 4, "abc"+counter+"@gmail.com");
			counter++;
		}
		hold = bookTicket.findAndHoldSeats(25, 1, 4, "abc"+26+"@gmail.com");
		assertEquals(SeatingArrangement.MAIN, hold.getVenueLevel());
		
		counter = 0;
		
		bookTicket.findAndHoldSeats(75, 1, 4, "abc"+26+"@gmail.com");
		while( counter < 19 ){
			bookTicket.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			bookTicket.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			counter++;
		}
		
		hold = bookTicket.findAndHoldSeats(25, 1, 4, "abc"+26+"@gmail.com");
		assertEquals(SeatingArrangement.BALCONY1, hold.getVenueLevel());
		
		counter = 0;
		
		bookTicket.findAndHoldSeats(75, 1, 4, "abc"+26+"@gmail.com");
		
		while( counter < 14 ){
			bookTicket.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			bookTicket.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			counter++;
		}
		
		hold = bookTicket.findAndHoldSeats(25, 1, 4, "abc"+26+"@gmail.com");
		assertEquals(SeatingArrangement.BALCONY2, hold.getVenueLevel());
		
	}
	
	
	
	/**
	 * Tests the totalAmount calculation for each level
	 */
	@Test
	public void testReserveSeats_totalAmount(){
		SeatHold hold = null;
		
		hold = bookTicket.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		assertEquals(5*SeatingArrangement.ORCHESTRA.getPrice(), hold.getTotalAmount(),0.00);
		
		hold = bookTicket.findAndHoldSeats(5, 2, 2, "abc@gmail.com");
		assertEquals(5*SeatingArrangement.MAIN.getPrice(), hold.getTotalAmount(), 0.00);
		
		hold = bookTicket.findAndHoldSeats(5, 3, 3, "abc@gmail.com");
		assertEquals(5*SeatingArrangement.BALCONY1.getPrice(), hold.getTotalAmount(), 0.00);
		
		hold = bookTicket.findAndHoldSeats(5, 4, 4, "abc@gmail.com");
		assertEquals(5*SeatingArrangement.BALCONY2.getPrice(), hold.getTotalAmount(), 0.00);
		
	}
	
	
	
	/**
	 * Tests the seats status after confirmation
	 */
	@Test
	public void testGetBookingDetails_seatsStatusReserved(){
		SeatHold hold = null;
		String confirmationCode = null;
		
		hold = bookTicket.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		confirmationCode = bookTicket.reserveSeats(hold.getSeatHoldId(), hold.getEmailId());
		hold = bookTicket.getBookingDetails(confirmationCode);
		
		for( SeatInfo seat : hold.getSeats() ){
			assertEquals(BookingStatus.RESERVED, seat.getStatus());
		}
		
	}
	

}
