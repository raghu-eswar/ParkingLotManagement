package vehicles;

import parkingSystem.ParkingLot;

import java.awt.Color;

public class Vehicle {

    public VehicleSize vehicleSize;
    public Color color;
    public String brand;
    public ParkingLot parkingLot;

    public Vehicle(VehicleSize vehicleSize, Color color, String brand) {
        this.vehicleSize = vehicleSize;
        this.color = color;
        this.brand = brand;
    }

}
