package parkingSystem;

import vehicles.Vehicle;
import vehicles.VehicleSize;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static parkingSystem.ParkingType.HANDICAPPED;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

public class ParkingSlot {
    public char name;
    public Attendant parkingAttendant;
    public ParkingLot parkingLot;
    public ParkingSpot[] parkingSpots;
    public Status status;

    public ParkingSlot(char name, int numberOfSpotsPerSlot, ParkingLot parkingLot, Attendant parkingAttendant) {
        this.name = name;
        this.parkingLot = parkingLot;
        this.parkingAttendant = parkingAttendant;
        this.parkingSpots = createParkingSpots(numberOfSpotsPerSlot);
        this.status = AVAILABLE;
    }

    private ParkingSpot[] createParkingSpots(int numberOfSpotsPerSlot) {
        ParkingSpot[] parkingSpots = new ParkingSpot[numberOfSpotsPerSlot];
        for (int i = 0; i < parkingSpots.length; i++) {
            parkingSpots[i] = new ParkingSpot(i+1,this);
        }
        return parkingSpots;
    }

    public boolean park(Vehicle vehicle, ParkingType type) {
        ParkingSpot[] spots = getParkingSpot(type, vehicle.vehicleSize);
        Arrays.stream(spots).forEach(parkingSpot -> parkingSpot.park(vehicle, type));
        return Arrays.stream(spots).allMatch(parkingSpot -> vehicle.equals(parkingSpot.vehicle));
    }

    public boolean canPark(VehicleSize vehicleSize) {
        return !this.status.equals(FILLED) &&
                Arrays.stream(this.parkingSpots).filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE)).count() >= vehicleSize.size;
    }

    private ParkingSpot[] getParkingSpot(ParkingType type, VehicleSize vehicleSize) {
        List<ParkingSpot> availableParkingSpots = Arrays.stream(this.parkingSpots)
                .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                .collect(Collectors.toList());
        if (type.equals(HANDICAPPED))
            return availableParkingSpots.subList(availableParkingSpots.size()-vehicleSize.size, availableParkingSpots.size())
                    .toArray(ParkingSpot[]::new);
        return availableParkingSpots.subList(0, vehicleSize.size).toArray(ParkingSpot[]::new);
    }

    public void updateStatus() {
        if (this.status != (this.status = Arrays.stream(this.parkingSpots)
                .anyMatch(parkingSlot -> parkingSlot.status.equals(AVAILABLE))? AVAILABLE : FILLED ))
            this.parkingLot.updateStatus();
    }
}
