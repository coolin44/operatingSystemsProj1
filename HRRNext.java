package eventScheduler;

import java.util.*; 

public class HRRNext {
	
	public static Queue<EventHRRN> list;
	public static Queue<EventHRRN> readyQueue;
	public static Event head;
	public static double newArrivalTime;
	public static double currentTime;
	public static boolean serverBusy;
	public static double averageServiceTime;
	public static double lambda;
	public static int eventsCompleted;
	public static double turnaroundArr[];
	
	
	public static void init(){
		list = new LinkedList<EventHRRN>();
		readyQueue = new LinkedList<EventHRRN>();
		Event head = null;
		newArrivalTime = 0;
		serverBusy = false;
		averageServiceTime = (double) 0.06;
		eventsCompleted = 0;
		turnaroundArr = new double [501];
		generateEvents();
	}
	
	
	public static double poissonRandomArrival(double lambda) {
		return (Math.log(Math.random())/-lambda);
	}
	
	public static double getNumberOfEventsInQueue() {
		int num = (readyQueue.size()-1);
		return num;
	}
	
	
/*	
	public static double getNumberOfEventsInQueue() {
		int num = 0;
		for (int i = 0; i < list.size(); i++) {
			EventHRRN e = new EventHRRN();
			e = ((LinkedList<EventHRRN>) list).get(i);
			if(e.arrivalTime <= currentTime && e.completed != true) {
				num++;
			}
		}
		return num;
	}
*/	
	
	public static double generateArrivalTime(double lambda) {
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
	
	
	public static double generateServiceTime(double lambda) {
		double expRandom = (double) (-lambda * Math.log(Math.random())/Math.log(2));
		return expRandom;
	}
	
	public static void generateReadyQueue() {
		int i = 0;
		EventHRRN e = new EventHRRN();
		//e = ((LinkedList<EventHRRN>) list).get(i);
		while(e.arrivalTime <= currentTime && !list.isEmpty()) {
			e = ((LinkedList<EventHRRN>) list).get(i);
			
			if(!readyQueue.contains(e)) {
				readyQueue.add(e);
				list.remove(e);

			}
			i++;
			try {
			e = ((LinkedList<EventHRRN>) list).get(i);
			}
			catch(IndexOutOfBoundsException ex) {
				System.out.println("Index out of bounds exception...");
				break;
			}		
		}
	}
	
	
	public static int findHighestR() {
		generateReadyQueue();
		int i = 0;
		EventHRRN highestR = ((LinkedList<EventHRRN>) readyQueue).get(0);
		highestR.waitingTime = (currentTime - highestR.arrivalTime); 
		highestR.responseRatio = (highestR.serviceTime + highestR.waitingTime) / highestR.serviceTime;
		for(int j =1; j < readyQueue.size()-1; j++) {
			EventHRRN eve = ((LinkedList<EventHRRN>) readyQueue).get(j);
			eve.waitingTime = (currentTime - eve.arrivalTime);
			eve.responseRatio = (eve.serviceTime + eve.waitingTime) / eve.serviceTime;
			if (eve.responseRatio > highestR.responseRatio && eve.arrivalTime <= currentTime) {
				highestR = eve;
				i = j;
			}
		}
		return i;
	}
	
	
	
/*	
	public static int findHighestR() {
		int i = 0;
		EventHRRN highestR = ((LinkedList<EventHRRN>) list).get(0);
		highestR.waitingTime = (currentTime - highestR.arrivalTime); 
		highestR.responseRatio = (highestR.serviceTime + highestR.waitingTime) / highestR.serviceTime;
		for(int j =1; j < list.size()-1; j++) {
			EventHRRN eve = ((LinkedList<EventHRRN>) list).get(j);
			eve.waitingTime = (currentTime - eve.arrivalTime);
			eve.responseRatio = (eve.serviceTime + eve.waitingTime) / eve.serviceTime;
			if (eve.responseRatio > highestR.responseRatio && eve.arrivalTime <= currentTime) {
				highestR = eve;
				i = j;
			}
		}
		return i;
	}
*/
	
	public static void runSim() {
		//used to debug
		int i = 0;
		double totalEventsInQueue = 0;
		while(eventsCompleted < 500) {
			processEvent();
			totalEventsInQueue += getNumberOfEventsInQueue();
			
			i++;
		}
		System.out.println("Befre dividing: " + totalEventsInQueue);
		double averageNumOfEventsInQueue = (double)(totalEventsInQueue / 500);
		System.out.println("");
		System.out.println("The average Number of Events In Queue: " + averageNumOfEventsInQueue);
		head = null;
	}
	
	public static void processEvent() {
		int index = findHighestR();
		EventHRRN e = ((LinkedList<EventHRRN>) readyQueue).get(index);
		if(currentTime < e.arrivalTime) {
			currentTime = e.arrivalTime;
		}
		e.completionTime = (currentTime + e.serviceTime);
		currentTime = e.completionTime;
		e.completed = true;
		System.out.println("Event " + e.place + " ARRIVED: " + e.arrivalTime + " FINISHED: " + currentTime + " R: " + e.responseRatio);
		turnaroundArr[eventsCompleted] = (e.completionTime - e.arrivalTime);
		((LinkedList<EventHRRN>) readyQueue).remove(index);
		
		eventsCompleted++;
	}
	
	
	
/*	
	public static void processEvent() {
		int index = findHighestR();
		EventHRRN e = ((LinkedList<EventHRRN>) list).get(index);
		if(currentTime < e.arrivalTime) {
			currentTime = e.arrivalTime;
		}
		e.completionTime = (currentTime + e.serviceTime);
		currentTime = e.completionTime;
		e.completed = true;
		System.out.println("Event " + e.place + " Arrived at time: " + e.arrivalTime + " and is finished at time: " + currentTime);
		turnaroundArr[eventsCompleted] = (e.completionTime - e.arrivalTime);
		((LinkedList<EventHRRN>) list).remove(index);
		
		eventsCompleted++;
	}
*/	
	
	public static void generateEvents() {
		EventHRRN first = new EventHRRN();
		//first.arrivalTime = generateArrivalTime(lambda);
		first.arrivalTime = poissonRandomArrival(lambda);
		first.place = 0;
		newArrivalTime += first.arrivalTime;
		first.serviceTime = generateServiceTime(averageServiceTime);
		list.add(first);
		readyQueue.add(first);
		first.completionTime = (first.arrivalTime + first.serviceTime);
		currentTime = first.completionTime;
		first.completed = true;
		turnaroundArr[eventsCompleted] = (first.completionTime - first.arrivalTime);
		list.remove();
		eventsCompleted++;
		
		
		for(int i = 1; i < 600; i++) {
			EventHRRN e = new EventHRRN();
			//newArrivalTime += generateArrivalTime(lambda);
			newArrivalTime += poissonRandomArrival(lambda);
			e.place = i;
			e.arrivalTime = newArrivalTime;
			e.serviceTime = generateServiceTime(averageServiceTime);
			e.completed = false;
			System.out.println("Creating Process " + i + " Arrival Time: " + e.arrivalTime + " Service Time: " + e.serviceTime);
			list.add(e);
			
		}
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
	
	
	
	public static void generateReport() {
		double total = 0;
		for(int j = 0; j < 501; j++) {
			total += turnaroundArr[j];
		}

		
		double row = calculateUtilization(lambda);
		double averageTurnaroundRate = (total / 500);
		double averageWaitingTime = calculateAverageWaitingTime(averageTurnaroundRate);
		double q = calculateQ(lambda, averageTurnaroundRate);
		double totalThroughput = 500 / currentTime;
		
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
	
	
	
	
	
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		lambda = sc.nextDouble();
		init();
		
		
		double total = 0;
		for(int i = 0; i < list.size();i++) {
			EventHRRN e = new EventHRRN();
			e = ((LinkedList<EventHRRN>) list).get(i);
			total += e.serviceTime;
		}
		
		double avg = total /600;
		System.out.println("Avg Service TIME: " + avg);
		
		
		runSim();
		generateReport();
			
		serverBusy = false;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
