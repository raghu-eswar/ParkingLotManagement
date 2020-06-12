package parkingService;

import parkingSystem.Owner;
import vehicles.Vehicle;

public class Services {

    Owner owner;

    public Services(Owner owner) {
        this.owner = owner;
    }

    public boolean park(Vehicle vehicle) {
        if (vehicle.parkingArea != null)
            throw new RuntimeException("Vehicle parked already");
        if (owner.parkingAreasAvailable.isEmpty())
            throw new RuntimeException("all parking areas are full");
       return owner.parkingAreasAvailable.get(0).park(vehicle);
    }

    public boolean unPark(Vehicle vehicle) {
        if (vehicle.parkingArea == null)
            throw new RuntimeException("No car found");
        return vehicle.parkingArea.unPark(vehicle);
    }

}
