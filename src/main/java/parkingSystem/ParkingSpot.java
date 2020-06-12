package parkingSystem;

import vehicles.Vehicle;

import static parkingSystem.Status.*;

public class ParkingSpot {
    public int spotNumber;
    public Status status;
    public Vehicle vehicle;
    public long startTime;
    public long endTime;
    public ParkingArea parkingArea;

    public ParkingSpot(int spotNumber, boolean status, ParkingArea parkingArea) {
        this.spotNumber = spotNumber;
        this.status = AVAILABLE;
        this.parkingArea = parkingArea;
    }

    public ParkingSpot park(Vehicle vehicle) {
        this.startTime = System.currentTimeMillis();
        this.vehicle = vehicle;
        this.updateStatus();
        return this;
    }

    public ParkingSpot unPark(Vehicle vehicle) {
        this.vehicle = null;
        this.endTime = System.currentTimeMillis();
        this.updateStatus();
        return this;
    }

    void updateStatus() {
        this.status = (this.vehicle == null)? AVAILABLE : FILLED;
        parkingArea.updateStatus();
    }

}
