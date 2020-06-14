package parkingService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.MockitoJUnitRunner;
import parkingSystem.Owner;
import parkingSystem.ParkingLot;
import parkingSystem.ParkingSpot;
import vehicles.Vehicle;

import static parkingService.ParkingType.NORMAL;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingServices {

    @Mock
    ParkingLot parkingLot;

    @Test
    public void givenVehicleReference_thenPark_shouldReturnTrue() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingLot.status = AVAILABLE;
        parkingLot.parkingSpots = new ParkingSpot[]{new ParkingSpot(1, parkingLot)};
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingLot));
        Mockito.when(parkingLot.park(vehicle, NORMAL)).thenReturn(true);
        Assert.assertTrue(parkingServices.park(vehicle));
        Mockito.verify(parkingLot).park(vehicle, NORMAL);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.parkingLot = parkingLot;
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingLot));
        Mockito.when(parkingLot.unPark(vehicle)).thenReturn(true);
        Assert.assertTrue(parkingServices.unPark(vehicle));
        Mockito.verify(parkingLot).unPark(vehicle);
    }

    @Test
    public void givenVehicleReference_afterParking_getParkingSpotShouldReturnCorrectSpot() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.parkingLot = parkingLot;
        ParkingSpot parkingSpot = Mockito.mock(ParkingSpot.class);
        parkingSpot.spotNumber = 1;
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingLot));
        Mockito.when(parkingLot.getParkingSpot(vehicle)).thenReturn(new ParkingSpot[]{parkingSpot});
        Integer[] parkingSpots = parkingServices.getParkingSpot(vehicle);
        Assert.assertEquals(parkingSpots.length, 1);
        int spotNumber = parkingSpots[0];
        Assert.assertEquals(1, spotNumber);
        Mockito.verify(parkingLot).getParkingSpot(vehicle);
    }

    @Test
    public void givenVehicleReferences_park_shouldDistributeFiveVehiclesEvenlyToEachParkingArea() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingLot.status = AVAILABLE;
        parkingLot.parkingSpots = new ParkingSpot[]{new ParkingSpot(1, parkingLot), new ParkingSpot(2, parkingLot),
                                                     new ParkingSpot(3, parkingLot), new ParkingSpot(4, parkingLot),
                                                     new ParkingSpot(5, parkingLot),new ParkingSpot(6, parkingLot)};
        ParkingLot parkingLot1 = Mockito.mock(ParkingLot.class);
        parkingLot1.status = AVAILABLE;
        parkingLot1.parkingSpots = new ParkingSpot[]{new ParkingSpot(1, parkingLot)};
        ParkingServices parkingServices = new ParkingServices(new Owner(parkingLot, parkingLot1));
        final int[] counter = {1};
        Mockito.when(parkingLot.park(vehicle, NORMAL)).then(invocation -> {
            parkingLot.parkingSpots[counter[0]++].status = FILLED;
            return true;
        });
        Mockito.when(parkingLot1.park(vehicle, NORMAL)).thenReturn(true);
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Assert.assertTrue(parkingServices.park(vehicle));
        Mockito.verify(parkingLot, new Times(5)).park(vehicle, NORMAL);
        Mockito.verify(parkingLot1).park(vehicle, NORMAL);
    }

}
