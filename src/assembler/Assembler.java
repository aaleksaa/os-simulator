package assembler;

import cpu.CPU;
import cpu.ProcessState;
import cpu.Register;
import cpu.Process;


public class Assembler {
    public static int accumulator = 0;

    private static int convertBinary(String binaryValue) {
        return Integer.parseInt(binaryValue, 2);
    }

    private static String toBinary(String input) {
        int value = Integer.parseInt(input);
        return String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public static String transformAssemblyToMachineCode(CPU cpu, String assemblyInstruction) {
        StringBuilder sb = new StringBuilder();
        String[] instructionParts = assemblyInstruction.split(" ");

        Instruction instruction = Instruction.fromString(instructionParts[0]);
        String instructionCode = instruction.getOperationCode();
        sb.append(instructionCode);

        if (instruction == Instruction.HALT)
            return sb.toString();
        else {
            String address = cpu.getRegisterAddress(instructionParts[1]);
            if (address == null)
               sb.append(toBinary(instructionParts[1]));
            else
                sb.append(address);
        }

        return sb.toString();
    }

    public static void halt(Process process) {
        process.setState(ProcessState.FINISHED);
    }

    public static void load(CPU cpu, String input) {
        if (input.length() == 8)
            accumulator = convertBinary(input);
        else
            accumulator = cpu.getRegisterByAddress(input).getValue();
    }

    public static void store(CPU cpu, String address) {
        Register reg = cpu.getRegisterByAddress(address);
        reg.setValue(accumulator);
    }


    public static void add(CPU cpu, String input) {
        if (input.length() == 8)
            accumulator += convertBinary(input);
        else
            accumulator += cpu.getRegisterByAddress(input).getValue();
    }

    public static void sub(CPU cpu, String input) {
        if (input.length() == 8)
            accumulator -= convertBinary(input);
        else
            accumulator -= cpu.getRegisterByAddress(input).getValue();
    }

    public static void mul(CPU cpu, String input) {
        if (input.length() == 8)
            accumulator *= convertBinary(input);
        else
            accumulator *= cpu.getRegisterByAddress(input).getValue();
    }

    public static void inc() {
        accumulator++;
    }

    public static void dec() {
        accumulator--;
    }
}
