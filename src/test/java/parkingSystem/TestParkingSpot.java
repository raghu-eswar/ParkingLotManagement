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
        Vehicle car = Mockito.mock(Vehicle.class);
        spot.park(car);
        Assert.assertEquals(spot.vehicle, car);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        ParkingSpot spot = new ParkingSpot(1,true, parkingArea);
        Vehicle car = Mockito.mock(Vehicle.class);
        spot.park(car);
        spot.unPark(car);
        Assert.assertNull(spot.vehicle);
    }
}
