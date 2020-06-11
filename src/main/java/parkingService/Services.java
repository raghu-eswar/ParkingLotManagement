package parkingService;

import parkingSystem.ParkingArea;
import parkingSystem.ParkingSpot;
import vehicles.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Services {

    List<ParkingArea> parkingAreas = new ArrayList<>();

    public Services(ParkingArea parkingArea, ParkingArea ... parkingAreas) {
        this.parkingAreas.add(parkingArea);
        this.parkingAreas.addAll(Arrays.asList(parkingAreas));
    }

    public boolean park(Vehicle vehicle) {
        Optional<ParkingArea> first = this.parkingAreas.stream().filter(parkingArea -> parkingArea.status).findFirst();
        if (first.isEmpty())
            throw new RuntimeException("all parking areas are full");
       return first.get().park(vehicle);
    }



    public boolean unPark(Vehicle vehicle) {
        ParkingSpot spot = getParkingSpot(vehicle);
        if (spot == null)
            throw new RuntimeException("No car found");
        spot.unPark(vehicle);
        return spot.vehicle == null;
    }

    private ParkingSpot getParkingSpot(Vehicle vehicle) {
        for (ParkingArea parkingArea : parkingAreas) {
            for (ParkingSpot parkingSpot : parkingArea.parkingSpots) {
                if (parkingSpot.vehicle.equals(vehicle))
                    return parkingSpot;
            }
        }
        return null;
    }
}
