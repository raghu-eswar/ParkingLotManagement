package parkingSystem;

import vehicles.Vehicle;

import static parkingSystem.Status.*;

public class ParkingSpot {
    public int spotNumber;
    public Status status;
    public Vehicle vehicle;
    public long startTime;
    public long endTime;
    public ParkingLot parkingLot;

    public ParkingSpot(int spotNumber, ParkingLot parkingLot) {
        this.spotNumber = spotNumber;
        this.status = AVAILABLE;
        this.parkingLot = parkingLot;
    }

    public ParkingSpot park(Vehicle vehicle) {
        this.startTime = System.currentTimeMillis();
        this.vehicle = vehicle;
        this.vehicle.parkingLot = parkingLot;
        this.updateStatus();
        return this;
    }

    public ParkingSpot unPark() {
        this.vehicle.parkingLot = null;
        this.vehicle = null;
        this.endTime = System.currentTimeMillis();
        this.updateStatus();
        return this;
    }

    void updateStatus() {
        this.status = (this.vehicle == null)? AVAILABLE : FILLED;
        parkingLot.updateStatus();
    }

}