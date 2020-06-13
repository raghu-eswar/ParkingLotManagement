package parkingSystem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import vehicles.Vehicle;

import static parkingService.ParkingType.HANDICAPPED;
import static parkingService.ParkingType.NORMAL;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingArea {

    @Mock
    Owner owner;

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstPlace() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 10, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.park(vehicle, NORMAL);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, vehicle);
    }

    @Test
    public void givenVehicleReference_afterParking_parkingAreaStatusShouldBeUpdated() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 1, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.park(vehicle, NORMAL);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, vehicle);
        Assert.assertEquals(parkingArea.status, FILLED);
    }

    @Test
    public void givenVehicleReference_afterUnParking_parkingAreaStatusShouldBeUpdated() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 1, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.park(vehicle, NORMAL);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, vehicle);
        parkingArea.parkingSpots[0].unPark();
        Assert.assertEquals(parkingArea.status, AVAILABLE);
    }

    @Test
    public void givenVehicleReference_afterParking_getParkingSpotShouldReturnCorrectSpot() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 10, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.park(vehicle, NORMAL);
        Assert.assertEquals(parkingArea.parkingSpots[0], parkingArea.getParkingSpot(vehicle));
    }

    @Test
    public void givenVehicleReferenceAndParkingType_thenPark_shouldParkVehicleAtLastPlace() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 10, owner);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.park(vehicle, HANDICAPPED);
        Assert.assertEquals(parkingArea.parkingSpots[9].vehicle, vehicle);
    }
}
