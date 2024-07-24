package cpu;

import java.util.PriorityQueue;

public class ProcessScheduler extends Thread {
    private CPU cpu;
    private PriorityQueue<Process> queue;
    private int numberOfProcesses;

    public ProcessScheduler(CPU cpu) {
        this.cpu = cpu;
        this.queue = new PriorityQueue<>(Process.compareRT);
    }

    public PriorityQueue<Process> getQueue() {
        return queue;
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public void addProcess(Process process) {
        queue.add(process);
        numberOfProcesses++;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            Process process = queue.poll();
            runProcess(process);
        }
    }

    private void runProcess(Process process) {
        cpu.setCurrentProcess(process);

        if (process.getProgramCounter() == -1) {
            System.out.println("Process " + process.getName() + " (ID = " + process.getPid() + ") started execution.");
            cpu.getPC().setValue(0);
            process.setState(ProcessState.RUNNING);
            cpu.execute();
        } else {
            System.out.println("Process " + process.getName() + " (ID = " + process.getPid() + ") continued execution.");
            cpu.loadValuesOfRegisters();
            process.setState(ProcessState.RUNNING);
            cpu.execute();
        }
    }
}
