package eventScheduler;

import java.util.*; 

public class RR {
	
	public static Queue<ProcessRR> list;
	public static Queue<ProcessRR> readyQueue;
	public static double newArrivalTime;
	public static double currentTime;
	public static boolean serverBusy;
	public static double averageServiceTime;
	public static double lambda;
	public static int eventsCompleted;
	public static double turnaroundArr[];
	public static double totalEventsInQueue;
	public static double timeSpentOnCurrentEvent;
	public static int cycles;
	public static double timeSpentIdle;
	public static double quantum;
	
	
	public static void init(){
		list = new LinkedList<ProcessRR>();
		readyQueue = new LinkedList<ProcessRR>();
		newArrivalTime = 0;
		serverBusy = false;
		averageServiceTime = (double) 0.042;
		eventsCompleted = 0;
		turnaroundArr = new double [20000];
		totalEventsInQueue = 0;
		timeSpentOnCurrentEvent = 0;
		cycles = 0;
		timeSpentIdle = 0;
		quantum = .02;
		generateProcesses();
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
		ProcessRR e = new ProcessRR();
		e = list.peek();
		while(e.arrivalTime <= currentTime) {
			e = ((LinkedList<ProcessRR>) list).get(i);
			if(!readyQueue.contains(e)) {
				readyQueue.add(e);
				list.remove(e);
			}
			i++;
			try {
			e = ((LinkedList<ProcessRR>) list).get(i);
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

	public static void runSim() {
		totalEventsInQueue = 0;
		while(eventsCompleted < 10000) {
			processEvent();
			totalEventsInQueue += getNumberOfEventsInQueue();
			cycles++;
		}
	}
	
/*	
	public static void processEvent() {
		generateReadyQueue();
		//Collections.sort( (List<ProcessRR>) readyQueue);
		ProcessRR e = readyQueue.peek();
		if(currentTime < e.arrivalTime) {
			timeSpentIdle += (e.arrivalTime - currentTime);
			currentTime = e.arrivalTime;
		}
		e.remainingTime = e.remainingTime- .0001;
		currentTime = currentTime + .0001;
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
		//need to remove the top two events, 1 is the process and one is the departure
		readyQueue.remove();
		readyQueue.remove();
	}
	
	public static void processEvent() {
		generateReadyQueue();
		ProcessRR p = readyQueue.poll();
		if(currentTime < p.arrivalTime) {
			timeSpentIdle += (p.arrivalTime - currentTime);
			currentTime = p.arrivalTime;
		}
		if (p.remainingTime <= quantum) {
			currentTime += p.remainingTime;
			p.completionTime = currentTime;
			p.completed = true;
			turnaroundArr[eventsCompleted] = (p.completionTime - p.arrivalTime);
			eventsCompleted++;
			System.out.println("Process " + p.place + " AT: " + p.arrivalTime + " ST: " + p.serviceTime + " CT: " + p.completionTime);
		}
		else {
			p.remainingTime = p.remainingTime - quantum;
			currentTime += quantum;
			readyQueue.add(p);
		}
	}
	
	
	public static void generateProcesses() {
		ProcessRR first = new ProcessRR();
		first.arrivalTime = generateArrivalTime(lambda);
		first.place = 0;
		newArrivalTime += first.arrivalTime;
		first.serviceTime = generateServiceTime(averageServiceTime);
		first.remainingTime = first.serviceTime;
		currentTime = first.arrivalTime;
		readyQueue.add(first);
		for(int i = 1; i < 20000; i++) {
			ProcessRR e = new ProcessRR();
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
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter lambda(average arrival rate): ");
		lambda = sc.nextDouble();
		init();
		
		
		double total = 0;
		for(int i = 0; i < list.size();i++) {
			ProcessRR e = new ProcessRR();
			e = ((LinkedList<ProcessRR>) list).get(i);
			total += e.serviceTime;
		}
		
		double avg = total /20000;
		
		
		runSim();
		generateReport();
		System.out.println("Avg Service TIME: " + avg);
		
		serverBusy = false;
		
	}
}
