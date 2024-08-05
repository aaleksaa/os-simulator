package cpu;

public class Register {
    private String name;
    private String address;
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

    public void decrementValue(int value) {
        this.value -= value;
    }

    public void multipleValue(int value) {
        this.value *= value;
    }

    public String getPartValue(int a, int b) {
        return strValue.substring(a, b);
    }

    public String splitInput() {
        return strValue.length() == 12 ? getPartValue(8, 12) : getPartValue(8, 16);
    }

    @Override
    public String toString() {
        return name + " [" + value + "]";
    }
}
