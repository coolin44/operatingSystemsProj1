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
	
	
	public static void init(){
		q = new LinkedList<Event>();
		numberOfProccessesCompleted = 0;
		Event head = null;
		newCompletionTime = 0;
		newArrivalTime = 0;
		serverBusy = false;
		currentTime = 0;
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
	
	public static void scheduleEvent(int i, float time, Event e) {
		if(i == 0) {
			q.add(e);
			System.out.println("Process arrived to Queue at time " + time);
			System.out.println("With a service time of " + e.serviceTime);
			
		}
		else {
			currentTime = time;
			q.remove();
			System.out.println("Process departed from Queue at " + currentTime);
		}
	}
	
	
	public static void processEventArrival(Event e) {
		if(serverBusy == false) {
			serverBusy = true;
			scheduleEvent(0, e.arrivalTime, e);
			scheduleEvent(1, (currentTime + e.serviceTime), e);
		}
		else {
			scheduleEvent(0, currentTime, e);
			scheduleEvent(1, (currentTime + e.serviceTime), e);
		}
	}
	
	
	
	public static void main(String args[])
	{
		init();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		float lambda = sc.nextFloat();
		
		System.out.println("enter no of process: ");
		int n = sc.nextInt();
		
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
		

		for(int i = 0; i <= n; i++)
		{
			
			Event ae = new Event();
			
			newArrivalTime += generateArrivalTime(lambda);
			ae.arrivalTime = newArrivalTime;
			
			ae.serviceTime = generateServiceTime(lambda);
			
			System.out.println("The " + (i+1) + " Arrival Time is: " + ae.arrivalTime);
			System.out.println("The " + (i+1) + " Service Time is: " + ae.serviceTime);

			q.add(ae);			
			
				
		}	

		
		float turnaroundArr[] = new float[15];
		
		int i = 0;
		float time = 0;

		while(numberOfProccessesCompleted < 11) {
			
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
					System.out.println("Process arived before CPU was ready");
					head.completionTime = time;
				}
				else {
					time = head.arrivalTime;
					time += head.serviceTime;
					head.completionTime = time;
					System.out.println("Process arrived after CPU was ready, CPU was idle for a bit.");
				}
			}
			turnaroundArr[i] = (head.completionTime - head.arrivalTime);
			System.out.println("Process " + (i+1) + " ARRIVAL: " + head.arrivalTime + 
					" SERVICE: " + head.serviceTime + " COMPLETION: " + head.completionTime);
			i++;
			q.remove();
			numberOfProccessesCompleted++;
		}
		
		serverBusy = false;
		
		float total = 0;
		for(int j = 0; j < 10; j++) {
			total += turnaroundArr[j];
		}

		float averageTurnaroundRate = (total / 10);
		System.out.println("Average Turnaround Rate: " + averageTurnaroundRate);
		
/*		
			q.remove();
			q.remove();
			head = q.peek();
			
			System.out.println("THE HEAD OF THE QUEUE AFTER 2 DELETIONS HAS ARRIVAL TIME: " + head.arrivalTime + "AND A SERVICE TIME OF: " + head.serviceTime );
*/		
	}
}
