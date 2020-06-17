package parkingSystem;

import vehicles.Brand;
import vehicles.VehicleSize;

import java.awt.*;
import java.time.LocalTime;

public class VehicleDetailsDTO {

    public LocalTime parkedTime;
    public LocalTime unParkedTime;
    public char parkingSlotName;
    public VehicleSize vehicleSize;
    public Color vehicleColor;
    public Brand vehicleBrand;
    public String attendantName;
    public ParkingType parkingType;

    public VehicleDetailsDTO(ParkingSpot parkingSpot) {
        this.parkedTime = LocalTime.parse(parkingSpot.startTime);
        this.unParkedTime = (parkingSpot.endTime != null)? LocalTime.parse(parkingSpot.endTime): null;
        this.parkingSlotName = parkingSpot.parkingSlot.name;
        this.vehicleSize = parkingSpot.vehicle.vehicleSize;
        this.vehicleColor = parkingSpot.vehicle.color;
        this.vehicleBrand = parkingSpot.vehicle.brand;
        this.attendantName = parkingSpot.parkingSlot.parkingAttendant.name;
        this.parkingType = parkingSpot.parkingType;
    }
}
