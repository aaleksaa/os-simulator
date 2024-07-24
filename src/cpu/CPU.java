package cpu;

import assembler.Assembler;
import assembler.Instruction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CPU {
    private Register R1, R2, R3, R4, PC, IR;
    private List<Register> generalRegisters;
    private Process currentProcess;

    public CPU() {
        this.generalRegisters = new ArrayList<>();
        R1 = new Register("R1", "0000");
        R2 = new Register("R2", "0001");
        R3 = new Register("R3", "0010");
        R4 = new Register("R4", "0011");

        PC = new Register("PC", "");
        IR = new Register("IR", "");

        Collections.addAll(generalRegisters, R1, R2, R3, R4);
    }

    public Register getIR() {
        return IR;
    }

    public Register getPC() {
        return PC;
    }

    public List<Register> getGeneralRegisters() {
        return generalRegisters;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
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

    public void saveValuesOfRegisters() {
        int[] values = {R1.getValue(), R2.getValue(), R3.getValue(), R4.getValue()};
        currentProcess.setValueOfRegisters(values);
        currentProcess.setProgramCounter(PC.getValue());
    }

    public void loadValuesOfRegisters() {
        int[] registers = currentProcess.getValueOfRegisters();
        for (int i = 0; i < registers.length; i++)
            generalRegisters.get(i).setValue(registers[i]);
        PC.setValue(currentProcess.getProgramCounter());
    }

    public void clearRegisters() {
        for (Register reg : generalRegisters)
            reg.setValue(0);
    }

    public String printRegisters() {
        StringBuilder sb = new StringBuilder();

        sb.append("IR [").append(IR.getStrValue()).append("]\n");
        sb.append(PC).append("\n");

        for (Register reg : generalRegisters)
            sb.append(reg).append("\n");

        return sb.toString();
    }

    private void executeMachineCode() {
        String instruction = IR.getStrValue().substring(0, 4);
        System.out.println(printRegisters());

        if (instruction.equals(Instruction.HALT.getOperationCode()))
            Assembler.halt(currentProcess);
        else if (instruction.equals(Instruction.ADD.getOperationCode())) {
            String s1 = IR.getStrValue().substring(4, 8);
            String s2 = IR.getStrValue().length() == 12 ? IR.getStrValue().substring(8, 12) : IR.getStrValue().substring(8, 16);
            Assembler.add(this, s1, s2);
        } else if (instruction.equals(Instruction.SUB.getOperationCode())) {
            String s1 = IR.getStrValue().substring(4, 8);
            String s2 = IR.getStrValue().length() == 12 ? IR.getStrValue().substring(8, 12) : IR.getStrValue().substring(8, 16);
            Assembler.sub(this, s1, s2);
        } else if (instruction.equals(Instruction.MUL.getOperationCode())) {
            String s1 = IR.getStrValue().substring(4, 8);
            String s2 = IR.getStrValue().length() == 12 ? IR.getStrValue().substring(8, 12) : IR.getStrValue().substring(8, 16);
            Assembler.mul(this, s1, s2);
        } else if (instruction.equals(Instruction.DEC.getOperationCode())) {
            String s1 = IR.getStrValue().substring(4, 8);
            Assembler.dec(this, s1);
        } else if (instruction.equals(Instruction.INC.getOperationCode())) {
            String s1 = IR.getStrValue().substring(4, 8);
            Assembler.inc(this, s1);
        }

        PC.incrementValue(1);
    }

    public void execute() {
        while (currentProcess.isRunning()) {
            IR.setStrValue(currentProcess.getCode().get(PC.getValue()));
            executeMachineCode();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        clearRegisters();
    }
}
