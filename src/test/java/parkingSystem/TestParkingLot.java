package parkingSystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import vehicles.Vehicle;

import java.time.LocalDateTime;

import static java.awt.Color.*;
import static parkingSystem.ParkingType.HANDICAPPED;
import static parkingSystem.ParkingType.NORMAL;
import static parkingSystem.ParkingSpot.timeFormatter;
import static parkingSystem.Status.AVAILABLE;
import static parkingSystem.Status.FILLED;
import static vehicles.Brand.*;
import static vehicles.VehicleSize.*;

@RunWith(MockitoJUnitRunner.class)
public class TestParkingLot {

    Attendant attendant;
    ParkingLot parkingLot;
    @Mock
    Owner owner;
    @Mock
    ParkingSlot parkingSlot;
    @Mock
    ParkingSpot parkingSpot;

    @Before
    public void setUp() {
        attendant = new Attendant("123A", "Agarwal");
        parkingLot = new ParkingLot("parking-1", owner, attendant, 5);
        parkingSlot.parkingSpots = new ParkingSpot[]{parkingSpot};
        parkingLot.parkingSlots = new ParkingSlot[]{parkingSlot};
    }

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstPlace() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingSlot.status = AVAILABLE;
        parkingSpot.status = AVAILABLE;
        vehicle.vehicleSize = SMALL;
        Mockito.when(parkingSlot.canPark(SMALL)).thenReturn(true);
        Mockito.when(parkingSlot.park(vehicle, NORMAL)).thenReturn(true);
        parkingLot.park(vehicle, NORMAL);
        Mockito.verify(parkingSlot).canPark(SMALL);
        Mockito.verify(parkingSlot).park(vehicle, NORMAL);
    }

    @Test
    public void givenVehicleReference_afterParking_parkingAreaStatusShouldBeUpdated() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingSlot.status = AVAILABLE;
        parkingSpot.status = AVAILABLE;
        vehicle.vehicleSize = SMALL;
        Mockito.when(parkingSlot.canPark(SMALL)).thenReturn(true);
        Mockito.when(parkingSlot.park(vehicle, NORMAL)).then(invocation -> {
            parkingSlot.status = FILLED;
            parkingLot.updateStatus();
            return true;
        });
        parkingLot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingLot.status, FILLED);
    }

    @Test
    public void givenVehicleReference_afterUnParking_parkingAreaStatusShouldBeUpdated() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingSlot.status = FILLED;
        parkingSpot.status = FILLED;
        parkingSpot.vehicle = vehicle;
        vehicle.parkingSpot = parkingSpot;
        Mockito.when(parkingSpot.unPark()).then(invocation -> {
            parkingSlot.status = AVAILABLE;
            parkingSpot.vehicle = null;
            parkingLot.updateStatus();
            return parkingSpot;
        });
        parkingLot.unPark(vehicle);
        Assert.assertEquals(parkingLot.status, AVAILABLE);
    }

    @Test
    public void givenVehicleReferenceAfterParking_getParkingSpot_ShouldReturnCorrectSpot() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        parkingSpot.vehicle = vehicle;
        vehicle.parkingSpot = parkingSpot;
        Assert.assertEquals(parkingSpot, parkingLot.getParkingSpots(vehicle)[0]);
    }

    @Test
    public void givenVehicleReferenceAndParkingType_thenPark_shouldParkVehicleAtLastPlace() {
        ParkingLot parkingLot = new ParkingLot("parking-1", owner, attendant, 10);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        parkingLot.park(vehicle, HANDICAPPED);
        Assert.assertEquals(parkingLot.parkingSlots[0].parkingSpots[9].vehicle, vehicle);
    }

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstTwoPlace() {
        ParkingLot parkingLot = new ParkingLot("parking-1", owner, attendant, 10);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = MEDIUM;
        parkingLot.park(vehicle, NORMAL);
        Assert.assertEquals(parkingLot.parkingSlots[0].parkingSpots[0].vehicle, vehicle);
        Assert.assertEquals(parkingLot.parkingSlots[0].parkingSpots[1].vehicle, vehicle);
    }

    @Test
    public void givenSameVehicleReferences_park_shouldThrowException() {
        ParkingLot parkingLot = new ParkingLot("parking-1", owner, attendant, 5);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        Assert.assertTrue(parkingLot.park(vehicle, NORMAL));
        try {
            parkingLot.park(vehicle, NORMAL);
        } catch (RuntimeException e) {
            Assert.assertEquals("Vehicle parked already", e.getMessage());
        }
    }

    @Test
    public void givenVehicleReferences_park_shouldDistributeFiveVehiclesEvenlyToEachParkingArea() {

        ParkingLot parkingLot = new ParkingLot("parking-1", owner, attendant, 5);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        vehicle.vehicleSize = SMALL;
        for (int i = 0; i < 5; i++) {
            Assert.assertTrue(parkingLot.park(vehicle, NORMAL));
            vehicle.parkingSpot = null;
        }
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(parkingLot.parkingSlots[i].parkingSpots[0].vehicle, vehicle);
        }
    }

    @Test
    public void afterParkingGivenVehicles_getVehiclesBy_shouldDistributeEvenly() {
        Vehicle vehicle1 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        Vehicle vehicle2 = new Vehicle(MEDIUM, BLACK, LAMBORGHINI);
        Vehicle vehicle3 = new Vehicle(LARGE, WHITE, LAMBORGHINI);
        Vehicle vehicle4 = new Vehicle(SMALL, RED, LAMBORGHINI);
        Vehicle vehicle5 = new Vehicle(MEDIUM, WHITE, LAMBORGHINI);
        Vehicle vehicle6 = new Vehicle(LARGE, WHITE, LAMBORGHINI);
        Vehicle vehicle7 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        Vehicle vehicle8 = new Vehicle(MEDIUM, WHITE, LAMBORGHINI);
        Vehicle vehicle9 = new Vehicle(MEDIUM, WHITE, LAMBORGHINI);
        Vehicle vehicle10 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        Vehicle vehicle11 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        Vehicle vehicle12 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        Vehicle vehicle13 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        Vehicle vehicle14 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        ParkingLot parkingLot = new ParkingLot("lot-1", owner, attendant, 5);
        Assert.assertTrue(parkingLot.park(vehicle1, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle2, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle3, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle4, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle5, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle6, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle7, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle8, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle9, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle10, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle11, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle12, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle13, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle14, NORMAL));
    }

    @Test
    public void afterParkingGivenVehicles_getVehiclesBy_shouldReturnArrayOfWhiteVehicles() {
        Vehicle vehicle1 = new Vehicle(SMALL, WHITE, LAMBORGHINI);
        Vehicle vehicle2 = new Vehicle(MEDIUM, BLACK, AUDI);
        Vehicle vehicle3 = new Vehicle(MEDIUM, WHITE, LAMBORGHINI);
        Vehicle vehicle4 = new Vehicle(SMALL, RED, AUDI);
        Vehicle vehicle5 = new Vehicle(LARGE, WHITE, LAMBORGHINI);
        ParkingLot parkingLot = new ParkingLot("lot-1", owner, attendant, 5);
        Assert.assertTrue(parkingLot.park(vehicle1, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle2, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle3, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle4, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle5, NORMAL));
        VehicleDetailsDTO[] journeyDetails = parkingLot.getVehiclesBy(WHITE);
        Assert.assertEquals(journeyDetails.length, 3);
        Assert.assertEquals(journeyDetails[0].vehicleColor, WHITE);
        Assert.assertEquals(journeyDetails[1].vehicleColor, WHITE);
        Assert.assertEquals(journeyDetails[2].vehicleColor, WHITE);
    }

    @Test
    public void afterParkingGivenVehicles_getVehiclesBy_shouldReturnArrayOfBlueToyotaVehicles() {
        Vehicle vehicle1 = new Vehicle(SMALL, WHITE, TOYOTA);
        Vehicle vehicle2 = new Vehicle(MEDIUM, BLUE, TOYOTA);
        Vehicle vehicle3 = new Vehicle(MEDIUM, BLUE, TOYOTA);
        Vehicle vehicle4 = new Vehicle(SMALL, BLUE, AUDI);
        Vehicle vehicle5 = new Vehicle(LARGE, BLUE, TOYOTA);
        ParkingLot parkingLot = new ParkingLot("lot-1", owner, attendant, 5);
        Assert.assertTrue(parkingLot.park(vehicle1, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle2, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle3, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle4, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle5, NORMAL));
        VehicleDetailsDTO[] journeyDetails = parkingLot.getVehiclesBy(BLUE, TOYOTA);
        Assert.assertEquals(journeyDetails.length, 3);
        Assert.assertEquals(journeyDetails[0].vehicleColor, BLUE);
        Assert.assertEquals(journeyDetails[1].vehicleColor, BLUE);
        Assert.assertEquals(journeyDetails[2].vehicleColor, BLUE);
    }

    @Test
    public void afterParkingGivenVehicles_getVehiclesBy_shouldReturnArrayVehiclesParkedBefore30Min() {
        Vehicle vehicle1 = new Vehicle(SMALL, WHITE, BMW);
        Vehicle vehicle2 = new Vehicle(MEDIUM, BLUE, AUDI);
        Vehicle vehicle3 = new Vehicle(MEDIUM, BLUE, BMW);
        Vehicle vehicle4 = new Vehicle(SMALL, RED, BMW);
        Vehicle vehicle5 = new Vehicle(LARGE, BLUE, TOYOTA);
        ParkingLot parkingLot = new ParkingLot("lot-1", owner, attendant, 5);
        Assert.assertTrue(parkingLot.park(vehicle1, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle2, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle3, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle4, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle5, NORMAL));
        VehicleDetailsDTO[] parkingSpots = parkingLot.getVehiclesBy( timeFormatter.format(LocalDateTime.now()));
        Assert.assertEquals(parkingSpots.length, 5);
    }

    @Test
    public void afterParkingGivenVehicles_getVehiclesBy_shouldReturnArrayVehiclesParkedInSlotsAandC() {
        Vehicle vehicle1 = new Vehicle(SMALL, WHITE, BMW);
        Vehicle vehicle2 = new Vehicle(SMALL, BLUE, AUDI);
        Vehicle vehicle3 = new Vehicle(SMALL, BLUE, BMW);
        Vehicle vehicle4 = new Vehicle(SMALL, RED, BMW);
        Vehicle vehicle5 = new Vehicle(SMALL, BLUE, TOYOTA);
        ParkingLot parkingLot = new ParkingLot("lot-1", owner, attendant, 3);
        Assert.assertTrue(parkingLot.park(vehicle1, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle2, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle3, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle4, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle5, NORMAL));
        VehicleDetailsDTO[] parkingSpots = parkingLot.getVehiclesBy('A', 'C');
        Assert.assertEquals(parkingSpots.length, 3);
    }

    @Test
    public void afterParkingGivenVehicles_getVehiclesBy_shouldReturnArrayVehiclesOfHandicappedDriverParkedInSlotsAandC() {
        Vehicle vehicle1 = new Vehicle(SMALL, WHITE, BMW);
        Vehicle vehicle2 = new Vehicle(SMALL, BLUE, AUDI);
        Vehicle vehicle3 = new Vehicle(SMALL, BLUE, BMW);
        Vehicle vehicle4 = new Vehicle(SMALL, RED, BMW);
        Vehicle vehicle5 = new Vehicle(SMALL, BLUE, TOYOTA);
        ParkingLot parkingLot = new ParkingLot("lot-1", owner, attendant, 3);
        Assert.assertTrue(parkingLot.park(vehicle1, HANDICAPPED));
        Assert.assertTrue(parkingLot.park(vehicle2, HANDICAPPED));
        Assert.assertTrue(parkingLot.park(vehicle3, HANDICAPPED));
        Assert.assertTrue(parkingLot.park(vehicle4, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle5, NORMAL));
        VehicleDetailsDTO[] parkingSpots = parkingLot.getVehiclesBy('A', 'C', HANDICAPPED);
        Assert.assertEquals(parkingSpots.length, 2);
    }

    @Test
    public void afterParkingGivenVehicles_getVehiclesBy_shouldReturnArrayOfBmwVehiclesParkedBefore30minByAgerwalInSlotsABAndC() {
        Vehicle vehicle1 = new Vehicle(SMALL, WHITE, BMW);
        Vehicle vehicle2 = new Vehicle(MEDIUM, BLUE, AUDI);
        Vehicle vehicle3 = new Vehicle(MEDIUM, BLUE, BMW);
        Vehicle vehicle4 = new Vehicle(SMALL, RED, BMW);
        Vehicle vehicle5 = new Vehicle(LARGE, BLUE, TOYOTA);
        ParkingLot parkingLot = new ParkingLot("lot-1", owner, attendant, 5);
        Assert.assertTrue(parkingLot.park(vehicle1, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle2, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle3, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle4, NORMAL));
        Assert.assertTrue(parkingLot.park(vehicle5, NORMAL));
        VehicleDetailsDTO[] parkingSpots = parkingLot.getVehiclesBy( BMW, "Agarwal", timeFormatter.format(LocalDateTime.now()), 'A', 'E', 'B');
        Assert.assertEquals(parkingSpots.length, 3);
        Assert.assertEquals(parkingSpots[0].vehicleBrand, BMW);
        Assert.assertEquals(parkingSpots[1].vehicleBrand, BMW);
        Assert.assertEquals(parkingSpots[1].vehicleBrand, BMW);
    }

}
