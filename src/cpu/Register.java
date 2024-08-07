package cpu;

public class Register {
    private final String name;
    private final String address;
    private String strValue;
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

    public String getStrValue() {
        return strValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public void incrementValue(int value) {
        this.value += value;
    }

    @Override
    public String toString() {
        return name + " [" + value + "]";
    }
}
