package parkingSystem;

import org.junit.Assert;
import org.junit.Test;
import vehicales.Car;

public class TestParkingSpot {

    @Test
    public void givenVehicleReference_thenPark_shouldParkVehicle() {
        ParkingSpot spot = new ParkingSpot(1,true);
        Car car = new Car("AP 1234", "honda");
        spot.park(car);
        Assert.assertEquals(spot.vehicle, car);
    }

    @Test
    public void givenVehicleReference_thenUnPark_shouldUnParkVehicle() {
        ParkingSpot spot = new ParkingSpot(1,true);
        Car car = new Car("AP 1234", "honda");
        spot.park(car);
        spot.unPark(car);
        Assert.assertNull(spot.vehicle);
    }
}
