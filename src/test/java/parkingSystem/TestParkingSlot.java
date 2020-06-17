package parkingSystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import vehicles.Vehicle;

import java.util.Arrays;

import static parkingSystem.ParkingType.HANDICAPPED;
import static parkingSystem.ParkingType.NORMAL;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;
import static vehicles.VehicleSize.*;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingSlot {
    Attendant attendant;
    ParkingSlot parkingSlot;
    @Mock
    ParkingLot parkingLot;
    @Mock
    ParkingSpot parkingSpot1;
    @Mock
    ParkingSpot parkingSpot2;
    @Mock
    ParkingSpot parkingSpot3;
    @Mock
    ParkingSpot parkingSpot4;
    @Mock
    ParkingSpot parkingSpot5;

    @Before
    public void setUp() {
        attendant = new Attendant("123A", "Agarwal");
        parkingSlot = new ParkingSlot('A', 5, parkingLot, attendant);
        parkingSlot.parkingSpots = new ParkingSpot[]{parkingSpot1, parkingSpot2, parkingSpot3, parkingSpot4, parkingSpot5};
        Arrays.stream(parkingSlot.parkingSpots).forEach(parkingSpot -> parkingSpot.status = AVAILABLE);
    }

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstPlace() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        Mockito.when(parkingSpot1.park(vehicle, NORMAL)).then(invocation -> {
            parkingSpot1.vehicle = vehicle;
            parkingSpot1.status = FILLED;
            return parkingSpot1;
        });
        Assert.assertTrue(parkingSlot.park(vehicle, NORMAL));
        Mockito.verify(parkingSpot1).park(vehicle, NORMAL);
    }

    @Test
    public void givenVehicleReference_afterParking_parkingSpotStatusShouldBeUpdated() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        parkingSlot.parkingSpots = new ParkingSpot[]{parkingSpot1};
        Mockito.when(parkingSpot1.park(vehicle, NORMAL)).then(invocation -> {
            parkingSpot1.vehicle = vehicle;
            parkingSpot1.status = FILLED;
            parkingSlot.updateStatus();
            return parkingSpot1;
        });
        parkingSlot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingSlot.status, FILLED);
        Mockito.verify(parkingSpot1).park(vehicle, NORMAL);
    }

    @Test
    public void givenVehicleReferenceAndParkingType_thenPark_shouldParkVehicleAtLastPlace() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        Mockito.when(parkingSpot5.park(vehicle, HANDICAPPED)).then(invocation -> parkingSpot5);
        parkingSlot.park(vehicle, HANDICAPPED);
        Mockito.verify(parkingSpot5).park(vehicle, HANDICAPPED);
    }

    @Test
    public void givenMediumVehicleReference_thenPark_shouldParkVehicleAtFirstTwoPlace() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = MEDIUM;
        Mockito.when(parkingSpot1.park(vehicle, NORMAL)).then(invocation -> parkingSpot1);
        Mockito.when(parkingSpot2.park(vehicle, NORMAL)).then(invocation -> parkingSpot2);
        parkingSlot.park(vehicle, NORMAL);
        Mockito.verify(parkingSpot1).park(vehicle, NORMAL);
        Mockito.verify(parkingSpot2).park(vehicle, NORMAL);
    }

    @Test
    public void givenLargeVehicleReference_thenPark_shouldParkVehicleAtFirstTwoPlace() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = LARGE;
        Mockito.when(parkingSpot1.park(vehicle, NORMAL)).then(invocation -> parkingSpot1);
        Mockito.when(parkingSpot2.park(vehicle, NORMAL)).then(invocation -> parkingSpot2);
        Mockito.when(parkingSpot3.park(vehicle, NORMAL)).then(invocation -> parkingSpot3);
        parkingSlot.park(vehicle, NORMAL);
        Mockito.verify(parkingSpot1).park(vehicle, NORMAL);
        Mockito.verify(parkingSpot2).park(vehicle, NORMAL);
        Mockito.verify(parkingSpot3).park(vehicle, NORMAL);
    }

    @Test
    public void givenLargeVehicleReference_thenCanPark_shouldReturnTrue() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = LARGE;
        Assert.assertTrue(parkingSlot.canPark(LARGE));
    }

    @Test
    public void givenLargeVehicleReference_thenCanPark_shouldReturnFalse() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingSpot1.status = FILLED;
        parkingSpot2.status = FILLED;
        parkingSpot3.status = FILLED;
        vehicle.vehicleSize = LARGE;
        Assert.assertFalse(parkingSlot.canPark(LARGE));
    }

}
