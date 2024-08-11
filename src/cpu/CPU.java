package cpu;

import assembler.Assembler;
import file_system.FileSystem;
import memory.RAM;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CPU {
    private Register R1, R2, R3, R4, PC, IR;
    private List<Register> generalRegisters;
    private static final int TIME_QUANTUM = 2;
    private FileSystem fileSystem;

    public CPU(FileSystem fileSystem) {
        this.generalRegisters = new ArrayList<>();
        this.fileSystem = fileSystem;

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

    public void saveValuesOfRegisters(Process process) {
        int[] values = {R1.getValue(), R2.getValue(), R3.getValue(), R4.getValue(), Assembler.accumulator};
        process.setValueOfRegisters(values);
        process.setProgramCounter(PC.getValue());
    }

    public void loadValuesOfRegisters(Process process) {
        int[] registers = process.getValueOfRegisters();

        for (int i = 0; i < registers.length - 1; i++)
            generalRegisters.get(i).setValue(registers[i]);

        PC.setValue(process.getProgramCounter());
        Assembler.accumulator = registers[registers.length - 1];
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

    private void executeMachineCode(Process process) {
        String instruction = IR.getStrValue().substring(0, 4);

        switch (instruction) {
            case "0000":
                Assembler.halt(process);
                break;
            case "0001":
                Assembler.load(this, IR.getStrValue().substring(4));
                break;
            case "0010":
                Assembler.store(this, IR.getStrValue().substring(4));
                break;
            case "0011":
                Assembler.add(this, IR.getStrValue().substring(4));
                break;
            case "0100":
                Assembler.sub(this, IR.getStrValue().substring(4));
                break;
            case "0101":
                Assembler.mul(this, IR.getStrValue().substring(4));
                break;
            case "0110":
                Assembler.inc();
                break;
            case "0111":
                Assembler.dec();
                break;
        }

        System.out.println(printRegisters());
        process.decrementRemainingTime();
        PC.incrementValue(1);
    }

    public void execute(RAM ram, Process process) {
        if (process.getProgramCounter() != -1)
            loadValuesOfRegisters(process);
        else
            clearRegisters();

        List<String> pageTable = process.getPageTable();


        for (int i = 0; i < TIME_QUANTUM; i++) {
            if (process.checkState(ProcessState.FINISHED))
                break;

            int frameIndex = PC.getValue() / 2;
            int index = PC.getValue() % 2;
            String instruction = ram.getInstruction(pageTable.get(frameIndex), index);

            IR.setStrValue(instruction);
            executeMachineCode(process);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (!process.checkState(ProcessState.FINISHED))
            saveValuesOfRegisters(process);
        else
            fileSystem.addFileToResultsDir(process.getName(), printResults(process.getName()));
    }

    private String printResults(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("Results of ").append(name).append(".asm").append("\n");

        for (Register reg : generalRegisters)
            sb.append(reg).append("\n");

        return sb.toString();
    }
}
