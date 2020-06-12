package parkingSystem;

import vehicles.Vehicle;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


public class ParkingArea {

    public String name;
    public boolean status;
    public ParkingSpot [] parkingSpots;

    public ParkingArea(String name, int numberSpots) {
        this.name = name;
        this.status = true;
        this.parkingSpots = createParkingSpots(numberSpots);
    }

    private ParkingSpot[] createParkingSpots(int numberSpots) {
        AtomicInteger number = new AtomicInteger();
        ParkingSpot[] parkingSpots = new ParkingSpot[numberSpots];
        for (int i = 0; i < parkingSpots.length; i++) {
            parkingSpots[i] = new ParkingSpot(number.incrementAndGet(), true, this);
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
        spot.unPark(vehicle);
        return spot.vehicle == null;
    }

    private ParkingSpot getParkingSpot() {
        for (ParkingSpot spot :this.parkingSpots) {
            if (spot.status) return spot;
        }
        return null;
    }

    private ParkingSpot getParkingSpot(Vehicle vehicle) {
        for (ParkingSpot parkingSpot : this.parkingSpots) {
            if (parkingSpot.vehicle.equals(vehicle))
                return parkingSpot;
        }
        return null;
    }

    void updateStatus() {
        this.status = Arrays.stream(parkingSpots).anyMatch(parkingSpot -> parkingSpot.status);
    }

}
