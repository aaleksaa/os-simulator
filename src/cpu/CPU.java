package cpu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CPU {
    private Register R1, R2, R3, R4;
    private List<Register> generalRegisters = new ArrayList<>();
    private Process currentProcess;

    public CPU() {
        R1 = new Register("R1", "0000");
        R2 = new Register("R2", "0001");
        R3 = new Register("R3", "0010");
        R4 = new Register("R4", "0011");

        Collections.addAll(generalRegisters, R1, R2, R3, R4);
    }

    public Register getRegisterByAddress(String address) {
        for (Register reg : generalRegisters)
            if (reg.getAddress().equals(address))
                return reg;
        return null;
    }

    public String getRegisterAddress(String name) {
        for (Register reg : generalRegisters)
            if (reg.getName().equals(name))
                return reg.getAddress();
        return null;
    }

    public void printRegisters() {
        for (Register reg : generalRegisters)
            System.out.println(reg);
    }

    public void clearRegisters() {
        for (Register reg : generalRegisters)
            reg.setValue(0);
    }
}
