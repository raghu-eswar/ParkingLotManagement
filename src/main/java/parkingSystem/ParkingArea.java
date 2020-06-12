package parkingSystem;

import vehicles.Vehicle;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static parkingSystem.Status.*;

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

    public boolean park(Vehicle vehicle) {
        ParkingSpot spot = getParkingSpot();
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

    private ParkingSpot getParkingSpot() {
        for (ParkingSpot spot :this.parkingSpots) {
            if (spot.status.equals(AVAILABLE)) return spot;
        }
        return null;
    }

    public ParkingSpot getParkingSpot(Vehicle vehicle) {
        for (ParkingSpot parkingSpot : this.parkingSpots) {
            if (parkingSpot.vehicle.equals(vehicle))
                return parkingSpot;
        }
        return null;
    }

    void updateStatus() {
        if (this.status != (this.status = Arrays.stream(parkingSpots).anyMatch(parkingSpot -> parkingSpot.status.equals(AVAILABLE))? AVAILABLE : FILLED ))
            owner.updateStatus(this, this.status);
    }

}





