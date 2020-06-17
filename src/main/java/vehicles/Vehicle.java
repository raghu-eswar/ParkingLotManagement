package vehicles;

import parkingSystem.ParkingSpot;

import java.awt.*;

public class Vehicle {

    public VehicleSize vehicleSize;
    public Color color;
    public Brand brand;
    public ParkingSpot parkingSpot;

    public Vehicle(VehicleSize vehicleSize, Color color, Brand brand) {
        this.vehicleSize = vehicleSize;
        this.color = color;
        this.brand = brand;
    }

}
