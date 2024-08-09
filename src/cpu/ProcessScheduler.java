package cpu;

import memory.RAM;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ProcessScheduler extends Thread {
    private CPU cpu;
    private RAM ram;
    private PriorityQueue<Process> queue;
    private List<Process> processes;
    private int numberOfProcesses;
    private Process currentProcess;

    public ProcessScheduler(CPU cpu, RAM ram) {
        this.cpu = cpu;
        this.ram = ram;
        this.queue = new PriorityQueue<>(Process.compareRT);
        this.processes = new ArrayList<>();
    }

    public PriorityQueue<Process> getQueue() {
        return queue;
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void addProcess(Process process) {
        queue.add(process);
        processes.add(process);
        numberOfProcesses++;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            Process process = queue.peek();
            currentProcess = process;
            runProcess(currentProcess);


            if (process.checkState(ProcessState.FINISHED))
                queue.remove(process);
        }
    }

    public String printProcesses() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-3s\t\t %-18s\t %-10s\t\t %-10s\n", "PID", "NAME", "STATE", "MEM"));

        for (Process process : processes)
            sb.append(process);

        return sb.toString();
    }

    private void runProcess(Process process) {
        if (process.getProgramCounter() == -1) {
            System.out.println(process.getName() + " (PID = " + process.getPid() + ") started execution.");
            cpu.getPC().setValue(0);
            process.setState(ProcessState.RUNNING);
            cpu.execute(ram, currentProcess);
            System.out.println(process.getName() + " (PID = " + process.getPid() + ") finished execution.");
        } else {
            System.out.println(process.getName() + " (PID = " + process.getPid() + ") continued execution.");
            cpu.loadValuesOfRegisters(currentProcess);
            process.setState(ProcessState.RUNNING);
            cpu.execute(ram, currentProcess);
            System.out.println(process.getName() + " (PID = " + process.getPid() + ") finished execution.");
        }
    }
}
