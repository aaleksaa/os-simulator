package cpu;

import memory.RAM;

import java.util.*;

public class ProcessScheduler extends Thread {
    private CPU cpu;
    private RAM ram;
    private PriorityQueue<Process> readyQueue;
    private Queue<Process> waitingQueue;
    private List<Process> processes;
    private Process currentProcess;

    public ProcessScheduler(CPU cpu, RAM ram) {
        this.cpu = cpu;
        this.ram = ram;
        this.readyQueue = new PriorityQueue<>(Process.compareRT);
        this.waitingQueue = new LinkedList<>();
        this.processes = new ArrayList<>();
    }

    public PriorityQueue<Process> getReadyQueue() {
        return readyQueue;
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
        processes.add(process);
    }

    public int getNextPid() {
        return processes.size();
    }

    @Override
    public void run() {
        while (!readyQueue.isEmpty() || !waitingQueue.isEmpty()) {
            Process process = !readyQueue.isEmpty() ? readyQueue.poll() : waitingQueue.poll();

            currentProcess = process;
            runProcess(currentProcess);

            if (!currentProcess.checkState(ProcessState.FINISHED))
                waitingQueue.add(process);

            if (readyQueue.isEmpty() && !waitingQueue.isEmpty()) {
                readyQueue.addAll(waitingQueue);
                waitingQueue.clear();
            }
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
        } else {
            System.out.println(process.getName() + " (PID = " + process.getPid() + ") continued execution.");
            cpu.loadValuesOfRegisters(currentProcess);
            process.setState(ProcessState.RUNNING);
            cpu.execute(ram, currentProcess);
        }
    }
}
