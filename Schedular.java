package scheduling;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler {
    public static List<Process> fcfs(List<Process> processes) {
        int currentTime = 0;

        for (Process process : processes) {
            process.setWaitingTime(currentTime);
            process.setTurnaroundTime(currentTime + process.getBurstTime());
            currentTime += process.getBurstTime();
        }

        return new ArrayList<>(processes);
    }

    public static List<Process> sjf(List<Process> processes) {
        List<Process> sortedProcesses = new ArrayList<>(processes);
        Collections.sort(sortedProcesses, (p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()));

        int currentTime = 0;

        for (Process process : sortedProcesses) {
            process.setWaitingTime(currentTime);
            process.setTurnaroundTime(currentTime + process.getBurstTime());
            currentTime += process.getBurstTime();
        }

        return sortedProcesses;
    }

    public static List<Process> priority(List<Process> processes) {
        List<Process> sortedProcesses = new ArrayList<>(processes);
        Collections.sort(sortedProcesses, (p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

        int currentTime = 0;

        for (Process process : sortedProcesses) {
            process.setWaitingTime(currentTime);
            process.setTurnaroundTime(currentTime + process.getBurstTime());
            currentTime += process.getBurstTime();
        }

        return sortedProcesses;
    }

    public static List<Process> roundRobin(List<Process> processes, int timeQuantum) {
        List<Process> scheduledProcesses = new ArrayList<>();
        List<Process> remainingProcesses = new ArrayList<>(processes);

        int currentTime = 0;
        while (!remainingProcesses.isEmpty()) {
            for (int i = 0; i < remainingProcesses.size(); i++) {
                Process process = remainingProcesses.get(i);
                int burstTime = Math.min(process.getBurstTime(), timeQuantum);

                process.setWaitingTime(currentTime);
                process.setTurnaroundTime(currentTime + burstTime);

                if (process.getBurstTime() > timeQuantum) {
                    process.setBurstTime(process.getBurstTime() - timeQuantum);
                    currentTime += timeQuantum;
                } else {
                    remainingProcesses.remove(process);
                    currentTime += process.getBurstTime();
                }

                scheduledProcesses.add(process);
            }
        }

        return scheduledProcesses;
    }

    }
