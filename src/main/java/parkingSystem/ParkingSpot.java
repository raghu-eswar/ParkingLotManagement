package parkingSystem;

import vehicles.Vehicle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

public class ParkingSpot {
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public int spotNumber;
    public Status status;
    public Vehicle vehicle;
    public String startTime;
    public String endTime;
    public ParkingType parkingType;
    public ParkingSlot parkingSlot;

    public ParkingSpot(int spotNumber, ParkingSlot parkingSlot) {
        this.spotNumber = spotNumber;
        this.status = AVAILABLE;
        this.parkingSlot = parkingSlot;
    }

    public ParkingSpot park(Vehicle vehicle, ParkingType parkingType) {
        this.startTime = timeFormatter.format(LocalDateTime.now());
        this.vehicle = vehicle;
        this.vehicle.parkingSpot = this;
        this.parkingType = parkingType;
        this.updateStatus();
        return this;
    }

    public ParkingSpot unPark() {
        this.vehicle.parkingSpot = null;
        this.vehicle = null;
        this.endTime = timeFormatter.format(LocalDateTime.now());
        this.parkingType = null;
        this.updateStatus();
        return this;
    }

    public void updateStatus() {
        this.status = (this.vehicle == null)? AVAILABLE : FILLED;
        parkingSlot.updateStatus();
    }

}