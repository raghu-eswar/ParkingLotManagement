package parkingService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import parkingSystem.ParkingArea;
import vehicales.Car;

@RunWith(MockitoJUnitRunner.class)
public class TestServices {

    @Test
    public void givenVehicleReference_thenPark_shouldReturnTrue() {
        ParkingArea parkingArea = Mockito.mock(ParkingArea.class);
        parkingArea.status = true;
        Services services = new Services(parkingArea);
        Car car = new Car("AP 1234", "honda");
        Mockito.when(parkingArea.park(car)).thenReturn(true);
        Assert.assertTrue(services.park(car));
        Mockito.verify(parkingArea).park(car);
    }

}
