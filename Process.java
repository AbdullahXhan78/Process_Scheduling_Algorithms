package scheduling;


public class Process {
    private int id;
    private int burstTime;
    private int priority;
    private int waitingTime;
    private int turnaroundTime;

    public Process(int id, int burstTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.priority = 0; // Default priority for simplicity
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }

    public Process(int id, int burstTime, int priority) {
        this.id = id;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }

    // Getters and setters (if needed)

    public int getId() {
        return id;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

	public void setBurstTime(int i) {
		// TODO Auto-generated method stub
		
	}
}