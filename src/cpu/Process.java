package cpu;

import memory.Disk;
import memory.Page;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private String name;
    private ProcessState state;
    private List<Page> pageTable = new ArrayList<>();
    private List<String> code = new ArrayList<>();

    public Process(String name) {
        this.name = name;
        this.state = ProcessState.READY;
    }

    private void readFile() {

    }

    private void splitPages() {

    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }

    public boolean isReady() {
        return state == ProcessState.READY;
    }

    public boolean isRunning() {
        return state == ProcessState.RUNNING;
    }

    public boolean isFinished() {
        return state == ProcessState.FINISHED;
    }
}
