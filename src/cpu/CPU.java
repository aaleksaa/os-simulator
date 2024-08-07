package cpu;

import assembler.Assembler;
import assembler.Instruction;
import memory.RAM;

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
        return generalRegisters.stream()
                .filter(r -> r.getAddress().equals(address))
                .findFirst()
                .orElse(null);
    }

    public String getRegisterAddress(String name) {
        return generalRegisters.stream()
                .filter(reg -> reg.getName().equals(name))
                .map(Register::getAddress)
                .findFirst()
                .orElse(null);
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
        generalRegisters.forEach(r -> r.setValue(0));
        Assembler.accumulator = 0;
    }

    public String printRegisters() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-5s %-5s %-5s %-5s %-20s %-5s %-5s", "R1", "R2", "R3", "R4", "IR", "PC", "ACC")).append("\n");
        sb.append(String.format("%-5d %-5d %-5d %-5d %-20s %-5d %-5d", R1.getValue(), R2.getValue(), R3.getValue(), R4.getValue(), IR.getStrValue(), PC.getValue(), Assembler.accumulator));
        sb.append("\n");

        return sb.toString();
    }

    private void executeMachineCode() {
        String instruction = IR.getStrValue().substring(0, 4);
        System.out.println(printRegisters());

        if (instruction.equals(Instruction.HALT.getOperationCode()))
            Assembler.halt(currentProcess);
        else if (instruction.equals(Instruction.ADD.getOperationCode())) {
            Assembler.add(this, IR.getStrValue().substring(4));
        } else if (instruction.equals(Instruction.SUB.getOperationCode())) {
            Assembler.sub(this, IR.getStrValue().substring(4));
        } else if (instruction.equals(Instruction.MUL.getOperationCode())) {
            Assembler.mul(this, IR.getStrValue().substring(4));
        } else if (instruction.equals(Instruction.DEC.getOperationCode())) {
            Assembler.dec();
        } else if (instruction.equals(Instruction.LOAD.getOperationCode())) {
            Assembler.load(this, IR.getStrValue().substring(4));
        } else if (instruction.equals(Instruction.STORE.getOperationCode())) {
            Assembler.store(this, IR.getStrValue().substring(4));
        } else if (instruction.equals(Instruction.INC.getOperationCode())) {
            Assembler.inc();
        }

        PC.incrementValue(1);
        currentProcess.decrementRemainingTime();
    }

    public void execute(RAM ram) {
        while (currentProcess.isRunning()) {
            IR.setStrValue(currentProcess.getNextInstruction(PC.getValue()));
            executeMachineCode();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        clearRegisters();
        ram.remove(currentProcess);
    }
}
