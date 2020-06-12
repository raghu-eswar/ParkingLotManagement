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
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.status = AVAILABLE;
        Services services = new Services(new Owner(parkingArea));
        Mockito.when(parkingArea.park(vehicle)).thenReturn(true);
        Assert.assertTrue(services.park(vehicle));
        Mockito.verify(parkingArea).park(vehicle);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.parkingArea = parkingArea;
        Services services = new Services(new Owner(parkingArea));
        Mockito.when(parkingArea.unPark(vehicle)).thenReturn(true);
        Assert.assertTrue(services.unPark(vehicle));
        Mockito.verify(parkingArea).unPark(vehicle);
    }

    @Test
    public void givenVehicleReference_afterParking_getParkingSpotShouldReturnCorrectSpot() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.parkingArea = parkingArea;
        ParkingSpot parkingSpot = Mockito.mock(ParkingSpot.class);
        parkingSpot.spotNumber = 1;
        Services services = new Services(new Owner(parkingArea));
        Mockito.when(parkingArea.getParkingSpot(vehicle)).thenReturn(parkingSpot);
        Assert.assertEquals(services.getParkingSpot(vehicle), 1);
        Mockito.verify(parkingArea).getParkingSpot(vehicle);
    }
}
