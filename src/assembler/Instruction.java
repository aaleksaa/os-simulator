package assembler;

public enum Instruction {
    HALT("0000"),
    MOV("0001"),
    ADD("0010"),
    SUB("0011"),
    MUL("0100"),
    INC("0101"),
    DEC("0110");

    private String operationCode;

    private Instruction(String operationCode) {
        this.operationCode = operationCode;
    }

    public static Instruction fromString(String s) {
        return switch (s) {
            case "HALT" -> HALT;
            case "MOV" -> MOV;
            case "ADD" -> ADD;
            case "SUB" -> SUB;
            case "MUL" -> MUL;
            case "INC" -> INC;
            case "DEC" -> DEC;
            default -> null;
        };
    }

    public String getOperationCode() {
        return operationCode;
    }
}
