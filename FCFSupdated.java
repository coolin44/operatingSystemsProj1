package eventScheduler;

import java.util.*;
import java.util.Random;
import java.util.LinkedList; 
import java.util.Queue; 
 
public class FCFS {
	
	public static Queue<Event> q;
	public static int numberOfProccessesCompleted;
	public static Event head;
	public static float newCompletionTime;
	public static float newArrivalTime;
	public static float currentTime;
	public static boolean serverBusy;
	public static int eventsInQueue;
	public static float averageServiceTime;
	public static float averageNumberOfEventsInQueue;
	public static float lambda;
	public static int eventsCompleted;
	public static float turnaroundArr[];
	
	
	public static void init(){
		q = new LinkedList<Event>();
		numberOfProccessesCompleted = 0;
		Event head = null;
		newCompletionTime = 0;
		newArrivalTime = 0;
		serverBusy = false;
		currentTime = 0;
		eventsInQueue = 0;
		averageServiceTime = (float) 0.06;
		averageNumberOfEventsInQueue = 0;
		lambda = 0;
		eventsCompleted = 0;
		turnaroundArr = new float [10000];
	}
	
	
	public static int generateArrivalTime(float lambda) {
	    Random r = new Random();
	    double L = Math.exp(-lambda);
	    int k = 0;
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
			System.out.println("Process arrived to Queue at time " + ae.arrivalTime);
			System.out.println("With a service time of " + ae.serviceTime);
		}
		else {
			Event ae = new Event();
			newArrivalTime += generateArrivalTime(lambda);
			ae.arrivalTime = newArrivalTime;
			ae.serviceTime = generateServiceTime(averageServiceTime);
			q.add(ae);
			System.out.println("Process arrived to Queue at time " + ae.arrivalTime);
			System.out.println("With a service time of " + ae.serviceTime);
		}
	}
	
	
	public void runSim() {
		
		while(eventsCompleted < 10000) {
			scheduleEvent(0);
			scheduleEvent(1);
		}
	}
	
	
	
	public static void main(String args[])
	{
		init();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		lambda = sc.nextFloat();
		
		//System.out.println("enter no of process: ");
		//int n = sc.nextInt();
		
		int n = 10500;
		
/*		
		for(int i =0; i <= n; i++) {
			Event e = new Event(0, currentTime, lambda);
			processEventArrival(e);
		}
*/		

/*		
		Event ae = new Event(0, currentTime, lambda);
		q.add(ae);
		Event head = q.peek();
		System.out.println("The first Arrival Time is: " + ae.arrivalTime);
		System.out.println("The first Service Time is: " + ae.serviceTime);
		ae.completionTime = (ae.arrivalTime + ae.serviceTime);
		currentTime = ae.completionTime;
		System.out.println("The first process completes at " + ae.completionTime );
		
		Event nextArrival = new Event(0, currentTime, lambda);
		if(nextArrival.arrivalTime <= )
		q.remove();
*/
		
		//used to generate a queue of events 
		
/*		
		for(int i = 0; i < n; i++)
		{
			
			Event ae = new Event();
			
			newArrivalTime += generateArrivalTime(lambda);
			ae.arrivalTime = newArrivalTime;
			ae.type = 0;
			
			ae.serviceTime = generateServiceTime(averageServiceTime);
			
			System.out.println("The " + (i+1) + " Arrival Time is: " + ae.arrivalTime);
			System.out.println("The " + (i+1) + " Service Time is: " + ae.serviceTime);

			q.add(ae);	
			eventsInQueue++;
			
				
		}	
*/
		
		
/*		
		float turnaroundArr[] = new float[10000];
		
		int i = 0;
		float time = 0;

		while(numberOfProccessesCompleted < 10000) {
			
			if(!serverBusy) {
				serverBusy = true;
				head = q.peek();
				time = (head.arrivalTime + head.serviceTime);
				head.completionTime = time;
			}
			else {
				head = q.peek();
				if(head.arrivalTime < time) {
					time += head.serviceTime;
				//	System.out.println("Process arrived before CPU was ready");
					head.completionTime = time;
				}
				else {
					time = head.arrivalTime;
					time += head.serviceTime;
					head.completionTime = time;
				//	System.out.println("Process arrived after CPU was ready, CPU was idle for a bit.");
				}
			}
			
			
			turnaroundArr[i] = (head.completionTime - head.arrivalTime);
		//	System.out.println("Process " + (i+1) + " ARRIVAL: " + head.arrivalTime + 
		//			" SERVICE: " + head.serviceTime + " COMPLETION: " + head.completionTime);
			i++;
			q.remove();
			eventsInQueue--;
			numberOfProccessesCompleted++;
		}
*/		
		serverBusy = false;
		
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
}
