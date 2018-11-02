package eventScheduler;

import java.util.*;
import java.util.Random;
import java.util.LinkedList; 
import java.util.Queue; 
 
public class FCFS {
	
	public static Queue<Event> list;
	public static Queue<Event> readyQueue;
	public static Event head;
	public static double newArrivalTime;
	public static double currentTime;
	public static boolean serverBusy;
	public static double averageServiceTime;
	public static double lambda;
	public static int eventsCompleted;
	public static double turnaroundArr[];
	public static int cycles;
	public static int totalEventsInQueue;
	public static double timeSpentIdle;
	
	
	public static void init(){
		list = new LinkedList<Event>();
		readyQueue = new LinkedList<Event>();
		newArrivalTime = 0;
		serverBusy = false;
		currentTime = 0;
		averageServiceTime = (double) 0.042;
		eventsCompleted = 0;
		turnaroundArr = new double [10000];
		cycles = 0;
		totalEventsInQueue = 0;
		timeSpentIdle = 0;
		generateEvents();
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
	
	public static double getNumberOfEventsInQueue() {
		return (readyQueue.size());

	}
	
	public static void generateReadyQueue() {
		Event e = new Event();
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
	
	public static void processEvent() {
		generateReadyQueue();
		Event e = readyQueue.peek();
		if(currentTime < e.arrivalTime) {
			timeSpentIdle += (e.arrivalTime - currentTime);
			currentTime = e.arrivalTime;
		}
		e.completionTime = (currentTime + e.serviceTime);
		currentTime = e.completionTime;
		e.completed = true;
		turnaroundArr[eventsCompleted] = (e.completionTime - e.arrivalTime);
		readyQueue.remove();
		eventsCompleted++;
	}
	
	public static void generateEvents() {
		Event first = new Event();
		first.arrivalTime = poissonRandomArrival(lambda);
		newArrivalTime += first.arrivalTime;
		first.serviceTime = generateServiceTime(averageServiceTime);
		first.place = 0;
		list.add(first);
		readyQueue.add(first);
		list.remove();
		first.completionTime = (first.arrivalTime + first.serviceTime);
		currentTime = first.completionTime;
		first.completed = true;
		turnaroundArr[eventsCompleted] = (first.completionTime - first.arrivalTime);
		readyQueue.remove();
		eventsCompleted++;
		for(int i = 1; i < 20000; i++) {
			Event e = new Event();
			newArrivalTime += poissonRandomArrival(lambda);
			e.arrivalTime = newArrivalTime;
			e.serviceTime = generateServiceTime(averageServiceTime);
			e.completed = false;
			e.place = i;
			list.add(e);
		}
	}
	
	public static void runSim() {
		while(eventsCompleted < 10000) {
			processEvent();
			totalEventsInQueue += getNumberOfEventsInQueue();
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

		double averageNumOfEventsInQueue = (double)(totalEventsInQueue / cycles);
		double averageTurnaroundRate = (total / 10000);
		double averageWaitingTime = calculateAverageWaitingTime(averageTurnaroundRate);
		double q = calculateQ(lambda, averageTurnaroundRate);
		double totalThroughput = 10000 / currentTime;
		double testRow = (lambda / (1/ averageServiceTime));
		double row = (1 - (timeSpentIdle / currentTime));
		double testQ = ((row) / (1 - row));
		
		System.out.print("\n");
		System.out.println("* * * * * * * * *ACTUAL DATA * * * * * * * * * * * * * * * * * * * * * * * *");
		System.out.println("AVERAGE TURNAROUND TIME: " + averageTurnaroundRate);
		System.out.println("TOTAL THROUGHPUT: " + totalThroughput);
		System.out.println("CPU UTILIZATION: " + row);
		System.out.println("AVERAGE NUMBER OF PROCESSES IN READY QUEUE: " + averageNumOfEventsInQueue);
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
			Event e = new Event();
			e = ((LinkedList<Event>) list).get(i);
			total += e.serviceTime;
		}
		
		double avg = total /15000;
		
		runSim();
		generateReport();
		
		System.out.println("Average Service Time: " + avg);
			
		serverBusy = false;
		
	}
}
