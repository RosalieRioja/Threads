//
import java.util.*;
import java.util.concurrent.*;

public class Race {
	private int noOfHorses;
	private static int noOfHealthyHorses;
	private final int BARN_GATE_DISTANCE = 10;
	private static int gateFinishlineDistance;
	private static Map<String, Horse> horseList;
	private static final int MIN_NO_HORSES = 3;
	private static boolean winner = false;
	private Thread[] horseThread;
	private CyclicBarrier barrier;
	private CyclicBarrier barrier2;

	public static void setWinner() {
		winner = true;
	}
	public static boolean getWinner() {
		return winner;
	}

	private void setFinishLineDistance(int num) {
		gateFinishlineDistance = num;
	}

	public static int getFinishLineDistance() {
		return gateFinishlineDistance;
	}

	public static int getMinHorses() {
		return MIN_NO_HORSES;
	}

	public static int getNoOfHealthyHorses() {
		return noOfHealthyHorses;
	}

	private void setNoOfHealthyHorses(int num) {
		noOfHealthyHorses = num;
	}

	///

	public static String getLastHorse() {
		String lastHorse = "";

		int lastStep = getFinishLineDistance();

		for(Horse horse : horseList.values()) {
			if (horse.getSteps() < lastStep && horse.getHealth()) {
				lastHorse = horse.getName();
				lastStep = horse.getSteps();
			}
			else if(horse.getSteps() == lastStep) {
				lastHorse = "";
			}
		}

		return lastHorse;
	}

	public Race() {
		horseList = new LinkedHashMap<String, Horse>();
	}

	private int getHorses() {
		int noOfHealthy = 0;
		String name = "";
		horseList.clear();

		noOfHorses = Validation.validNoOfHorses("\nEnter number of horses to race : ");

		for(int i = 0; i < noOfHorses; i++) {
			do {
				name = Validation.acceptString("\nGive name to horse " + (i+1) + " : ");

				if(horseList.get(name) != null) {
					System.out.println(name + " is already in the barn.");
				}
			} while(horseList.get(name) != null);

			Horse horse = new Horse();
			horse.setName(name);
			horse.setWarCry(Validation.acceptString("What is " + horse.getName() + "'s war cry : "));
			horseList.put(horse.getName(), horse);

			if(horse.getHealth()) {
				noOfHealthy++;
			}
		}

		return noOfHealthy;
	}

	private void getHealthyHorses() {
		boolean healthy = false;
		int healthyHorses = 0;

		while(!healthy) {
			healthyHorses = getHorses();

			if(healthyHorses <= 2) {
				System.out.println("\n" + healthyHorses + " horses are healthy.");
				System.out.println("At least 3 healthy horses are required to race.");
				System.out.println("Please enter new set of horses.");
				healthy = false;
			}
			else {
				healthy = true;
			}
		}

		setNoOfHealthyHorses(healthyHorses);
	}

	public void setupRace() {
		System.out.println("\nHealthy horses are walking to the gate.\n");

		horseThread = new Thread[getNoOfHealthyHorses()];
		int ctr = 0;

		barrier = new CyclicBarrier(getNoOfHealthyHorses(), new Runnable() {
			//@Override
			public void run() {
				System.out.println("\nAll healthy horses are at the gate.\n");
				System.out.println("RACE START\n");
			}
		});

		barrier2 = new CyclicBarrier(getNoOfHealthyHorses(), new Runnable() {
			//@Override
			public void run() {
				System.out.println("RACE END\n");
				printResults();
			}
		});

		System.out.println("");
		for(Horse horse : horseList.values()) {
			if(!horse.getHealth()) {
				System.out.println(horse.getName() + " is not healthy to race.");
			}
			else {
				horse.setBarrier(barrier, barrier2);
				horseThread[ctr] = new Thread(horse);
				horseThread[ctr].setName(horse.getName().toUpperCase());
				ctr++;
			}
		}
	}

	public void raceToFinish() {
		for(Thread thread : horseThread) {
			thread.start();
		}
	}

	public void printResults() {
		System.out.println("\nResults :\n");

		for (Horse horse : horseList.values()) {
			if(horse.getHealth()) {
				System.out.print(horse.getName().toUpperCase() + " traveled " + horse.getSteps() + ". ");

				if(horse.isWinner()) {
					System.out.println(horse.getName().toUpperCase() + " won the race. " + horse.getWarCry() + "!");
				}
				else {
					System.out.println((getFinishLineDistance() - horse.getSteps()) + " steps left to the finish line.");
				}
			}
			else {
				System.out.println(horse.getName() + " was not healthy to race.");
			}
		}
	}

	public static void main(String args[]) {
		Race race = new Race();
		
		race.getHealthyHorses();

		race.setFinishLineDistance(Validation.enterInteger("\nEnter distance between the gate and finish line : "));

		race.setupRace();

		race.raceToFinish();
	}

}