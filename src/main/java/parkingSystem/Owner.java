package parkingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Owner {

    public List<ParkingArea> parkingAreasAvailable = new ArrayList<>();
    public List<ParkingArea> parkingAreasFilled = new ArrayList<>();
    public List<ParkingArea> parkingAreasUnderMaintenance = new ArrayList<>();

    public Owner(ParkingArea parkingArea, ParkingArea ... parkingAreas) {
        this.parkingAreasAvailable.add(parkingArea);
        this.parkingAreasAvailable.addAll(Arrays.asList(parkingAreas));
    }

    public void updateStatus(ParkingArea parkingArea, Status status) {
        switch (status) {
            case FILLED:
                parkingAreasAvailable.remove(parkingArea);
                parkingAreasFilled.add(parkingArea);
                break;
            case AVAILABLE:
                parkingAreasFilled.remove(parkingArea);
                parkingAreasAvailable.add(parkingArea);
                break;
        }
    }

}
