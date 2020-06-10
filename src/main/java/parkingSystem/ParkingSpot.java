package parkingSystem;

import vehicales.Vehicle;

public class ParkingSpot {
    public int spotNumber;
    public boolean status;
    public Vehicle vehicle;
    public long startTime;
    public long endTime;

    public ParkingSpot(int spotNumber, boolean status) {
        this.spotNumber = spotNumber;
        this.status = status;
    }

    public void park(Vehicle vehicle) {
        this.startTime = System.currentTimeMillis();
        this.vehicle = vehicle;
    }
}
