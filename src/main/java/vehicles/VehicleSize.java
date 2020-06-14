package vehicles;

public enum VehicleSize {
    SMALL(1), MEDIUM(2), LARGE(3);

    public int size;
    VehicleSize(int size) {
        this.size = size;
    }
}
