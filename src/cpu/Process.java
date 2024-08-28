package cpu;

import assembler.Assembler;
import memory.Disk;
import memory.Frame;
import memory.Page;
import memory.RAM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Process {
    private final int pid;
    private final String name;
    private final int size;
    private ProcessState state;
    private int remainingTime;
    private final List<Page> pages = new ArrayList<>();
    private final List<String> pageTable = new ArrayList<>();
    private int[] valueOfRegisters = new int[5];
    private int programCounter = -1;
    public final static Comparator<Process> compareRT = Comparator.comparingInt(o -> o.remainingTime);

    public Process(int id, String name, Disk disk, CPU cpu) {
        this.pid = id;
        this.name = name.substring(0, name.lastIndexOf('.'));
        splitPages(name, disk, cpu);
        this.size = pages.stream().mapToInt(Page::getTotalSize).sum();
    }

    private void splitPages(String name, Disk disk, CPU cpu) {
        List<String> code = Assembler.transform(cpu, disk.getFileByName(name).getContent());
        this.remainingTime = code.size();

        int number = Frame.SIZE / Page.PAGE_SIZE;
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
        this.valueOfRegisters = Arrays.copyOf(valueOfRegisters, 5);
    }

    public void block() {
        if (checkState(ProcessState.RUNNING)) {
            setState(ProcessState.BLOCKED);
            System.out.println(name + " is blocked.");
        } else {
            System.out.println(name + " is not in running state.");
        }
    }

    public void unblock() {
        if (checkState(ProcessState.BLOCKED)) {
            setState(ProcessState.READY);
            System.out.println(name + " is unblocked.");
        } else {
            System.out.println(name + " is not blocked.");
        }
    }

    public void clearPageTable() {
        pageTable.clear();
    }

    public boolean checkNameMatch(String inputName) {
        return name.equals(inputName.substring(0, inputName.lastIndexOf('.')));
    }

    @Override
    public String toString() {
        return String.format("%-3s\t\t %-18s\t\t %-10s\t %-10s\n", pid, name, state, size);
    }
}
