package assembler;

import cpu.CPU;
import cpu.ProcessState;
import cpu.Register;
import cpu.Process;

import java.util.ArrayList;
import java.util.List;

public class Assembler {
    private static int convertBinary(String binaryValue) {
        return Integer.parseInt(binaryValue, 2);
    }

    private static String toBinary(String input) {
        int value = Integer.parseInt(input);
        return String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public static String transformAssemblyToMachineCode(CPU cpu, String assemblyInstruction) {
        StringBuilder sb = new StringBuilder();
        String[] instructionParts = assemblyInstruction.split("[ |,]");

        Instruction instruction = Instruction.fromString(instructionParts[0]);
        String instructionCode = instruction.getOperationCode();
        sb.append(instructionCode);

        if (instruction == Instruction.HALT)
            return sb.toString();
        else if (instruction == Instruction.INC || instruction == Instruction.DEC)
            sb.append(cpu.getRegisterAddress(instructionParts[1]));
        else if (cpu.getRegisterAddress(instructionParts[2]) != null)
            sb.append(cpu.getRegisterAddress(instructionParts[1])).append(cpu.getRegisterAddress(instructionParts[2]));
        else
            sb.append(cpu.getRegisterAddress(instructionParts[1])).append(toBinary(instructionParts[2]));

        return sb.toString();
    }

    public static void halt(Process process) {
        process.setState(ProcessState.FINISHED);
    }

    public static void mov(CPU cpu, String address1, String address2) {
        Register reg1 = cpu.getRegisterByAddress(address1);
        Register reg2 = cpu.getRegisterByAddress(address2);
        reg1.setValue(reg2.getValue());
    }

    public static void add(CPU cpu, String address, String input) {
        Register reg = cpu.getRegisterByAddress(address);

        if (input.length() == 8)
            reg.incrementValue(convertBinary(input));
        else
            reg.incrementValue(cpu.getRegisterByAddress(input).getValue());
    }

    public static void sub(CPU cpu, String address, String input) {
        Register reg = cpu.getRegisterByAddress(address);

        if (input.length() == 8)
            reg.decrementValue(convertBinary(input));
        else
            reg.decrementValue(cpu.getRegisterByAddress(input).getValue());
    }

    public static void mul(CPU cpu, String address, String input) {
        Register reg = cpu.getRegisterByAddress(address);

        if (input.length() == 8)
            reg.multipleValue(convertBinary(input));
        else
            reg.multipleValue(cpu.getRegisterByAddress(input).getValue());
    }

    public static void inc(CPU cpu, String address) {
        Register reg = cpu.getRegisterByAddress(address);
        reg.incrementValue(1);
    }

    public static void dec(CPU cpu, String address) {
        Register reg = cpu.getRegisterByAddress(address);
        reg.decrementValue(1);
    }

    public static void main(String[] args) {
//        ADD R1,10
//        ADD R2,5
//        MUL R1,R2
//        ADD R3,R1
//        SUB R3,R2
//        INC R1
//        INC R2
//        INC R3
//        HALT
        List<String> test = new ArrayList<>();
        test.add("ADD R1,10");
        test.add("ADD R2,5");
        test.add("MUL R1,R2");
        test.add("ADD R3,R1");
        test.add("SUB R3,R2");
        test.add("INC R1");
        test.add("INC R2");
        test.add("INC R3");
        test.add("HALT");

        CPU cpu = new CPU();

        for (String s : test) {
            String ins = Assembler.transformAssemblyToMachineCode(cpu, s);
            cpu.executeMachineCode(ins);
        }

    }
}
