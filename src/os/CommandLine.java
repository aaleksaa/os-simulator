package os;


import cpu.CPU;
import cpu.Process;
import cpu.ProcessScheduler;
import file_system.FileSystem;
import memory.Disk;
import memory.RAM;


public class CommandLine {
    public static void cd(FileSystem fileSystem, String name) {
        try {
            fileSystem.changeDirectory(name);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void mkdir(FileSystem fileSystem, String name) {
        try {
            fileSystem.makeDirectory(name);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void dir(FileSystem fileSystem) {
        System.out.println(fileSystem.listFiles());
    }

    public static void rm(FileSystem fileSystem, Disk disk, String name) {
        try {
            fileSystem.removeFileOrDirectory(name, disk);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void ren(FileSystem fileSystem, String oldName, String newName) {
        try {
            fileSystem.renameDirectory(oldName, newName);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void load(ProcessScheduler scheduler, Disk disk, CPU cpu, RAM ram, String file) {
        try {
            Process process = new Process(scheduler.getNumberOfProcesses(), file, disk, cpu, ram);
            scheduler.addProcess(process);
            ram.load(process);
            System.out.println("Process " + file + " is loaded!");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.out.println(ram.printMemory());
        }
    }


    public static void run(ProcessScheduler scheduler) {
        scheduler.start();
    }

    public static void mem(Disk disk, CPU cpu, RAM ram) {
        System.out.println(disk.printDisk());
        System.out.println("----------------------------");
        System.out.println(cpu.printRegisters());
        System.out.println("-----------------------------");
        System.out.println(ram.printMemory());
    }

    public static void ps(ProcessScheduler scheduler) {
        System.out.println(scheduler.printProcesses());
    }

    public static void exit() {
        System.exit(0);
    }
}
