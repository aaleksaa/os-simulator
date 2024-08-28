package os;


import cpu.CPU;
import cpu.Process;
import cpu.ProcessScheduler;
import cpu.ProcessState;
import file_system.FileSystem;
import file_system.MyFile;
import memory.Disk;
import memory.RAM;

import java.util.ArrayList;
import java.util.List;


public class CommandLine {
    private final List<String> commands;
    private int commandIndex;

    public CommandLine() {
        this.commands = new ArrayList<>();
    }

    public void executeCommand(String command, FileSystem fileSystem, ProcessScheduler scheduler, Disk disk, CPU cpu, RAM ram) {
        commands.add(command);
        commandIndex = commands.size();
        String[] parts = command.toLowerCase().split(" ");

        switch (parts[0]) {
            case "cd":
                if (parts.length == 2)
                    cd(fileSystem, parts[1]);
                else
                    printError(parts[0]);
                break;
            case "mkdir":
                if (parts.length == 2)
                    mkdir(fileSystem, parts[1]);
                else
                    printError(parts[0]);
                break;
            case "dir":
                if (parts.length == 1)
                    dir(fileSystem);
                else
                    printError(parts[0]);
                break;
            case "rm":
                if (parts.length == 2)
                    rm(fileSystem, parts[1]);
                else
                    printError(parts[0]);
                break;
            case "ren":
                if (parts.length == 3)
                    ren(fileSystem, parts[1], parts[2]);
                else
                    printError(parts[0]);
                break;
            case "load":
                if (parts.length == 2)
                    load(scheduler, disk, cpu, ram, parts[1]);
                else
                    printError(parts[0]);
                break;
            case "run":
                if (parts.length == 1)
                    run(scheduler);
                else
                    printError(parts[0]);
                break;
            case "mem":
                if (parts.length == 2)
                    mem(parts[1], ram, disk);
                else
                    printError(parts[0]);
                break;
            case "ps":
                if (parts.length == 1)
                    ps(scheduler);
                else
                    printError(parts[0]);
                break;
            case "exit":
                if (parts.length == 1)
                    exit();
                else
                    printError(parts[0]);
                break;
            case "help":
                if (parts.length == 1)
                    help();
                else
                    printError(parts[0]);
                break;
            case "block":
                if (parts.length == 2)
                    block(scheduler, Integer.parseInt(parts[1]));
                else
                    printError(parts[0]);
                break;
            case "unblock":
                if (parts.length == 2)
                    unblock(scheduler, Integer.parseInt(parts[1]));
                else
                    printError(parts[0]);
                break;
            default:
                System.err.println("Unknown command!");
        }
    }

    public String getPreviousCommand() {
        if (commands.isEmpty())
            return "";

        if (commandIndex == 0)
            return commands.get(0);

        return commands.get(--commandIndex);
    }

    public String getNextCommand() {
        if (commands.isEmpty())
            return "";

        if (commandIndex >= commands.size() - 1)
            return commands.get(commands.size() - 1);

        return commands.get(++commandIndex);
    }

    private void printError(String command) {
        System.out.println("Parameters for command \"" + command + "\" are incorrect!");
    }

    public void cd(FileSystem fileSystem, String name) {
        try {
            fileSystem.changeDirectory(name);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void mkdir(FileSystem fileSystem, String name) {
        try {
            fileSystem.makeDirectory(name);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void dir(FileSystem fileSystem) {
        fileSystem.listFiles();
    }

    public void rm(FileSystem fileSystem, String name) {
        try {
            fileSystem.removeFileOrDirectory(name);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void ren(FileSystem fileSystem, String oldName, String newName) {
        try {
            fileSystem.renameDirectory(oldName, newName);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void load(ProcessScheduler scheduler, Disk disk, CPU cpu, RAM ram, String filename) {
        try {
            MyFile file = disk.getFileByName(filename);

            if (file == null)
                System.err.println("Program " + filename + " does not exist!");
            else {
                Process process = scheduler.getProcessByName(file.getName());

                if (process == null) {
                    process = new Process(scheduler.getNextPid(), file.getName(), disk, cpu);
                    scheduler.addToList(process);
                    scheduler.addToQueue(process);
                    ram.load(process);
                    System.out.println("Process " + process.getName() + " is loaded!");
                } else {
                    if (process.checkState(ProcessState.FINISHED)) {
                        ram.load(process);
                        scheduler.addToQueue(process);
                        System.out.println("Process " + process.getName() + " is loaded!");
                    }
                    else
                        System.out.println("Process " + process.getName() + " is already loaded!");
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            ram.printMemory();
        }
    }

    public void run(ProcessScheduler scheduler) {
        new Thread(scheduler).start();
    }

    public void mem(String type, RAM ram, Disk disk) {
        if (!type.equals("-m") && !type.equals("-s"))
            System.out.println("Unknown command!");
        else {
            if (type.equals("-m"))
                ram.printMemory();
            else
                disk.printDisk();
        }
    }

    public void ps(ProcessScheduler scheduler) {
        scheduler.printProcesses();
    }

    public void block(ProcessScheduler scheduler, int pid) {
        Process process = scheduler.getProcessByPID(pid);

        if (process == null)
            System.err.println("Process with (PID = " + pid + ") doesn't exist!");
        else
            process.block();
    }

    public void unblock(ProcessScheduler scheduler, int pid) {
        Process process = scheduler.getProcessByPID(pid);

        if (process == null)
            System.err.println("Process with (PID = " + pid + ") doesn't exist!");
        else
            process.unblock();
    }

    public void exit() {
        System.exit(0);
    }

    public void help() {
        System.out.println("dir                                      List files and directories.");
        System.out.println("cd <dir>                                 Change working directory.");
        System.out.println("mkdir <dir>                              Make a directory.");
        System.out.println("rm <name>                                Remove file or directory.");
        System.out.println("ren <old name> <new name>                Rename a directory.");
        System.out.println("load <program name>                      Load process.");
        System.out.println("block <pid>                              Block process.");
        System.out.println("unblock <run>                            Unblock process.");
        System.out.println("run                                      Start processes.");
        System.out.println("ps                                       List all processes.");
        System.out.println("mem -m                                   Show main memory.");
        System.out.println("mem -s                                   Show secondary memory.");
        System.out.println("help                                     List of commands.");
        System.out.println("exit                                     Terminate simulator.");
        System.out.println("cls                                      Clear terminal.");
    }
}
