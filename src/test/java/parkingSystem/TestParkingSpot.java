package parkingSystem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import vehicles.Vehicle;

import static parkingSystem.ParkingType.NORMAL;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingSpot {

    @Mock
    ParkingSlot parkingSlot;

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicle() {
        ParkingSpot spot = new ParkingSpot(1, parkingSlot);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        spot.park(vehicle, NORMAL);
        Assert.assertEquals(spot.vehicle, vehicle);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        ParkingSpot spot = new ParkingSpot(1, parkingSlot);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        spot.park(vehicle, NORMAL);
        spot.unPark();
        Assert.assertNull(spot.vehicle);
    }
}
