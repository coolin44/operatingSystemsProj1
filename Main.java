package eventScheduler;

import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("1] FCFS");
		System.out.println("2] HRRN");
		System.out.println("3] SRTF");
		System.out.println("4] RR");
		System.out.println("Enter the scheduler you wish to use: ");
		int choice = sc.nextInt();
		
		switch(choice) {
			case 1: FCFS.main(args);
				break;
			case 2: HRRNext.main(args);
				break;
			case 3: SRTF.main(args);
				break;
			case 4: RR.main(args);
				break;
			default: System.out.println("ERROR...");
					 System.out.println("You must enter an integer between 1 and 4");
					
		}
	}
}
