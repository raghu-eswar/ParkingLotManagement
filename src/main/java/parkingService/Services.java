package parkingService;

import parkingSystem.ParkingArea;
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
        if (vehicle.parkingArea != null)
            throw new RuntimeException("Vehicle parked already");
        Optional<ParkingArea> first = this.parkingAreas.stream().filter(parkingArea -> parkingArea.status).findFirst();
        if (first.isEmpty())
            throw new RuntimeException("all parking areas are full");
       return first.get().park(vehicle);
    }

    public boolean unPark(Vehicle vehicle) {
        if (vehicle.parkingArea == null)
            throw new RuntimeException("No car found");
        return vehicle.parkingArea.unPark(vehicle);
    }

}
