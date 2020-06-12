package parkingService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import parkingSystem.Owner;
import parkingSystem.ParkingArea;
import parkingSystem.ParkingSpot;
import vehicles.Vehicle;

import static parkingSystem.Status.AVAILABLE;

@RunWith(MockitoJUnitRunner.class)
public class TestServices {

    @Mock
    ParkingArea parkingArea;

    @Test
    public void givenVehicleReference_thenPark_shouldReturnTrue() {
        Vehicle car = Mockito.mock(Vehicle.class);
        parkingArea.status = AVAILABLE;
        Services services = new Services(new Owner(parkingArea));
        Mockito.when(parkingArea.park(car)).thenReturn(true);
        Assert.assertTrue(services.park(car));
        Mockito.verify(parkingArea).park(car);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        Vehicle car = Mockito.mock(Vehicle.class);
        car.parkingArea = parkingArea;
        Services services = new Services(new Owner(parkingArea));
        Mockito.when(parkingArea.unPark(car)).thenReturn(true);
        Assert.assertTrue(services.unPark(car));
        Mockito.verify(parkingArea).unPark(car);
    }

    @Test
    public void givenVehicleReference_afterParking_getParkingSpotShouldReturnCorrectSpot() {
        Vehicle car = Mockito.mock(Vehicle.class);
        car.parkingArea = parkingArea;
        ParkingSpot parkingSpot = Mockito.mock(ParkingSpot.class);
        parkingSpot.spotNumber = 1;
        Services services = new Services(new Owner(parkingArea));
        Mockito.when(parkingArea.getParkingSpot(car)).thenReturn(parkingSpot);
        Assert.assertEquals(services.getParkingSpot(car), 1);
        Mockito.verify(parkingArea).getParkingSpot(car);
    }
}
