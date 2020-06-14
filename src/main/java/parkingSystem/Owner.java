package parkingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Owner {

    public List<ParkingLot> parkingAreasAvailable = new ArrayList<>();
    public List<ParkingLot> parkingAreasFilled = new ArrayList<>();
    public List<ParkingLot> parkingAreasUnderMaintenance = new ArrayList<>();

    public Owner() {    }

    public Owner(ParkingLot parkingLot, ParkingLot... parkingLots) {
        addParkingLots(parkingLot, parkingLots);
    }

    public void addParkingLots(ParkingLot parkingLot, ParkingLot... parkingLots) {
        this.parkingAreasAvailable.add(parkingLot);
        this.parkingAreasAvailable.addAll(Arrays.asList(parkingLots));
    }

    public void updateStatus(ParkingLot parkingLot, Status status) {
        switch (status) {
            case FILLED:
                parkingAreasAvailable.remove(parkingLot);
                parkingAreasFilled.add(parkingLot);
                break;
            case AVAILABLE:
                parkingAreasFilled.remove(parkingLot);
                parkingAreasAvailable.add(parkingLot);
                break;
        }
    }

}
