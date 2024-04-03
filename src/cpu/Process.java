package cpu;

import assembler.Assembler;
import file_system.FileSystem;
import memory.Disk;
import memory.Page;
import memory.RAM;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private String name;
    private ProcessState state;
    private List<Page> pageTable = new ArrayList<>();
    private List<String> code = new ArrayList<>();

    public Process(String name, Disk disk, CPU cpu, RAM ram) {
        this.name = name;
        this.state = ProcessState.READY;
        readFile(name, disk, cpu);
        splitPages(ram);
    }

    private void readFile(String name, Disk disk, CPU cpu) {
        List<String> content = disk.getFileByName(name).getContent();

        for (String line : content)
            code.add(Assembler.transformAssemblyToMachineCode(cpu, line));
    }

    private void splitPages(RAM ram) {
        int frameSize = ram.getFrameSize();
        int number = frameSize / 16;
        int counter = 0;

        while (counter < code.size()) {
            Page page = new Page();

            for (int i = 0; i < number; i++)
                if (counter < code.size()) {
                    page.add(code.get(counter));
                    counter++;
                }

            pageTable.add(page);
        }
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
