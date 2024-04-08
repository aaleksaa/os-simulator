package cpu;

import java.util.PriorityQueue;
import java.util.Queue;

public class ProcessScheduler extends Thread {
    private CPU cpu;
    private PriorityQueue<Process> queue;
    private int numberOfProcesses;

    public ProcessScheduler(CPU cpu) {
        this.cpu = cpu;
        this.queue = new PriorityQueue<>(Process.compareRT);;
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
            cpu.execute(process);
        }
    }
}
