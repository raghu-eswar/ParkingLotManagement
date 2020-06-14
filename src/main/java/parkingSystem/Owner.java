package parkingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Owner {

    public List<ParkingLot> parkingLotsAvailable = new ArrayList<>();
    public List<ParkingLot> parkingLotsFilled = new ArrayList<>();
    public List<ParkingLot> parkingLotsUnderMaintenance = new ArrayList<>();

    public Owner() {    }

    public Owner(ParkingLot parkingLot, ParkingLot... parkingLots) {
        addParkingLots(parkingLot, parkingLots);
    }

    public void addParkingLots(ParkingLot parkingLot, ParkingLot... parkingLots) {
        this.parkingLotsAvailable.add(parkingLot);
        this.parkingLotsAvailable.addAll(Arrays.asList(parkingLots));
    }

    public void updateStatus(ParkingLot parkingLot, Status status) {
        switch (status) {
            case FILLED:
                parkingLotsAvailable.remove(parkingLot);
                parkingLotsFilled.add(parkingLot);
                break;
            case AVAILABLE:
                parkingLotsFilled.remove(parkingLot);
                parkingLotsAvailable.add(parkingLot);
                break;
        }
    }

}
