package parkingService;

import parkingSystem.Owner;
import parkingSystem.ParkingLot;
import vehicles.Vehicle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static parkingService.ParkingType.NORMAL;
import static parkingSystem.Status.AVAILABLE;

public class ParkingServices {

    Owner owner;

    public ParkingServices(Owner owner) {
        this.owner = owner;
    }

    public boolean park(Vehicle vehicle, ParkingType type) {
        if (vehicle.parkingLot != null)
            throw new RuntimeException("Vehicle parked already");
        if (owner.parkingLotsAvailable.isEmpty())
            throw new RuntimeException("all parking areas are full");
        List<ParkingLot> possibleLotsForVehicle = owner.parkingLotsAvailable
                                                        .stream()
                                                        .filter(parkingLot -> Arrays.stream(parkingLot.parkingSpots)
                                                                .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                                                                .count() >= vehicle.vehicleSize.size).collect(Collectors.toList());
        if (possibleLotsForVehicle.isEmpty())
            throw new RuntimeException("space not available for "+vehicle.vehicleSize+" vehicles");
        List<Long> countOfVehiclesParked = possibleLotsForVehicle.stream()
                                                                 .map(parkingLot -> parkingLot.parkingSpots)
                                                                 .map(parkingSpots -> Arrays.stream(parkingSpots)
                                                                                        .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                                                                                        .count()).collect(Collectors.toList());
        Optional<Long> minimumVehicles = countOfVehiclesParked.stream().reduce(Math::max);
        int indexOfParkingArea = minimumVehicles.map(countOfVehiclesParked::indexOf).orElse(0);
        return possibleLotsForVehicle.get(indexOfParkingArea).park(vehicle, type);
    }

    public boolean park(Vehicle vehicle) {
        return park(vehicle, NORMAL);
    }

    public boolean unPark(Vehicle vehicle) {
        if (vehicle.parkingLot == null)
            throw new RuntimeException("No car found");
        return vehicle.parkingLot.unPark(vehicle);
    }

    public Integer[] getParkingSpot(Vehicle vehicle) {
        if (vehicle.parkingLot == null)
            throw new RuntimeException("No car found");
        return Arrays.stream(vehicle.parkingLot.getParkingSpot(vehicle)).map(parkingSpot -> parkingSpot.spotNumber).toArray(Integer[]::new);
    }

    public Vehicle[] getVehiclesByColor(Color color) {
        List<ParkingLot> parkingLots = new ArrayList<>(this.owner.parkingLotsAvailable);
        parkingLots.addAll(this.owner.parkingLotsFilled);
        return parkingLots.stream()
                            .map(parkingLot -> Arrays.asList(parkingLot.parkingSpots))
                            .reduce(new ArrayList<>(), (parkingSpots, parkingSpots2) -> {
                                parkingSpots.addAll(parkingSpots2);
                                return parkingSpots;
                            }).stream()
                              .filter(parkingSpot -> parkingSpot.vehicle != null)
                              .filter(parkingSpot -> parkingSpot.vehicle.color.equals(color))
                              .map(parkingSpot -> parkingSpot.vehicle)
                              .distinct()
                              .toArray(Vehicle[]::new);
    }

}
