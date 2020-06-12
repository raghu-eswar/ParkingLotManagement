package parkingService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import parkingSystem.ParkingArea;
import vehicles.Vehicle;

@RunWith(MockitoJUnitRunner.class)
public class TestServices {

    @Mock
    ParkingArea parkingArea;

    @Test
    public void givenVehicleReference_thenPark_shouldReturnTrue() {
        Vehicle car = Mockito.mock(Vehicle.class);
        parkingArea.status = true;
        Services services = new Services(parkingArea,parkingArea,parkingArea);
        Mockito.when(parkingArea.park(car)).thenReturn(true);
        Assert.assertTrue(services.park(car));
        Mockito.verify(parkingArea).park(car);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        Vehicle car = Mockito.mock(Vehicle.class);
        car.parkingArea = parkingArea;
        Services services = new Services(parkingArea);
        Mockito.when(parkingArea.unPark(car)).thenReturn(true);
        Assert.assertTrue(services.unPark(car));
        Mockito.verify(parkingArea).unPark(car);
    }
}
