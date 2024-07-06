import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

public class SchedulerPanel {
    private int numProcesses;
    private int timeQuantum;
    private StringBuilder ganttChart;
    private double avgWaitingTime;
    private double avgTurnaroundTime;
    private double avgResponseTime;
    Queue<Process> processes = new LinkedList<>();
    Queue<Process> originalProcesses = new LinkedList<>();
    StringBuilder processDetails = new StringBuilder();

    public SchedulerPanel(int numProcesses, int timeQuantum) {
        this.numProcesses = numProcesses;
        this.timeQuantum = timeQuantum;
        ganttChart = new StringBuilder();
    }

    public void schedule() {
        for (int i = 1; i <= numProcesses; i++) {
            int arrivalTime = getInput("Enter Arrival Time for Process " + i + ":");
            int burstTime = getInput("Enter Burst Time for Process " + i + ":");
            processes.add(new Process(i, arrivalTime, burstTime));
            originalProcesses.add(new Process(i, arrivalTime, burstTime));
        }

        int totalTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int totalResponseTime = 0;
        int currProcess = -1;
        int responseTime = 0;
        while (!processes.isEmpty()) {
            Process process = processes.poll();
            Process originalProcess = originalProcesses.poll();
            int BurstTime = process.getBurstTime();
            int startTime = Math.max(totalTime, process.getArrivalTime());

            if (totalTime < process.getArrivalTime()) {
                processes.add(new Process(process.getId(), process.getArrivalTime(), process.getBurstTime()));
                originalProcesses.add(new Process(originalProcess.getId(), originalProcess.getArrivalTime(),
                        originalProcess.getBurstTime()));
                continue;
            }
            ganttChart.append("P").append(process.getId()).append(": ").append(startTime).append(" - ");

            if (BurstTime <= timeQuantum) {
                totalTime = startTime + BurstTime;
                ganttChart.append(totalTime).append(". ");

                process.setTurnaroundTime(totalTime - originalProcess.getArrivalTime());

                if (currProcess != process.getId()) {
                    process.setResponseTime(startTime - process.getArrivalTime());
                } else {
                    process.setResponseTime(responseTime);
                }
                int waitingTime = process.getWaitingTime(originalProcess);
                totalWaitingTime += waitingTime;
                totalTurnaroundTime += process.getTurnaroundTime();
                totalResponseTime += process.getResponseTime();

                processDetails.append("Process " + process.getId())
                        .append(": Turnaround Time = " + process.getTurnaroundTime())
                        .append(", Waiting Time = " + waitingTime)
                        .append(", Response Time = " + process.getResponseTime())
                        .append("\n");

            } else {
                totalTime = startTime + timeQuantum;
                ganttChart.append(totalTime).append(". ");

                processes.add(new Process(process.getId(), totalTime, BurstTime - timeQuantum));
                originalProcesses.add(new Process(originalProcess.getId(), originalProcess.getArrivalTime(),
                        originalProcess.getBurstTime()));

                if (currProcess != process.getId()) {
                    process.setResponseTime(startTime - process.getArrivalTime());
                } else {
                    process.setResponseTime(responseTime);
                }
            }
            currProcess = process.getId();
            responseTime = process.getResponseTime();
        }

        avgWaitingTime = (double) totalWaitingTime / numProcesses;
        avgTurnaroundTime = (double) totalTurnaroundTime / numProcesses;
        avgResponseTime = (double) totalResponseTime / numProcesses;

    }

    public String getGanttChart() {
        return ganttChart.toString() + "\n\n" + processDetails +
                "\nAverage Waiting Time: " + avgWaitingTime +
                "\nAverage Turnaround Time: " + avgTurnaroundTime +
                "\nAverage Response Time: " + avgResponseTime;
    }

    private int getInput(String message) {
        while (true) {
            try {
                int input = Integer.parseInt(JOptionPane.showInputDialog(message));
                if (input < 0) {
                    throw new NumberFormatException();
                }
                return input;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid positive number.");
            }
        }
    }

    private class Process {
        private int id;
        private int arrivalTime;
        private int burstTime;
        private int turnaroundTime;
        private int responseTime;

        public Process(int id, int arrivalTime, int burstTime) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
        }

        public int getId() {
            return id;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getBurstTime() {
            return burstTime;
        }

        public int getTurnaroundTime() {
            return turnaroundTime;
        }

        public void setTurnaroundTime(int turnaroundTime) {
            this.turnaroundTime = turnaroundTime;
        }

        public int getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(int responseTime) {
            this.responseTime = responseTime;
        }

        public int getWaitingTime(Process originalProcess) {
            return turnaroundTime - originalProcess.getBurstTime();
        }
    }
}