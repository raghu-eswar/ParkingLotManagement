package parkingSystem;

import vehicles.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Attendant {
    public String id;
    public String name;

    public Attendant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public JourneyDetails upDateLogBook(List<JourneyDetails> logBook, Vehicle vehicle) {
        Optional<JourneyDetails> details = logBook.stream()
                                                    .filter(journeyDetails -> journeyDetails.vehicle.equals(vehicle))
                                                    .filter(journeyDetails -> journeyDetails.unParkedTime == null)
                                                    .findFirst();
        if (details.isPresent())
            return details.get().addUnParkingDetails(LocalDateTime.now());
        JourneyDetails newJourneyDetails = new JourneyDetails(LocalDateTime.now(), vehicle, this);
        logBook.add(newJourneyDetails);
        return newJourneyDetails;
    }

}
