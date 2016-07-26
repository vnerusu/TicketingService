/**
 * This is a comparator to sort SeatHold objects based on 
 * creation timestamp (oldest first) used by holdingQueue.
 */
package walmartAssignment.javacode.info;

import java.util.Comparator;
import walmartAssignment.javacode.service.SeatHold;

/**
 * @author vnerusu
 *
 */
public class SeatHoldComparator implements Comparator<SeatHold>{

	public int compare(SeatHold o1, SeatHold o2) {
		if(o1.getTimestamp() <= o2.getTimestamp()){
			return -1;
		}
		
		return 1;
	}

}
