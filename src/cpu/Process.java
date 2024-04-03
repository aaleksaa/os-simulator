package cpu;

import assembler.Assembler;
import memory.Disk;
import memory.Page;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private String name;
    private ProcessState state;
    private List<Page> pageTable = new ArrayList<>();
    private List<String> code = new ArrayList<>();

    public Process(String name, Disk disk, CPU cpu) {
        this.name = name;
        this.state = ProcessState.READY;
        readFile(name, disk, cpu);
    }

    private void readFile(String name, Disk disk, CPU cpu) {
        List<String> content = disk.getFileByName(name).getContent();

        for (String line : content)
            code.add(Assembler.transformAssemblyToMachineCode(cpu, line));
    }

    private void splitPages() {

    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }

    public boolean isReady() {
        return state == ProcessState.READY;
    }

    public boolean isRunning() {
        return state == ProcessState.RUNNING;
    }

    public boolean isFinished() {
        return state == ProcessState.FINISHED;
    }
}
