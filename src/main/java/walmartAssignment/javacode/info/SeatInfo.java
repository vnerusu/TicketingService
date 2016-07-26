/**
 * 
 */
package walmartAssignment.javacode.info;

/**
 * @author vnerusu
 * Initialize and assign information for each seat in the theater
 */
public class SeatInfo {

	private int rowId;
	private int seatId;
	private BookingStatus status;
	
	public SeatInfo(){
		this.status = BookingStatus.AVAILABLE;
	}
	
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public int getSeatId() {
		return seatId;
	}
	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}
	
	@Override
	public String toString() {
		return String.valueOf(seatId+1);
	}
	
	public BookingStatus getStatus() {
		return status;
	}
	
	public void setStatus(BookingStatus status) {
		this.status = status;
	}
}
