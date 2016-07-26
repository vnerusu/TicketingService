/**
 * Main home screen
 */
package walmartAssignment.javacode.start;

import java.io.*;

import walmartAssignment.javacode.info.SystemData;
import walmartAssignment.javacode.service.BookingTicketService;

/**
 * @author vnerusu
 *
 */
public class BookingScreen {
	public static void main(String[] args) {
		
	   int optNum;
	   int levelID;
	   int numSeatsAvail;
	   String sEmailId = "";
	   int numSeats; 
	   int startLevelId;
	   int endLevelId;
	   int seatHoldId;
	   BufferedReader brReadUserInput = new BufferedReader(new InputStreamReader(System.in));
	   BookingTicketService bookTicket = new BookingTicketService();
       		
	   while(true){
		  System.out.println("**************TICKET BOOKING SYSTEM**************");
          System.out.println("Please choose from the following options:-");
          System.out.println("1 - Check availabilty");
          System.out.println("2 - Reserve Seats");
          System.out.println("3 - Hold Seats");
          System.out.println("4 - Confirm Hold seats");
          System.out.println("5 - Get Booking Details");
          System.out.println("5 - Reset the ticket booking system");
          System.out.println("6 - Exit");
          System.out.println("Enter the option number");
          try {
		     optNum = Integer.parseInt(brReadUserInput.readLine());
		  
		     switch(optNum) {
		     case 1:
			     //Check availability
			     System.out.format("%10s%12s%12s%10s%15s", "Level ID", "Level Name", "Price", "Rows", "Seats in Row" +"\n");
			     System.out.format("%10s%12s%12s%8s%10s", "1", "Orchestra", "$100.00", "25", "50" +"\n");
			     System.out.format("%10s%7s%16s%9s%10s", "2", "Main", "$75.00", "20", "100" +"\n");
			     System.out.format("%10s%11s%12s%9s%10s", "3", "Balcony1", "$50.00", "15", "100" +"\n");
			     System.out.format("%10s%11s%12s%9s%10s", "4", "Balcony2", "$40.00", "15", "100" +"\n");
			     System.out.println("Choose the desired level");
			  
		         levelID = Integer.parseInt(brReadUserInput.readLine());
			     numSeatsAvail = bookTicket.numSeatsAvailable(levelID);
			  
			     System.out.println ("Number of seats available at level " + levelID +" = " + numSeatsAvail);
			     break;
		      case 2:
		    	//Reserve Seats
		    	  System.out.println("Please Enter EmailID:");
				  sEmailId = brReadUserInput.readLine();
				  System.out.println("Please Enter Number of seats:");
				  numSeats = Integer.parseInt(brReadUserInput.readLine());
				  System.out.println("Please Enter lowest level:");
				  startLevelId = Integer.parseInt(brReadUserInput.readLine());
				  System.out.println("Please Enter end level:");
				  endLevelId = Integer.parseInt(brReadUserInput.readLine());				  
				  System.out.println("Confirmation Code: "+ bookTicket.reserveSeats(numSeats, startLevelId, endLevelId, sEmailId));
			      break;		    	 
		      case 3:
		    	 //Hold Seats
		    	  System.out.println("Please Enter EmailID:");
				  sEmailId = brReadUserInput.readLine();
				  System.out.println("Please Enter Number of seats:");
				  numSeats = Integer.parseInt(brReadUserInput.readLine());
				  System.out.println("Please Enter lowest level:");
				  startLevelId = Integer.parseInt(brReadUserInput.readLine());
				  System.out.println("Please Enter end level:");
				  endLevelId = Integer.parseInt(brReadUserInput.readLine());
				  System.out.println(bookTicket.findAndHoldSeats(numSeats, startLevelId, endLevelId, sEmailId));
				  break;
		      case 4:
			     //Confirm Hold seats
		    	 System.out.println("Please Enter Book Hold ID:");
				 seatHoldId = Integer.parseInt(brReadUserInput.readLine());
			     System.out.println("Confirmation Code: "+bookTicket.reserveSeats(seatHoldId, sEmailId));
			     break;
		      case 5:
		    	 //Get Booking Details 
		    	  System.out.println("Please enter confirmationCode:");
				  System.out.println(bookTicket.getBookingDetails(brReadUserInput.readLine()));
				  break;
		      case 6:
			     //Reset the ticket booking system/
		    	 SystemData.resetData();
				 System.out.println("All seats are available");
			     break;
		      case 7:
			     //Exit
			     System.exit(0);
			     break;
		  
		     }
	     } 
	     catch (NumberFormatException e) {
		   e.printStackTrace();
	     } 
         catch (IOException e) {
		   e.printStackTrace();
	     }
	  }
	}

}
