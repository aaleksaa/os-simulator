package cpu;

public class Register {
    private String name;
    private String address;
    private int value;

    public Register(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementValue(int value) {
        this.value += value;
    }

    public void decrementValue(int value) {
        this.value -= value;
    }

    public void multipleValue(int value) {
        this.value *= value;
    }

    @Override
    public String toString() {
        return name + " [" + value + "]";
    }
}
