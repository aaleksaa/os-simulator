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
        fileSystem.listFiles();
    }

    public static void rm(FileSystem fileSystem, String name) {
        try {
            fileSystem.removeFileOrDirectory(name);
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
            Process process = new Process(scheduler.getNextPid(), file, disk, cpu, ram);
            scheduler.addProcess(process);
            ram.load(process);
            System.out.println("Process " + process.getName() + " is loaded!");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            ram.printMemory();
        }
    }
    

    public static void run(ProcessScheduler scheduler) {
        scheduler.start();
    }

    public static void mem(String type, RAM ram, Disk disk) {
        if (type.equals("-m"))
            ram.printMemory();
        else
            disk.printDisk();
    }

    public static void ps(ProcessScheduler scheduler) {
        scheduler.printProcesses();
    }

    public static void block(ProcessScheduler scheduler, int pid) {
        Process process = scheduler.getProcessByPID(pid);

        if (process == null)
            System.err.println("Process with (PID = " + pid + ") doesn't exist!");
        else
            process.block();
    }

    public static void unblock(ProcessScheduler scheduler, int pid) {
        Process process = scheduler.getProcessByPID(pid);

        if (process == null)
            System.err.println("Process with (PID = " + pid + ") doesn't exist!");
        else
            process.unblock();
    }

    public static void exit() {
        System.exit(0);
    }

    public static void help() {
        System.out.printf("%-40s %s\n", "dir", "List files and directories.");
        System.out.printf("%-40s %s\n", "cd <dir>", "Change working directory.");
        System.out.printf("%-40s %s\n", "mkdir <dir>", "Make a directory.");
        System.out.printf("%-40s %s\n", "rm <name>", "Remove file or directory.");
        System.out.printf("%-40s %s\n", "ren <new name> <old name>", "Rename a directory.");
        System.out.printf("%-40s %s\n", "load <program name>", "Load a process.");
        System.out.printf("%-40s %s\n", "ps", "List all processes.");
        System.out.printf("%-40s %s\n", "mem", "Show main memory.");
        System.out.printf("%-40s %s\n", "exit", "Terminate simulator.");
        System.out.printf("%-40s %s\n", "help", "List of commands.");
    }
}
