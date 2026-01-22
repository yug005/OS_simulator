package models;

public class Process {
	// processId = unique ID, debug + table output me use hota hai.
	private int processId;
	// arrivalTime = process kab system me aayi.
	private int arrivalTime;
	// burstTime = CPU time needed.
	private int burstTime;
	// remainingTime = preemptive schedulers ke liye.
	private int remainingTime;
	// priority = future priority scheduling ke liye reserve.
	private int priority;

	// Scheduling results (populated by algorithms like FCFS / RR)
	private int completionTime = -1;
	private int waitingTime = -1;
	private int turnaroundTime = -1;

	public Process(int processId, int arrivalTime, int burstTime, int remainingTime, int priority) {
		// Constructor: base fields set karte hain, metrics baad me set honge.
		this.processId = processId;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.remainingTime = remainingTime;
		this.priority = priority;
	}

	public int getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(int completionTime) {
		this.completionTime = completionTime;
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

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getBurstTime() {
		return burstTime;
	}

	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Process{" +
				"processId=" + processId +
				", arrivalTime=" + arrivalTime +
				", burstTime=" + burstTime +
				", remainingTime=" + remainingTime +
				", priority=" + priority +
				", completionTime=" + completionTime +
				", waitingTime=" + waitingTime +
				", turnaroundTime=" + turnaroundTime +
				'}';
	}
}
