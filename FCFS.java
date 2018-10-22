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
	
	public static void init(){
		q = new LinkedList<Event>();
		numberOfProccessesCompleted = 0;
		Event head = null;
		newCompletionTime = 0;
		newArrivalTime = 0;
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
	
	public static float urand() {
		Random rand = new Random();
		float  n = rand.nextInt(1) + 0;
		return n;
	}	
	
	public static void main(String args[])
	{
		init();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		float lambda = sc.nextFloat();
		
		System.out.println("enter no of process: ");
		int n = sc.nextInt();

		for(int i = 0; i < n; i++)
		{
			Event e = new Event();
			
			newArrivalTime += generateArrivalTime(lambda);
			e.arrivalTime = newArrivalTime;
			
			e.serviceTime = generateServiceTime(lambda);
			
			System.out.println("The " + (i+1) + " Arrival Time is: " + e.arrivalTime);
			System.out.println("The " + (i+1) + " Service Time is: " + e.serviceTime);

			q.add(e);
			
			
				
		}	
		
		float clock = 0;
		head = q.peek();
		while(numberOfProccessesCompleted < 10000) {
			head = q.peek();
			numberOfProccessesCompleted++;
		}
		
/*		
			q.remove();
			q.remove();
			head = q.peek();
			
			System.out.println("THE HEAD OF THE QUEUE AFTER 2 DELETIONS HAS ARRIVAL TIME: " + head.arrivalTime + "AND A SERVICE TIME OF: " + head.serviceTime );
*/		
	}
}
