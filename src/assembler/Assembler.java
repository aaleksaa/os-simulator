package assembler;

import cpu.CPU;
import cpu.ProcessState;
import cpu.Register;
import cpu.Process;

import java.util.*;


public class Assembler {
    public static int accumulator = 0;

    private static int convertBinary(String binaryValue) {
        return Integer.parseInt(binaryValue, 2);
    }

    private static String toBinary(String input) {
        int value = Integer.parseInt(input);
        return String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public static List<String> transform(CPU cpu, List<String> assemblyCode) {
        List<String> machineCode = new ArrayList<>();
        Map<String, Integer> labelPositions = getLabelPositions(assemblyCode);

        for (String line : assemblyCode) {
            String[] parts = line.split(" ");

            if (Instruction.fromString(parts[0]) != null)
                machineCode.add(transformAssemblyToMachineCode(cpu, labelPositions, parts));
        }

        return machineCode;
    }

    private static Map<String, Integer> getLabelPositions(List<String> assemblyCode) {
        Map<String, Integer> labelPositions = new HashMap<>();
        int counter = 0;

        for (String line : assemblyCode) {
            if (line.isEmpty())
                continue;

            if (line.contains(":")) {
                labelPositions.put(line.substring(0, line.indexOf(':')), counter);
                continue;
            }
            counter++;
        }

        return labelPositions;
    }

    public static String transformAssemblyToMachineCode(CPU cpu, Map<String, Integer> labelPositions, String[] instructionParts) {
        StringBuilder sb = new StringBuilder();
        Instruction instruction = Instruction.fromString(instructionParts[0]);
        String instructionCode = instruction.getOperationCode();
        sb.append(instructionCode);

        if (instruction == Instruction.HALT || instruction == Instruction.DEC || instruction == Instruction.INC)
            return sb.toString();
        else {
            if (instruction == Instruction.JMP || instruction == Instruction.JZ) {
                int counter = labelPositions.get(instructionParts[1]);
                sb.append(toBinary(String.valueOf(counter)));
            } else {
                String address = cpu.getRegisterAddress(instructionParts[1]);

                if (address == null)
                   sb.append(toBinary(instructionParts[1]));
                else
                    sb.append(address);
            }
        }

        return sb.toString();
    }

    public static void halt(Process process) {
        process.setState(ProcessState.FINISHED);
    }

    public static void load(CPU cpu, String input) {
        accumulator = input.length() == 8 ? convertBinary(input) : cpu.getRegisterByAddress(input).getValue();
    }

    public static void store(CPU cpu, String address) {
        cpu.getRegisterByAddress(address).setValue(accumulator);
    }


    public static void add(CPU cpu, String input) {
        accumulator += input.length() == 8 ? convertBinary(input) : cpu.getRegisterByAddress(input).getValue();
    }

    public static void sub(CPU cpu, String input) {
        accumulator -= input.length() == 8 ? convertBinary(input) : cpu.getRegisterByAddress(input).getValue();
    }

    public static void mul(CPU cpu, String input) {
        accumulator *= input.length() == 8 ? convertBinary(input) : cpu.getRegisterByAddress(input).getValue();
    }

    public static void inc() {
        accumulator++;
    }

    public static void dec() {
        accumulator--;
    }

    public static void jmp(CPU cpu, String input) {
        cpu.getPC().setValue(convertBinary(input));
    }

    public static void jz(CPU cpu, String input) {
        if (accumulator == 0)
            jmp(cpu, input);
        else
            cpu.getPC().incrementValue(1);
    }
}
