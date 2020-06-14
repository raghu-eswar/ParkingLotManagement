package vehicles;

import parkingSystem.ParkingLot;

import java.awt.Color;

public class Vehicle {

    public VehicleSize vehicleSize;
    public Color color;
    public ParkingLot parkingLot;

    public Vehicle(VehicleSize vehicleSize, Color color) {
        this.vehicleSize = vehicleSize;
        this.color = color;
    }

}
