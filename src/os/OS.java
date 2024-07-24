package os;

import cpu.CPU;
import cpu.ProcessScheduler;
import file_system.FileSystem;
import memory.Disk;
import memory.RAM;


public class OS {
    private Disk disk;
    private FileSystem fileSystem;
    private RAM ram;
    private CPU cpu;
    private ProcessScheduler scheduler;

    public OS() {
        this.disk = new Disk();
        this.fileSystem = new FileSystem(disk);
        this.ram = new RAM();
        this.cpu = new CPU();
        this.scheduler = new ProcessScheduler(cpu);
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

    public void executeCommand(String command) {
        String[] parts = command.split(" ");

        switch (parts[0]) {
            case "cd":
                CommandLine.cd(fileSystem, parts[1]);
                break;
            case "mkdir":
                CommandLine.mkdir(fileSystem, parts[1]);
                break;
            case "dir":
                CommandLine.dir(fileSystem);
                break;
            case "rm":
                CommandLine.rm(fileSystem, disk, parts[1]);
                break;
            case "ren":
                CommandLine.ren(fileSystem, parts[1], parts[2]);
                break;
            case "exit":
                CommandLine.exit();
                break;
            default:
                System.err.println("Unknown command!");
        }
    }
}
