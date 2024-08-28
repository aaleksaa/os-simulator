package cpu;

import memory.RAM;

import java.util.*;

public class ProcessScheduler extends Thread {
    private final CPU cpu;
    private final RAM ram;
    private final PriorityQueue<Process> readyQueue = new PriorityQueue<>(Process.compareRT);
    private final Queue<Process> waitingQueue = new LinkedList<>();
    private final List<Process> processes = new ArrayList<>();
    private Process currentProcess;

    public ProcessScheduler(CPU cpu, RAM ram) {
        this.cpu = cpu;
        this.ram = ram;
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

    public void addToQueue(Process process) {
        readyQueue.add(process);
    }

    public void addToList(Process process) {
        processes.add(process);
    }

    public int getNextPid() {
        return processes.size() + 1;
    }

    public Process getProcessByPID(int pid) {
        return processes.stream()
                .filter(process -> process.getPid() == pid)
                .findFirst()
                .orElse(null);
    }

    public Process getProcessByName(String name) {
        return processes.stream()
                .filter(process -> process.checkNameMatch(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void run() {
        while (!readyQueue.isEmpty() || !waitingQueue.isEmpty()) {
            Process process = !readyQueue.isEmpty() ? readyQueue.poll() : waitingQueue.poll();
            currentProcess = process;

            if (currentProcess.checkState(ProcessState.BLOCKED))
                waitingQueue.add(currentProcess);
            else {
                runProcess(currentProcess);

                if (!currentProcess.checkState(ProcessState.FINISHED))
                    waitingQueue.add(process);

                if (readyQueue.isEmpty() && !waitingQueue.isEmpty()) {
                    readyQueue.addAll(waitingQueue);
                    waitingQueue.clear();
                }
            }
        }
    }

    public void printProcesses() {
        System.out.printf("%-3s\t\t %-18s\t %-10s\t\t %-10s\n", "PID", "NAME", "STATE", "MEM");
        processes.forEach(System.out::print);
    }

    private void runProcess(Process process) {
        System.out.println(process.getName() + " (PID = " + process.getPid() + ") is executing.");

        if (process.getProgramCounter() == -1)
            cpu.getPC().setValue(0);
        else
            cpu.loadValuesOfRegisters(currentProcess);

        process.setState(ProcessState.RUNNING);
        cpu.execute(ram, currentProcess);
    }
}
