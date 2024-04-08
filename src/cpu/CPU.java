package cpu;

import assembler.Assembler;
import assembler.Instruction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CPU {
    private Register R1, R2, R3, R4;
    private List<Register> generalRegisters = new ArrayList<>();
    private String instructionRegister;
    private int programCounter;
    private Process currentProcess = null;

    public CPU() {
        R1 = new Register("R1", "0000");
        R2 = new Register("R2", "0001");
        R3 = new Register("R3", "0010");
        R4 = new Register("R4", "0011");

        Collections.addAll(generalRegisters, R1, R2, R3, R4);
    }

    public void execute(Process process) {
        currentProcess = process;
        System.out.println("Process " + process.getName() + " started execution!");
        while (process.isRunning()) {
            instructionRegister = process.getCode().get(programCounter);
            System.out.println(programCounter);
            System.out.println(instructionRegister);
            executeMachineCode(instructionRegister);
        }
        programCounter = 0;
        clearRegisters();
    }

    public void executeMachineCode(String input) {
        String instruction = input.substring(0, 4);
        System.out.println(printRegisters());

        if (instruction.equals(Instruction.HALT.getOperationCode()))
            Assembler.halt(currentProcess);
        else if (instruction.equals(Instruction.ADD.getOperationCode())) {
            String s1 = input.substring(4, 8);
            String s2 = input.length() == 12 ? input.substring(8, 12) : input.substring(8, 16);
            Assembler.add(this, s1, s2);
        } else if (instruction.equals(Instruction.SUB.getOperationCode())) {
            String s1 = input.substring(4, 8);
            String s2 = input.length() == 12 ? input.substring(8, 12) : input.substring(8, 16);
            Assembler.sub(this, s1, s2);
        } else if (instruction.equals(Instruction.MUL.getOperationCode())) {
            String s1 = input.substring(4, 8);
            String s2 = input.length() == 12 ? input.substring(8, 12) : input.substring(8, 16);
            Assembler.mul(this, s1, s2);
        } else if (instruction.equals(Instruction.DEC.getOperationCode())) {
            String s1 = input.substring(4, 8);
            Assembler.dec(this, s1);
        } else if (instruction.equals(Instruction.INC.getOperationCode())) {
            String s1 = input.substring(4, 8);
            Assembler.inc(this, s1);
        }

        programCounter++;
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

    public String printRegisters() {
        StringBuilder sb = new StringBuilder();

        for (Register reg : generalRegisters)
            sb.append(reg).append("\n");

        return sb.toString();
    }

    public void clearRegisters() {
        for (Register reg : generalRegisters)
            reg.setValue(0);
    }
}
