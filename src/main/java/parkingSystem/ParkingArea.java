package parkingSystem;

import vehicales.Vehicle;
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
            parkingSpots[i] = new ParkingSpot(number.incrementAndGet(), true);
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

    private ParkingSpot getParkingSpot() {
        for (ParkingSpot spot :this.parkingSpots) {
            if (spot.status) return spot;
        }
        return null;
    }
}
