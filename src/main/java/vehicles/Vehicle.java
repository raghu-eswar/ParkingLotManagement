package vehicales;

public abstract class Vehicle {
    public String vehicleNumber;
    public String brand;

    public Vehicle(String vehicleNumber, String brand) {
        this.vehicleNumber = vehicleNumber;
        this.brand = brand;
    }
}
