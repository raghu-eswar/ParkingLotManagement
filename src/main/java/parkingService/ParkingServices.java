package parkingService;

import parkingSystem.Owner;
import vehicles.Vehicle;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static parkingService.ParkingType.NORMAL;
import static parkingSystem.Status.FILLED;

public class Services {

    Owner owner;

    public Services(Owner owner) {
        this.owner = owner;
    }

    public boolean park(Vehicle vehicle, ParkingType type) {
        if (vehicle.parkingArea != null)
            throw new RuntimeException("Vehicle parked already");
        if (owner.parkingAreasAvailable.isEmpty())
            throw new RuntimeException("all parking areas are full");
        List<Long> vehiclesParked = owner.parkingAreasAvailable.stream()
                                                                .map(parkingArea -> parkingArea.parkingSpots)
                                                                .map(parkingSpots -> Arrays.stream(parkingSpots)
                                                                        .filter(parkingSpot -> parkingSpot.status.equals(FILLED))
                                                                        .count()/5).collect(Collectors.toList());
        Optional<Long> minimumVehicles = vehiclesParked.stream().reduce(Math::min);
        int indexOfParkingArea = minimumVehicles.map(vehiclesParked::indexOf).orElse(0);
        return owner.parkingAreasAvailable.get(indexOfParkingArea).park(vehicle, type);
    }

    public boolean park(Vehicle vehicle) {
        return park(vehicle, NORMAL);
    }

    public boolean unPark(Vehicle vehicle) {
        if (vehicle.parkingArea == null)
            throw new RuntimeException("No car found");
        return vehicle.parkingArea.unPark(vehicle);
    }

    public int getParkingSpot(Vehicle vehicle) {
        if (vehicle.parkingArea == null)
            throw new RuntimeException("No car found");
        return vehicle.parkingArea.getParkingSpot(vehicle).spotNumber;
    }

}
