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
    private List<Page> pages = new ArrayList<>();
    private List<String> pageTable = new ArrayList<>();
    private List<String> code = new ArrayList<>();

    public Process(String name, Disk disk, CPU cpu, RAM ram) {
        this.name = name;
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

    public static void main(String[] args) {
        Disk disk = new Disk();
        RAM ram = new RAM();
        CPU cpu = new CPU();
        FileSystem fileSystem = new FileSystem(disk);
        Process p = new Process("test.asm", disk, cpu, ram);

        ram.load(p);
        ram.printFreeNum();
        System.out.println(p.pages);
        System.out.println(p.pageTable);
        ram.remove(p);
        System.out.println("----------");
        ram.printFreeNum();
    }
}
