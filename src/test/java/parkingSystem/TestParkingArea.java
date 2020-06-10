package parkingSystem;

import org.junit.Assert;
import org.junit.Test;
import vehicales.Car;

public class TestParkingArea {

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicleAtFirstPlace() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 10);
        Car car = new Car("AP 1234", "honda");
        parkingArea.park(car);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, car);
    }

    @Test
    public void givenVehicleReference_afterParking_parkingAreaStatusShouldBeUpdated() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 1);
        Car car = new Car("AP 1234", "honda");
        parkingArea.park(car);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, car);
        Assert.assertFalse(parkingArea.status);
    }

    @Test
    public void givenVehicleReference_afterUnParking_parkingAreaStatusShouldBeUpdated() {
        ParkingArea parkingArea = new ParkingArea("parking-1", 1);
        Car car = new Car("AP 1234", "honda");
        parkingArea.park(car);
        Assert.assertEquals(parkingArea.parkingSpots[0].vehicle, car);
        parkingArea.parkingSpots[0].unPark(car);
        Assert.assertTrue(parkingArea.status);
    }
}
