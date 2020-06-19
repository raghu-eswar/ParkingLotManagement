package parkingSystem;

import vehicles.Vehicle;
import vehicles.VehicleSize;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;
import static vehicles.VehicleSize.SMALL;

public class ParkingLot {

    public String name;
    public Status status;
    public ParkingSlot [] parkingSlots;
    public Owner owner;

    public ParkingLot(String name, Owner owner, Attendant parkingAttendant, int numberSpots) {
        this(name, owner, parkingAttendant, numberSpots, numberSpots);
    }

    public ParkingLot(String name, Owner owner, Attendant parkingAttendant, int numberSpots, int numberOfSpotsPerSlot) {
        this.name = name;
        this.status = AVAILABLE;
        this.parkingSlots = createParkingSlots(numberSpots, numberOfSpotsPerSlot, parkingAttendant);
        this.owner = owner;
    }

    private ParkingSlot[] createParkingSlots(int numberSlots, int numberOfSpotsPerSlot, Attendant parkingAttendant) {
        ParkingSlot[] parkingSlots = new ParkingSlot[numberSlots];
        char name = 'A';
        for (int i = 0; i < parkingSlots.length; i++) {
            parkingSlots[i] = new ParkingSlot(name++, numberOfSpotsPerSlot, this, parkingAttendant);
        }
        return parkingSlots;
    }

    public boolean park(Vehicle vehicle, ParkingType type) {
        if (vehicle.parkingSpot != null)
            throw new RuntimeException("Vehicle parked already");
        if (this.status.equals(FILLED))
            throw new RuntimeException("no space available to park vehicle");
        ParkingSlot slot = getParkingSlot(type, vehicle.vehicleSize);
        return slot.park(vehicle, type);
    }

    public boolean unPark(Vehicle vehicle) {
        ParkingSpot[] spots = this.getParkingSpots(vehicle);
        if (spots == null || spots.length == 0)
            throw new RuntimeException("No car found");
        Arrays.stream(spots).forEach(ParkingSpot::unPark);
        return Arrays.stream(spots).allMatch(parkingSpot -> parkingSpot.vehicle == null);
    }

    private ParkingSlot getParkingSlot(ParkingType type, VehicleSize vehicleSize, boolean isReArranged) {
        List<ParkingSlot> availableParkingSlots = new ArrayList<>();
        for (int index = 0; index < this.parkingSlots.length; index++) {
            boolean status = this.parkingSlots[index].canPark(vehicleSize);
            int counter = vehicleSize.size;
            if (vehicleSize.size > SMALL.size && index != 0 && index != this.parkingSlots.length - 1) {
                counter = 0;
                for (int i = index, j = 0; j < vehicleSize.size; i++, j++) {
                    if (Arrays.stream(this.parkingSlots[index].parkingSpots).filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE)).count()
                            <= Arrays.stream(this.parkingSlots[i - 1].parkingSpots).filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE)).count()) {
                       counter++;
                    }
                }
            }
            if (status && counter >= vehicleSize.size)
                availableParkingSlots.add(this.parkingSlots[index]);
        }
        if (availableParkingSlots.size() == 0 ) {
            if (isReArranged) {
                throw new RuntimeException("space not available for " + vehicleSize + " vehicles");
            }
            getParkingSlot(type, vehicleSize, reArrangeVehicles(type, vehicleSize));
        }
        List<Integer> countOfVehicleParked = availableParkingSlots.stream()
                                                                .map(parkingSlot -> parkingSlot.emptySpots)
                                                                .collect(Collectors.toList());
        Optional<Integer> maximumFreeSpots = countOfVehicleParked.stream().reduce(Math::max);
        Integer index = maximumFreeSpots.map(countOfVehicleParked::indexOf).orElse(0);
        return availableParkingSlots.get(index);
    }
    private ParkingSlot getParkingSlot(ParkingType type, VehicleSize vehicleSize) {
        return getParkingSlot(type, vehicleSize, false);
    }

    private boolean reArrangeVehicles(ParkingType type, VehicleSize vehicleSize) {
        List<ParkingSpot> parkingSpotsWithSmallVehicles = new ArrayList<>();
        List<ParkingSpot> emptyParkingSpots = new ArrayList<>();
        List<ParkingSpot> smallVehiclesInFirstColumn = Arrays.stream(this.parkingSlots[0].parkingSpots)
                                                    .filter(parkingSpot -> parkingSpot.vehicle != null &&
                                                            parkingSpot.vehicle.vehicleSize.equals(SMALL))
                                                    .collect(Collectors.toList());
        List<ParkingSpot> smallVehiclesInLastColumn = Arrays.stream(this.parkingSlots[this.parkingSlots.length-1].parkingSpots)
                                                    .filter(parkingSpot -> parkingSpot.vehicle != null &&
                                                            parkingSpot.vehicle.vehicleSize.equals(SMALL))
                                                    .collect(Collectors.toList());
        if (smallVehiclesInFirstColumn.size()+this.parkingSlots[0].emptySpots >= vehicleSize.size) {
            parkingSpotsWithSmallVehicles = smallVehiclesInFirstColumn;
            emptyParkingSpots = Arrays.stream(this.parkingSlots[0].parkingSpots)
                                        .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                                        .collect(Collectors.toList());
        }
        else if (smallVehiclesInLastColumn.size()+this.parkingSlots[this.parkingSlots.length-1].emptySpots >= vehicleSize.size) {
            parkingSpotsWithSmallVehicles = smallVehiclesInLastColumn;
            emptyParkingSpots = Arrays.stream(this.parkingSlots[this.parkingSlots.length-1].parkingSpots)
                                        .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                                        .collect(Collectors.toList());
        }
        else {
            int minimumVehiclesInSlot = Arrays.stream(this.parkingSlots)
                                                .reduce((parkingSlot1, parkingSlot2) -> {
                                                    if (parkingSlot2.emptySpots > parkingSlot1.emptySpots)
                                                        return parkingSlot2;
                                                    return parkingSlot1;
                                                }).map(parkingSlot -> parkingSlot.emptySpots)
                                                    .orElse(0);
            if (minimumVehiclesInSlot + vehicleSize.size <= this.parkingSlots[0].parkingSpots.length) {
                List<ParkingSpot> parkingSpotsInLastRows = Arrays.stream(this.parkingSlots)
                                                                    .map(parkingSlot -> Arrays.stream(parkingSlot.parkingSpots)
                                                                                                .filter(parkingSpot -> parkingSpot.spotNumber > minimumVehiclesInSlot
                                                                                                        && (parkingSpot.status.equals(AVAILABLE) ||
                                                                                                            SMALL.equals(parkingSpot.vehicle.vehicleSize)))
                                                                    .collect(Collectors.toList()))
                                                                    .reduce((parkingSpots1, parkingSpots2) -> {
                                                                        parkingSpots1.addAll(parkingSpots2);
                                                                        return parkingSpots1;
                                                                    }).orElse(parkingSpotsWithSmallVehicles);
                if (parkingSpotsInLastRows.size() >= Math.pow(vehicleSize.size, 2)) {
                    parkingSpotsWithSmallVehicles = parkingSpotsInLastRows.stream()
                                                                            .filter(parkingSpot -> parkingSpot.vehicle != null &&
                                                                                    parkingSpot.vehicle.vehicleSize.equals(SMALL))
                                                                            .collect(Collectors.toList());
                    emptyParkingSpots = parkingSpotsInLastRows.stream()
                                                                .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                                                                .collect(Collectors.toList());
                } else {
                    throw new RuntimeException("space not available for " + vehicleSize + " vehicles");
                }
            }
        }
        List<ParkingSpot> emptyParkingSpotsInLot = Arrays.stream(this.parkingSlots)
                                                            .filter(parkingSlot -> !parkingSlot.equals(this.parkingSlots[0]) &&
                                                                    !parkingSlot.equals(this.parkingSlots[this.parkingSlots.length - 1]))
                                                            .map(parkingSlot -> Arrays.stream(parkingSlot.parkingSpots)
                                                                                        .filter(parkingSpot -> parkingSpot.status.equals(AVAILABLE))
                                                                                        .collect(Collectors.toList()))
                                                            .reduce((parkingSpots1, parkingSpots2) -> {
                                                                parkingSpots1.addAll(parkingSpots2);
                                                                return parkingSpots1;
                                                            }).orElse(new ArrayList<>());
        emptyParkingSpotsInLot.removeAll(emptyParkingSpots);
        int index = Math.min(parkingSpotsWithSmallVehicles.size(), emptyParkingSpotsInLot.size());
        for (int i = 0; i < index; i++) {
            ParkingSpot spot = parkingSpotsWithSmallVehicles.get(i);
            ParkingSpot spot1 = emptyParkingSpotsInLot.get(i);
            spot1.park(spot.vehicle, spot.parkingType);
            spot1.startTime = spot.startTime;
            spot.unPark();
        }
        for (ParkingSlot parkingSlot : this.parkingSlots) {
            for (int j = 0; j < parkingSlot.parkingSpots.length; j++) {
                ParkingSpot emptySpot = parkingSlot.parkingSpots[j];
                if (emptySpot.vehicle == null) {
                    for (int k = j + 1; k < parkingSlot.parkingSpots.length; k++) {
                        ParkingSpot parkingSpot = parkingSlot.parkingSpots[k];
                        if (parkingSpot.vehicle == null)
                            emptySpot = parkingSpot;
                        else {
                            parkingSlot.parkingSpots[j] = parkingSpot;
                            parkingSlot.parkingSpots[k] = emptySpot;
                        }
                    }
                }
            }
        }
        return true;
    }

    public ParkingSpot[] getParkingSpots(Vehicle vehicle) {
        return Arrays.stream(this.parkingSlots)
                    .map(parkingSlot -> Arrays.asList(parkingSlot.parkingSpots))
                    .reduce(new LinkedList<>(), (parkingSpots, parkingSpots2) -> {
                        parkingSpots.addAll(parkingSpots2);
                        return parkingSpots;
                    }).stream()
                      .filter(parkingSpot -> vehicle.equals(parkingSpot.vehicle))
                      .toArray(ParkingSpot[]::new);
    }

    void updateStatus() {
        if (this.status != (this.status = Arrays.stream(this.parkingSlots)
                                                .anyMatch(parkingSlot -> parkingSlot.status.equals(AVAILABLE))? AVAILABLE : FILLED ))
            this.owner.updateStatus(this.status);
    }

    public VehicleDetailsDTO[] getVehiclesBy(Object... options) {
        return Arrays.stream(this.parkingSlots)
                        .map(parkingSlot -> Arrays.stream(parkingSlot.parkingSpots)
                                                    .filter(parkingSpot -> parkingSpot.status.equals(FILLED))
                                                    .collect(Collectors.toList()))
                        .reduce(new ArrayList<>(), (parkingSpots, parkingSpots2) -> {
                            parkingSpots.addAll(parkingSpots2);
                            return parkingSpots;
                        }).stream()
                            .map(parkingSpot -> parkingSpot.vehicle)
                            .distinct()
                            .map(vehicle -> vehicle.parkingSpot)
                            .map(VehicleDetailsDTO::new)
                            .filter(vehicleDetailsDTO -> isOption(options, vehicleDetailsDTO))
                            .toArray(VehicleDetailsDTO[]::new);
    }

    private boolean isOption(Object[] objects, VehicleDetailsDTO detailsDTO) {
        if (objects.length == 0)
            return true;
        Field[] vehicleDetailsDtoFields = detailsDTO.getClass().getDeclaredFields();
        long requiredCount = Arrays.stream(objects).map(Object::getClass).distinct().count();
        long actualCount = 0;
        try {
            List<String> collect = Arrays.stream(objects)
                    .filter(o -> o.toString().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]?$"))
                    .map(Object::toString)
                    .collect(Collectors.toList());
            if (!collect.isEmpty() && requiredCount > 1)
                requiredCount += 1;
            for (Field field : vehicleDetailsDtoFields) {
                if (field.getType().equals(LocalTime.class)) {
                    LocalTime actualTime = (LocalTime) field.get(detailsDTO);
                    if (actualTime != null) {
                        for (String time : collect) {
                            LocalTime giveTime = LocalTime.parse(time);
                            if (Math.abs((giveTime.getHour() * 60 + giveTime.getMinute()) - (actualTime.getHour() * 60 + actualTime.getMinute())) <= 30)
                                actualCount++;
                        }
                    }
                }
                if (Arrays.asList(objects).contains(field.get(detailsDTO)))
                    actualCount++;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return actualCount == requiredCount;
    }

}