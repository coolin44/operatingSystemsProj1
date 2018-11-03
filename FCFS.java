package eventScheduler;

import java.util.*;
import java.util.Random;
import java.util.LinkedList; 
import java.util.Queue; 
 
public class FCFS {
	
	public static LinkedList<Process> list;
	public static LinkedList<Process> readyQueue;
	public static Process head;
	public static double newArrivalTime;
	public static double currentTime;
	public static boolean serverBusy;
	public static double averageServiceTime;
	public static double lambda;
	public static int ProcessesCompleted;
	public static double turnaroundArr[];
	public static int cycles;
	public static int totalProcessesInQueue;
	public static double timeSpentIdle;
	
	
	public static void init(){
		list = new LinkedList<Process>();
		readyQueue = new LinkedList<Process>();
		newArrivalTime = 0;
		serverBusy = false;
		currentTime = 0;
		averageServiceTime = (double) 0.0315;
		ProcessesCompleted = 0;
		turnaroundArr = new double [10000];
		cycles = 0;
		totalProcessesInQueue = 0;
		timeSpentIdle = 0;
		generateProcesses();
	}
	
	public static double poissonRandomArrival(double lambda) {
		return (Math.log(Math.random())/-lambda);
	}
	
	public static double generateServiceTime(double lambda) {
		double expRandom = (double) (-lambda * Math.log(Math.random())/Math.log(2));
		return expRandom;
	}
	
	public static double calculateUtilization(double lambda) {
		return (lambda * averageServiceTime);
	}
	
	public static double calculateQ(double lambda, double Tq) {
		return (lambda * Tq);
	}
	
	public static double calculateAverageWaitingTime(double averageTurnaroundTime) {
		return (averageTurnaroundTime - averageServiceTime);
	}
	
	public static double getNumberOfProcessesInQueue() {
		return (readyQueue.size());

	}
	
	public static void generateReadyQueue() {
		Process e = new Process();
		e = list.peek();
		while(e.arrivalTime <= currentTime) {
			e = list.peek();
			if(!readyQueue.contains(e)) {
				readyQueue.add(e);
				list.remove();
			}
			try {
				e = list.peek();
			}
			catch(IndexOutOfBoundsException ex) {
				System.out.println("Index out of bounds exception...");
				break;
			}		
		}
		if(readyQueue.isEmpty()) {
			readyQueue.add(list.peek());
			list.remove();
		}
	}
	
	public static void processProcess() {
		generateReadyQueue();
		Process e = readyQueue.peek();
		if(currentTime < e.arrivalTime) {
			timeSpentIdle += (e.arrivalTime - currentTime);
			currentTime = e.arrivalTime;
		}
		e.completionTime = (currentTime + e.serviceTime);
		currentTime = e.completionTime;
		e.completed = true;
		turnaroundArr[ProcessesCompleted] = (e.completionTime - e.arrivalTime);
		Process p = new Process();
		p.type = 1;
		//readyQueue.add(0, p);
		scheduleDeparture();
	}
	
	public static void scheduleDeparture() {
		readyQueue.remove();
		ProcessesCompleted++;
	}
	
	
	public static void generateProcesses() {
		Process first = new Process();
		first.arrivalTime = poissonRandomArrival(lambda);
		newArrivalTime += first.arrivalTime;
		first.serviceTime = generateServiceTime(averageServiceTime);
		first.place = 0;
		first.type = 0;
		list.add(first);
		readyQueue.add(first);
		list.remove();
		first.completionTime = (first.arrivalTime + first.serviceTime);
		currentTime = first.completionTime;
		first.completed = true;
		turnaroundArr[ProcessesCompleted] = (first.completionTime - first.arrivalTime);
		Process p = new Process();
		p.type = 1;
		//readyQueue.add(0, p);
		scheduleDeparture();
		for(int i = 1; i < 20000; i++) {
			Process e = new Process();
			newArrivalTime += poissonRandomArrival(lambda);
			e.arrivalTime = newArrivalTime;
			e.serviceTime = generateServiceTime(averageServiceTime);
			e.completed = false;
			e.place = i;
			e.type = 0;
			list.add(e);
		}
	}
/*	
	public static void runSim() {
		Process p = readyQueue.peek();
		while(ProcessesCompleted < 10000) {
			switch(p.type) {
				case 1: scheduleDeparture();
				break;
				case 0:
					processProcess();
					totalProcessesInQueue += getNumberOfProcessesInQueue();
					cycles++;
			}
			p = readyQueue.peek();
		}
	}
*/	
	
	public static void runSim() {
		while(ProcessesCompleted < 10000) {
			processProcess();
			totalProcessesInQueue += getNumberOfProcessesInQueue();
			cycles++;
		}
		head = null;
	}

	//used to generate the report with the requested metrics and display them to the console
	public static void generateReport() {
		double total = 0;
		for(int j = 0; j < 10000; j++) {
			total += turnaroundArr[j];
		}

		double averageNumOfProcessesInQueue = (double)(totalProcessesInQueue / cycles);
		double averageTurnaroundRate = (total / 10000);
		double averageWaitingTime = calculateAverageWaitingTime(averageTurnaroundRate);
		double q = calculateQ(lambda, .06);
		double totalThroughput = 10000 / currentTime;
		double testRow = (lambda / (1/ averageServiceTime));
		double row = (1 - (timeSpentIdle / currentTime));
		double testQ = ((row) / (1 - row));
		
		System.out.print("\n");
		System.out.println("* * * * * * * * *ACTUAL DATA * * * * * * * * * * * * * * * * * * * * * * * *");
		System.out.println("AVERAGE TURNAROUND TIME: " + averageTurnaroundRate);
		System.out.println("TOTAL THROUGHPUT: " + totalThroughput);
		System.out.println("CPU UTILIZATION: " + row);
		System.out.println("AVERAGE NUMBER OF PROCESSES IN READY QUEUE: " + averageNumOfProcessesInQueue);
		System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
		System.out.print("\n");
		System.out.println("* * * * * * * * * TEST DATA* * * * * * * * * * * * * * * * * * * * * * * *");
		System.out.println("TESTING ROW: " + testRow);
		System.out.println("AVERAGE NUMBER OF PROCESSES IN QUEUE: " + q);
		System.out.println("TEST Q WITH REAL ROW: " + testQ);
		System.out.println("AVERAGE WAITING TIME: " + averageWaitingTime);
		System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
	}
	
	
/*	
	public static void generateReport() {
		double total = 0;
		for(int j = 0; j < 10000; j++) {
			total += turnaroundArr[j];
		}
		
		double row = calculateUtilization(lambda);
		double averageTurnaroundRate = (total / 10000);
		double averageWaitingTime = calculateAverageWaitingTime(averageTurnaroundRate);
		double q = calculateQ(lambda, averageTurnaroundRate);
		double totalThroughput = 10000 / currentTime;
		
		double testRow = (lambda / (1/ averageServiceTime));
		//float testQ = (row / (1 - row));
		
		System.out.print("\n");
		System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
		System.out.println("AVERAGE TURNAROUND TIME: " + averageTurnaroundRate);
		System.out.println("CPU UTILIZATION: " + row);
		System.out.println("TESTING ROW: " + testRow);
		System.out.println("AVERAGE NUMBER OF PROCESSES IN QUEUE: " + q);
		System.out.println("TOTAL THROUGHPUT: " + totalThroughput);
		//System.out.println("THIS NUM SHOULD MATCH Q: " + testQ);
		System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
	}
*/
	
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		lambda = sc.nextDouble();
		init();
		
		double total = 0;
		for(int i = 0; i < list.size();i++) {
			Process e = new Process();
			e = ((LinkedList<Process>) list).get(i);
			total += e.serviceTime;
		}
		
		double avg = total /15000;
		
		runSim();
		generateReport();
		
		System.out.println("Average Service Time: " + avg);
			
		serverBusy = false;
		
	}
}