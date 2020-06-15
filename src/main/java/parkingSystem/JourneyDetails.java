package parkingSystem;

import vehicles.Vehicle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JourneyDetails {

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public String parkedDate;
    public String parkedTime;
    public String unParkedDate;
    public String unParkedTime;
    public Vehicle vehicle;
    public Attendant attendant;

    public JourneyDetails(LocalDateTime dateTime, Vehicle vehicle, Attendant attendant) {
        this.parkedDate = dateFormatter.format(dateTime);
        this.parkedTime = timeFormatter.format(dateTime);
        this.vehicle = vehicle;
        this.attendant = attendant;
    }

    public JourneyDetails addUnParkingDetails (LocalDateTime dateTime) {
        this.unParkedDate = dateFormatter.format(dateTime);
        this.unParkedTime = timeFormatter.format(dateTime);
        return this;
    }

}
