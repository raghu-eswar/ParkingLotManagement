package parkingSystem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import vehicles.Vehicle;

import static parkingSystem.Status.*;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingArea {

    @Mock
    Owner owner;

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstPlace() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 10, owner);
        Vehicle car = Mockito.mock(Vehicle.class);
        parkingArea.park(car);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, car);
    }

    @Test
    public void givenVehicleReference_afterParking_parkingAreaStatusShouldBeUpdated() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 1, owner);
        Vehicle car = Mockito.mock(Vehicle.class);
        parkingArea.park(car);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, car);
        Assert.assertEquals(parkingArea.status, FILLED);
    }

    @Test
    public void givenVehicleReference_afterUnParking_parkingAreaStatusShouldBeUpdated() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 1, owner);
        Vehicle car = Mockito.mock(Vehicle.class);
        parkingArea.park(car);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, car);
        parkingArea.parkingSpots[0].unPark(car);
        Assert.assertEquals(parkingArea.status, AVAILABLE);
    }

    @Test
    public void givenVehicleReference_afterParking_getParkingSpotShouldReturnCorrectSpot() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 10, owner);
        Vehicle car = Mockito.mock(Vehicle.class);
        parkingArea.park(car);
        Assert.assertEquals(parkingArea.parkingSpots[0], parkingArea.getParkingSpot(car));
    }
}
