package vehicles;

import parkingSystem.ParkingLot;

public class Vehicle {

    public VehicleSize vehicleSize;
    public ParkingLot parkingLot;

    public Vehicle(VehicleSize vehicleSize) {
        this.vehicleSize = vehicleSize;
    }
}
