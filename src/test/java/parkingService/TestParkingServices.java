package parkingService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.MockitoJUnitRunner;
import parkingSystem.Owner;
import parkingSystem.ParkingArea;
import parkingSystem.ParkingSpot;
import vehicles.Vehicle;

import static parkingService.ParkingType.NORMAL;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingServices {

    @Mock
    ParkingArea parkingArea;

    @Test
    public void givenVehicleReference_thenPark_shouldReturnTrue() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.status = AVAILABLE;
        parkingArea.parkingSpots = new ParkingSpot[]{new ParkingSpot(1, parkingArea)};
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingArea));
        Mockito.when(parkingArea.park(vehicle, NORMAL)).thenReturn(true);
        Assert.assertTrue(parkingServices.park(vehicle));
        Mockito.verify(parkingArea).park(vehicle, NORMAL);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.parkingArea = parkingArea;
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingArea));
        Mockito.when(parkingArea.unPark(vehicle)).thenReturn(true);
        Assert.assertTrue(parkingServices.unPark(vehicle));
        Mockito.verify(parkingArea).unPark(vehicle);
    }

    @Test
    public void givenVehicleReference_afterParking_getParkingSpotShouldReturnCorrectSpot() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.parkingArea = parkingArea;
        ParkingSpot parkingSpot = Mockito.mock(ParkingSpot.class);
        parkingSpot.spotNumber = 1;
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingArea));
        Mockito.when(parkingArea.getParkingSpot(vehicle)).thenReturn(parkingSpot);
        Assert.assertEquals(parkingServices.getParkingSpot(vehicle), 1);
        Mockito.verify(parkingArea).getParkingSpot(vehicle);
    }

    @Test
    public void givenVehicleReferences_park_shouldDistributeFiveVehiclesEvenlyToEachParkingArea() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingArea.status = AVAILABLE;
        parkingArea.parkingSpots = new ParkingSpot[]{new ParkingSpot(1, parkingArea), new ParkingSpot(2, parkingArea),
                                                     new ParkingSpot(3, parkingArea), new ParkingSpot(4, parkingArea),
                                                     new ParkingSpot(5, parkingArea),new ParkingSpot(6, parkingArea)};
        ParkingArea parkingArea1 = Mockito.mock(ParkingArea.class);
        parkingArea1.status = AVAILABLE;
        parkingArea1.parkingSpots = new ParkingSpot[]{new ParkingSpot(1, parkingArea)};
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingArea, parkingArea1));
        final int[] counter = {1};
        Mockito.when(parkingArea.park(vehicle, NORMAL)).then(invocation -> {
            parkingArea.parkingSpots[counter[0]++].status = FILLED;
            return true;
        });
        Mockito.when(parkingArea1.park(vehicle, NORMAL)).thenReturn(true);
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Mockito.verify(parkingArea, new Times(5)).park(vehicle, NORMAL);
        Mockito.verify(parkingArea1).park(vehicle, NORMAL);
    }

}
