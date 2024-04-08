package cpu;

import assembler.Assembler;
import memory.Disk;
import memory.Page;
import memory.RAM;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Process {
    private int id;
    private String name;
    private int remainingTime;
    private ProcessState state;
    private List<Page> pages = new ArrayList<>();
    private List<String> pageTable = new ArrayList<>();
    private List<String> code = new ArrayList<>();
    public final static Comparator<Process> compareRT = (o1, o2) -> Integer.compare(o1.remainingTime, o2.remainingTime);
    public final static Comparator<Process> compareID = (o1, o2) -> Integer.compare(o1.id, o2.id);

    public Process(int id, String name, Disk disk, CPU cpu, RAM ram) {
        this.id = id;
        this.name = name;
        readFile(name, disk, cpu);
        splitPages(ram);
        this.remainingTime = code.size();
        this.state = ProcessState.RUNNING;
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

            pages.add(page);
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

    public List<Page> getPages() {
        return pages;
    }

    public List<String> getPageTable() {
        return pageTable;
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

    public void addToPageTable(String frameNumber) {
        pageTable.add(frameNumber);
    }

    public String getName() {
        return name;
    }
}
