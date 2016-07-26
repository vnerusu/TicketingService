/**
 * 
 */
package walmartAssignment.javacode.info;

import java.util.List;
import java.util.Queue;
import java.util.TimerTask;

import walmartAssignment.javacode.service.SeatHold;
/**
 * @author vnerusu
 * 
 * this checks for the timer of hold seats and releases them accordingly
 */

public final class ClearHoldQueue extends TimerTask{

	@Override
	public void run() {
		Queue<SeatHold> originalQueue = SystemData.getQueue();
		
		while( originalQueue.peek() != null ){
			SeatHold seatHold = originalQueue.peek();
        	if( (System.currentTimeMillis() - seatHold.getTimestamp()) > SystemData.TIME_OUT ){
        		originalQueue.poll();
        		SystemData.removeFromHoldMap(seatHold.getSeatHoldId());
        		List<SeatInfo> seatList = seatHold.getSeats();
        		SeatInfo[][] seats = SystemData.getSeats();
        		for( SeatInfo seat : seatList ){
        			seats[seat.getRowId()][seat.getSeatId()].setStatus(BookingStatus.AVAILABLE);
        		}
        		
        		SystemData.increaseAvailability(seatList.size(), seatHold.getVenueLevel());
        	}
        	else{
        		break;
        	}
		}
	}

}