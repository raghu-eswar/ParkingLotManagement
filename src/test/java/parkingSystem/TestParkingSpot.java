package parkingSystem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import vehicles.Vehicle;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingSpot {

    @Mock
    ParkingArea parkingArea;

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicle() {
        ParkingSpot spot = new ParkingSpot(1,true,parkingArea);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        spot.park(vehicle);
        Assert.assertEquals(spot.vehicle, vehicle);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        ParkingSpot spot = new ParkingSpot(1,true, parkingArea);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        spot.park(vehicle);
        spot.unPark(vehicle);
        Assert.assertNull(spot.vehicle);
    }
}
