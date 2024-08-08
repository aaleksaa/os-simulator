package cpu;

import assembler.Assembler;
import memory.Disk;
import memory.Page;
import memory.RAM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Process {
    private final int pid;
    private final String name;
    private int remainingTime;
    private ProcessState state;
    private List<Page> pages;
    private List<String> pageTable;
    private int[] valueOfRegisters = new int[4];
    private int programCounter = -1;
    public final static Comparator<Process> compareRT = (o1, o2) -> Integer.compare(o1.remainingTime, o2.remainingTime);

    public Process(int id, String name, Disk disk, CPU cpu, RAM ram) {
        this.pid = id;
        this.name = name;
        this.pages = new ArrayList<>();
        this.pageTable = new ArrayList<>();
        splitPages(name, disk, cpu, ram);
    }

    private void splitPages(String name, Disk disk, CPU cpu, RAM ram) {
        List<String> code = disk.getFileByName(name).getContent();
        code.replaceAll(assemblyInstruction -> Assembler.transformAssemblyToMachineCode(cpu, assemblyInstruction));
        this.remainingTime = code.size();

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


    @Override
    public String toString() {
        return "Process " + name + " (PID = " + pid + ")";
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public int[] getValueOfRegisters() {
        return valueOfRegisters;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public ProcessState getState() {
        return state;
    }

    public List<Page> getPages() {
        return pages;
    }

    public List<String> getPageTable() {
        return pageTable;
    }

    public boolean checkState(ProcessState state) {
        return this.state == state;
    }

    public void decrementRemainingTime() {
        this.remainingTime--;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public void addToPageTable(String frameNumber) {
        pageTable.add(frameNumber);
    }

    public void setValueOfRegisters(int[] valueOfRegisters) {
        this.valueOfRegisters = Arrays.copyOf(valueOfRegisters, 4);
    }

    public void block(ProcessScheduler scheduler) {
        if (checkState(ProcessState.RUNNING)) {
            setState(ProcessState.BLOCKED);
            System.out.println(this + " is blocked.");
            scheduler.getQueue().remove(this);
        } else {
            System.out.println(this + " is not in running state.");
        }
    }

    public void unblock(ProcessScheduler scheduler) {
        if (checkState(ProcessState.BLOCKED)) {
            setState(ProcessState.READY);
            System.out.println(this + " is unblocked.");
            scheduler.getQueue().add(this);
        } else {
            System.out.println(this + " is not blocked.");
        }
    }
}
