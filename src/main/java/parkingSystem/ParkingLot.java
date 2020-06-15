package parkingSystem;

import parkingService.ParkingType;
import vehicles.Vehicle;
import vehicles.VehicleSize;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static parkingService.ParkingType.HANDICAPPED;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

public class ParkingLot {

    public String name;
    public Status status;
    public ParkingSpot [] parkingSpots;
    public Attendant parkingAttendant;
    public List<JourneyDetails> logBook;
    public Owner owner;

    public ParkingLot(String name, int numberSpots, Owner owner, Attendant parkingAttendant) {
        this.name = name;
        this.status = AVAILABLE;
        this.parkingSpots = createParkingSpots(numberSpots);
        this.owner = owner;
        this.parkingAttendant = parkingAttendant;
    }

    private ParkingSpot[] createParkingSpots(int numberSpots) {
        AtomicInteger number = new AtomicInteger();
        ParkingSpot[] parkingSpots = new ParkingSpot[numberSpots];
        for (int i = 0; i < parkingSpots.length; i++) {
            parkingSpots[i] = new ParkingSpot(number.incrementAndGet(), this);
        }
        return parkingSpots;
    }

    public boolean park(Vehicle vehicle, ParkingType type) {
        ParkingSpot[] spots = getParkingSpot(type, vehicle.vehicleSize);
        Arrays.stream(spots).forEach(parkingSpot -> parkingSpot.park(vehicle));
        return Arrays.stream(spots).allMatch(parkingSpot -> parkingSpot.vehicle.equals(vehicle));
    }

    public boolean unPark(Vehicle vehicle) {
        ParkingSpot[] spots = this.getParkingSpot(vehicle);
        if (spots == null)
            throw new RuntimeException("No car found");
        Arrays.stream(spots).forEach(ParkingSpot::unPark);
        return Arrays.stream(spots).allMatch(parkingSpot -> parkingSpot.vehicle == null);
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

    public ParkingSpot[] getParkingSpot(Vehicle vehicle) {
        return Arrays.stream(this.parkingSpots)
                        .filter(parkingSpot -> vehicle.equals(parkingSpot.vehicle))
                        .toArray(ParkingSpot[]::new);
    }

    void updateStatus() {
        if (this.status != (this.status = Arrays.stream(parkingSpots)
                                                .anyMatch(parkingSpot -> parkingSpot.status.equals(AVAILABLE))? AVAILABLE : FILLED ))
            owner.updateStatus(this, this.status);
    }

}