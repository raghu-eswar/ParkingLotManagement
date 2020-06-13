package parkingSystem;

import parkingService.ParkingType;
import vehicles.Vehicle;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static parkingService.ParkingType.HANDICAPPED;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

public class ParkingArea {

    public String name;
    public Status status;
    public ParkingSpot [] parkingSpots;
    public Owner owner;

    public ParkingArea(String name, int numberSpots, Owner owner) {
        this.name = name;
        this.status = AVAILABLE;
        this.parkingSpots = createParkingSpots(numberSpots);
        this.owner = owner;
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
        ParkingSpot spot = getParkingSpot(type);
        if (spot != null) {
            spot.park(vehicle);
            return spot.vehicle != null;
        }
        return false;
    }

    public boolean unPark(Vehicle vehicle) {
        ParkingSpot spot = this.getParkingSpot(vehicle);
        if (spot == null)
            throw new RuntimeException("No car found");
        spot.unPark();
        return spot.vehicle == null;
    }

    private ParkingSpot getParkingSpot(ParkingType type) {
        List<ParkingSpot> availableParkingSpotStream = Arrays.stream(this.parkingSpots)
                                                                .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                                                                .collect(Collectors.toList());
        if (type.equals(HANDICAPPED))
            return availableParkingSpotStream.get(availableParkingSpotStream.size()-1);
        return availableParkingSpotStream.get(0);
    }

    public ParkingSpot getParkingSpot(Vehicle vehicle) {
        Optional<ParkingSpot> parkingSpotStream = Arrays.stream(this.parkingSpots)
                                                        .filter(parkingSpot -> parkingSpot.vehicle.equals(vehicle)).findFirst();
        return parkingSpotStream.orElse(null);
    }

    void updateStatus() {
        if (this.status != (this.status = Arrays.stream(parkingSpots)
                                                .anyMatch(parkingSpot -> parkingSpot.status.equals(AVAILABLE))? AVAILABLE : FILLED ))
            owner.updateStatus(this, this.status);
    }

}