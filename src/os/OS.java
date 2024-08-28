package os;

import cpu.CPU;
import cpu.ProcessScheduler;
import file_system.FileSystem;
import memory.Disk;
import memory.RAM;

public class OS {
    private final Disk disk;
    private final FileSystem fileSystem;
    private final RAM ram;
    private final CPU cpu;
    private final ProcessScheduler scheduler;
    private final CommandLine commandLine;

    public OS() {
        this.disk = new Disk();
        this.fileSystem = new FileSystem(disk);
        this.ram = new RAM(4096);
        this.cpu = new CPU(fileSystem);
        this.scheduler = new ProcessScheduler(cpu, ram);
        this.commandLine = new CommandLine();
    }

    public Disk getDisk() {
        return disk;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public RAM getRam() {
        return ram;
    }

    public CPU getCpu() {
        return cpu;
    }

    public ProcessScheduler getScheduler() {
        return scheduler;
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public void executeCommand(String command) {
        commandLine.executeCommand(command, fileSystem, scheduler, disk, cpu, ram);
    }
}
