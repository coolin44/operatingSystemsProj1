package eventScheduler;

import java.util.*;
import java.util.Random;
import java.util.LinkedList; 
import java.util.Queue; 
 
public class FCFS {
	
	public static Queue<Event> q;
	public static Event head;
	public static float newArrivalTime;
	public static float currentTime;
	public static boolean serverBusy;
	public static float averageServiceTime;
	public static float lambda;
	public static int eventsCompleted;
	public static float turnaroundArr[];
	
	
	public static void init(){
		q = new LinkedList<Event>();
		Event head = null;
		newArrivalTime = 0;
		serverBusy = false;
		currentTime = 0;
		averageServiceTime = (float) 0.06;
		lambda = 0;
		eventsCompleted = 0;
		turnaroundArr = new float [10000];
	}
	
	
	public static float generateArrivalTime(float lambda) {
	    Random r = new Random();
	    double L = Math.exp(-lambda);
	    float k = 0;
	    double p = 1.0;
	    do {
	        p = p * r.nextDouble();
	        k++;
	    } while (p > L);
	    return k - 1;
	}
	
	
	public static float generateServiceTime(float lambda) {
		float expRandom = (float) (-lambda * Math.log(Math.random())/Math.log(2));
		return expRandom;
	}
	
/*	
	public static float urand() {
		Random rand = new Random();
		float  n = rand.nextInt(1) + 0;
		return n;
	}
*/		
	
	public static float calculateUtilization(float lambda) {
		return (lambda * averageServiceTime);
	}
	
	public static float calculateQ(float lambda, float Tq) {
		return (lambda * Tq);
	}
	
	public static float calculateAverageWaitingTime(float averageTurnaroundTime) {
		return (averageTurnaroundTime - averageServiceTime);
	}
	
	public static void scheduleEvent(int i) {
		if(i == 0) {
			processEventArrival();
		}
		else {
			processEventDeparture();
			System.out.println("Process departed from Queue at " + currentTime);
		}
	}
	
	public static void processEventDeparture() {
		if(eventsCompleted == 0) {
			head = q.peek();
			currentTime = (head.arrivalTime + head.serviceTime);
			head.completionTime = currentTime;
		}
		else {
			head = q.peek();
			if(head.arrivalTime < currentTime) {
				currentTime += head.serviceTime;
				head.completionTime = currentTime;
			}
			else {
				currentTime = head.arrivalTime;
				currentTime += head.serviceTime;
				head.completionTime = currentTime;
			}
		}
		turnaroundArr[eventsCompleted] = (head.completionTime - head.arrivalTime);
		q.remove();
		eventsCompleted++;
	}
	
	
	public static void processEventArrival() {
		if(serverBusy == false) {
			serverBusy = true;
			Event ae = new Event();
			newArrivalTime += generateArrivalTime(lambda);
			ae.arrivalTime = newArrivalTime;
			ae.serviceTime = generateServiceTime(averageServiceTime);
			q.add(ae);
			System.out.println("Process " + (eventsCompleted + 1) + " arrived to Queue at time " + ae.arrivalTime);
			System.out.println("With a service time of " + ae.serviceTime);
		}
		else {
			Event ae = new Event();
			newArrivalTime += generateArrivalTime(lambda);
			ae.arrivalTime = newArrivalTime;
			ae.serviceTime = generateServiceTime(averageServiceTime);
			q.add(ae);
			System.out.println("Process " + (eventsCompleted + 1) +" arrived to Queue at time " + ae.arrivalTime);
			System.out.println("With a service time of " + ae.serviceTime);
		}
	}
	
	
	public static void runSim() {
		
		while(eventsCompleted < 10000) {
			scheduleEvent(0);
			scheduleEvent(1);
		}
	}
	
	public static void generateReport() {
		float total = 0;
		for(int j = 0; j < 10000; j++) {
			total += turnaroundArr[j];
		}

		
		float row = calculateUtilization(lambda);
		float averageTurnaroundRate = (total / 10000);
		float averageWaitingTime = calculateAverageWaitingTime(averageTurnaroundRate);
		float q = calculateQ(lambda, averageTurnaroundRate);
		
		float testQ = (row / (1 - row));
		
		System.out.print("\n");
		System.out.println("Average Turnaround Time: " + averageTurnaroundRate);
		System.out.println("CPU Utilization: " + row);
		System.out.println("Average Number Of Processes In Queue: " + q);
		System.out.println("THIS NUM SHOULD MATCH Q: " + testQ);
	}
	
	
	
	public static void main(String args[])
	{
		init();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		lambda = sc.nextFloat();
		runSim();
		generateReport();
			
		serverBusy = false;
		

	
	}
}
