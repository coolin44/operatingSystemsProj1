package eventScheduler;

import java.util.*; 

public class SRTF {
	
	public static Queue<ProcessSRTF> list;
	public static Queue<ProcessSRTF> readyQueue;
	public static double currentTime;
	public static boolean serverBusy;
	public static double averageServiceTime;
//	public static double lambda;
	public static int eventsCompleted;
	public static double turnaroundArr[];
	public static double totalEventsInQueue;
	public static int cycles;
	public static double timeSpentIdle;
	
	
	public static void init(double lambda){
		list = new LinkedList<ProcessSRTF>();
		readyQueue = new LinkedList<ProcessSRTF>();
		serverBusy = false;
		averageServiceTime = (double) 0.06;
		eventsCompleted = 0;
		turnaroundArr = new double [20000];
		generateEvents(lambda);
		totalEventsInQueue = 0;
		cycles = 0;
		timeSpentIdle = 0;
	}
	
	public static double generateArrivalTime(double lambda) {
		return (Math.log(Math.random())/-lambda);
	}
	
	public static double getNumberOfEventsInQueue() {
		return (readyQueue.size());
	}

	public static double generateServiceTime(double lambda) {
		double expRandom = (double) (-lambda * Math.log(Math.random())/Math.log(2));
		return expRandom;
	}

	
	public static void generateReadyQueue() {
		int i = 0;
		ProcessSRTF e = new ProcessSRTF();
		e = list.peek();
		while(e.arrivalTime <= currentTime) {
			e = ((LinkedList<ProcessSRTF>) list).get(i);
			if(!readyQueue.contains(e)) {
				readyQueue.add(e);
				list.remove(e);
			}
			i++;
			try {
			e = ((LinkedList<ProcessSRTF>) list).get(i);
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
/*	
	public static int findShortestTimeRemaining() {
		generateReadyQueue();
		int i = 0;
		ProcessSRTF shortestTimeRemaining = ((LinkedList<ProcessSRTF>) readyQueue).get(0);
		for(int j = 0; j < readyQueue.size(); j++) {
			ProcessSRTF eve = ((LinkedList<ProcessSRTF>) readyQueue).get(j);
			if(eve.remainingTime < shortestTimeRemaining.remainingTime) {
				shortestTimeRemaining = eve;
				i = j;
			}
		}
		return i;
	}
*/
/*	
	public static int binarySearch(Queue<ProcessSRTF> readyQueue, int l, int r, ProcessSRTF e) {
		
		
		if(r >= l) {
			int mid = l + (r - 1) / 2;
			if(e.remainingTime )
		}
	}
*/	
	
/*	
	public static int findShortestTimeRemaining() {
		generateReadyQueue();
		Collections.sort( (List<ProcessSRTF>) readyQueue);
		int i = 0;
		ProcessSRTF shortestTimeRemaining = ((LinkedList<ProcessSRTF>) readyQueue).get(0);
		for(int j = 0; j < readyQueue.size(); j++) {
			ProcessSRTF eve = ((LinkedList<ProcessSRTF>) readyQueue).get(j);
			if(eve.remainingTime < shortestTimeRemaining.remainingTime) {
				shortestTimeRemaining = eve;
				i = j;
			}
		}
		return i;
	}
*/	
	
	
	public static void runSim() {
		totalEventsInQueue = 0;
		while(eventsCompleted < 10001) {
			processEvent();
			totalEventsInQueue += getNumberOfEventsInQueue();
			cycles++;
		}
	}
/*	
	@SuppressWarnings("unchecked")
	public static void processEvent() {
		generateReadyQueue();
		Collections.sort( (List<ProcessSRTF>) readyQueue);
		ProcessSRTF e = readyQueue.peek();
		if(currentTime < e.arrivalTime) {
			timeSpentIdle += (e.arrivalTime - currentTime);
			currentTime = e.arrivalTime;
		}
		e.remainingTime = e.remainingTime- .00001;
		currentTime = currentTime + .00001;
		if(e.remainingTime <= 0) {			
			e.completed = true;
			e.completionTime = currentTime;
			turnaroundArr[eventsCompleted] = (e.completionTime - e.arrivalTime);
			System.out.println("Event " + e.place + " AT: " + e.arrivalTime + " ST: " + e.serviceTime + " CT: " + e.completionTime);
			readyQueue.remove();
			eventsCompleted++;
		}
	}
*/
	
	public static void scheduleDeparture() {
		readyQueue.remove();
		eventsCompleted++;
	}
	
	
	@SuppressWarnings("unchecked")
	public static void processEvent() {
		generateReadyQueue();
		Collections.sort( (List<ProcessSRTF>) readyQueue);
		serverBusy = true;
		ProcessSRTF e = readyQueue.peek();
		if(currentTime < e.arrivalTime) {
			timeSpentIdle += (e.arrivalTime - currentTime);
			currentTime = e.arrivalTime;
		}
		if(!checkIncomingProcesses(e)) {
			currentTime += e.remainingTime;
			e.completed = true;
			e.completionTime = currentTime;
			turnaroundArr[eventsCompleted] = (e.completionTime - e.arrivalTime);
			System.out.println("Event " + e.place + " AT: " + e.arrivalTime + " ST: " + e.serviceTime + " CT: " + e.completionTime);
			scheduleDeparture();
			//readyQueue.remove();
			//eventsCompleted++;
		}
		
	}
	
	public static boolean checkIncomingProcesses(ProcessSRTF current) {
		//used to increase the speed, otherwise its extremely slow searching the entire list
		int size;
		if(list.size() > 1000) {
			size = (int)(.3 * list.size());
		}
		else {
			size = list.size();
		}
		
		for(int i = 0; i < size; i++) {
			ProcessSRTF e = new ProcessSRTF();
			e = ((LinkedList<ProcessSRTF>) list).get(i);
			if(e.arrivalTime <= (currentTime + current.remainingTime) && e.remainingTime < (current.remainingTime - (e.arrivalTime - currentTime))){
				double timeSpentOnCurrentEvent = e.arrivalTime - currentTime;
				current.remainingTime = current.remainingTime - timeSpentOnCurrentEvent;
				currentTime = e.arrivalTime;
				return true;
			}
		}
		return false;
			
		}

	
	
	public static void generateEvents(double lambda) {
		double newArrivalTime = 0;
		ProcessSRTF first = new ProcessSRTF();
		first.arrivalTime = generateArrivalTime(lambda);
		first.place = 0;
		newArrivalTime += first.arrivalTime;
		first.serviceTime = generateServiceTime(averageServiceTime);
		first.remainingTime = first.serviceTime;
		currentTime = first.arrivalTime;
		readyQueue.add(first);
		for(int i = 1; i < 20000; i++) {
			ProcessSRTF e = new ProcessSRTF();
			newArrivalTime += generateArrivalTime(lambda);
			e.place = i;
			e.arrivalTime = newArrivalTime;
			e.serviceTime = generateServiceTime(averageServiceTime);
			e.remainingTime = e.serviceTime;
			e.completed = false;
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
		for(int j = 0; j < 10000; j++) {
			total += turnaroundArr[j];
		}

		double averageNumOfEventsInQueue = (double)(totalEventsInQueue / cycles);
		double averageTurnaroundRate = (total / 10000);
		double averageWaitingTime = calculateAverageWaitingTime(averageTurnaroundRate);
	//	double q = calculateQ(lambda, averageTurnaroundRate);
		double totalThroughput = 10000 / currentTime;
	//	double testRow = (lambda / (1/ averageServiceTime));
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
	//	System.out.println("TESTING ROW: " + testRow);
	//	System.out.println("AVERAGE NUMBER OF PROCESSES IN QUEUE: " + q);
		System.out.println("TEST Q WITH REAL ROW: " + testQ);
		System.out.println("AVERAGE WAITING TIME: " + averageWaitingTime);
		System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
	}	
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		double lambda = sc.nextDouble();
		init(lambda);
		
		
		double total = 0;
		for(int i = 0; i < list.size();i++) {
			ProcessSRTF e = new ProcessSRTF();
			e = ((LinkedList<ProcessSRTF>) list).get(i);
			total += e.serviceTime;
		}
		
		double avg = total /15000;
		
		
		runSim();
		generateReport();
		System.out.println("Avg Service TIME: " + avg);
		
		serverBusy = false;
		
	}
}