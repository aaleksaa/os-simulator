package assembler;

public enum Instruction {
    HALT("0000"),
    LOAD("0001"),
    STORE("0010"),
    ADD("0011"),
    SUB("0100"),
    MUL("0101"),
    INC("0110"),
    DEC("0111"),
    JMP("1000"),
    JZ("1001");
    private final String operationCode;

    private Instruction(String operationCode) {
        this.operationCode = operationCode;
    }

    public static Instruction fromString(String s) {
        return switch (s) {
            case "HALT" -> HALT;
            case "ADD" -> ADD;
            case "SUB" -> SUB;
            case "MUL" -> MUL;
            case "INC" -> INC;
            case "DEC" -> DEC;
            case "LOAD" -> LOAD;
            case "STORE" -> STORE;
            case "JMP" -> JMP;
            case "JZ" -> JZ;
            default -> null;
        };
    }

    public String getOperationCode() {
        return operationCode;
    }
}
