package cpu;

import memory.RAM;

import java.util.PriorityQueue;

public class ProcessScheduler extends Thread {
    private CPU cpu;
    private RAM ram;
    private PriorityQueue<Process> queue;
    private int numberOfProcesses;

    public ProcessScheduler(CPU cpu, RAM ram) {
        this.cpu = cpu;
        this.ram = ram;
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
            Process process = queue.peek();
            runProcess(process);


            if (process.isFinished())
                queue.remove(process);
        }
    }

    public String printProcesses() {
        StringBuilder sb = new StringBuilder();

        for (Process process : queue)
            sb.append(process).append(" ").append(process.getState()).append("\n");

        return sb.toString();
    }

    private void runProcess(Process process) {
        cpu.setCurrentProcess(process);

        if (process.getProgramCounter() == -1) {
            System.out.println(process + " started execution.");
            cpu.getPC().setValue(0);
            process.setState(ProcessState.RUNNING);
            cpu.execute(ram);
        } else {
            System.out.println(process + " continued execution.");
            cpu.loadValuesOfRegisters();
            process.setState(ProcessState.RUNNING);
            cpu.execute(ram);
        }
    }
}
