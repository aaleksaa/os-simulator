package os;

import cpu.CPU;
import cpu.ProcessScheduler;
import file_system.FileSystem;
import memory.Disk;
import memory.RAM;

import java.util.Scanner;


public class OS {
    private final Disk disk;
    private final FileSystem fileSystem;
    private final RAM ram;
    private final CPU cpu;
    private final ProcessScheduler scheduler;

    public OS() {
        this.disk = new Disk();
        this.fileSystem = new FileSystem(disk);
        this.ram = new RAM();
        this.cpu = new CPU();
        this.scheduler = new ProcessScheduler(cpu, ram);
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
            case "load":
                CommandLine.load(scheduler, disk, cpu, ram, parts[1]);
                break;
            case "run":
                CommandLine.run(scheduler);
                break;
            case "mem":
                CommandLine.mem(disk, cpu, ram);
                break;
            case "ps":
                CommandLine.ps(scheduler);
                break;
            case "exit":
                CommandLine.exit();
                break;
            default:
                System.err.println("Unknown command!");
        }
    }

    public static void main(String[] args) {
        OS os = new OS();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print(os.getFileSystem().getDirectoryPath());
            os.executeCommand(sc.nextLine());
        }
    }
}
