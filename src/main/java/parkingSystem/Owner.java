package parkingSystem;

import static parkingSystem.Status.AVAILABLE;

public class Owner {
    Status lotStatus = AVAILABLE;
    public boolean updateStatus(Status status) {  return !lotStatus.equals(status);  }

}
