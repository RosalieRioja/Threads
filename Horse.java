//
import java.util.concurrent.*;

public class Horse implements Runnable {
	private String name;
	private String warCry;
	private boolean isHealthy;
	private int speed;
	private final int DEFAULT_SPEED_RANGE = 10;
	private final int TURBO_SPEED_RANGE = 20;
	private final int BARN_GATE_DISTANCE = 10;
	private int steps = 0;
	private boolean winner = false;
	private CyclicBarrier barnToGate;
	private CyclicBarrier gateToFinish;

	public Horse() {
		setHealth();
	}

	public void setBarrier(CyclicBarrier barrier, CyclicBarrier barrier2) {
		barnToGate = barrier;
		gateToFinish = barrier2;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getWarCry() {
		return warCry;
	}
	public void setWarCry(String warCry) {
		this.warCry = warCry;
	}

	public boolean getHealth() {
		return isHealthy;
	}
	private void setHealth() {
		int randomNum = (int) (Math.random() * 2);	//true or false
		boolean result = false;

		if (randomNum == 0) {
			result = false;
		}
		if (randomNum == 1) {
			result = true;
		}
		//result = true;
		this.isHealthy = result;
	}

	public int getSpeed() {
		return speed;
	}
	public void setSpeed() {
		this.speed = ( (int) (Math.random() * DEFAULT_SPEED_RANGE) + 1 );
	}
	public void setSpeed(String lastHorse) {
		if(getName().equals(lastHorse)) {
			this.speed = ( (int) (Math.random() * TURBO_SPEED_RANGE) + 1 );	
			System.out.println(lastHorse + " is trailing.");		
		}
		else {
			this.speed = ( (int) (Math.random() * DEFAULT_SPEED_RANGE) + 1 );
		}
	}

	public int getSteps() {
		return steps;
	}
	public void setSteps(int num) {
		steps += num;
	}

	public boolean isWinner() {
		return winner;
	}
	public void setWinner() {
		winner = true;
	}

	///

	//@Override
	public void run() {
		try {
			walkToGate();
			setSteps((0-getSteps()));
			barnToGate.await();
			raceToFinish();
			gateToFinish.await();
		}
		catch(InterruptedException ex) {
			ex.printStackTrace();
		}
		catch(BrokenBarrierException ex) {
			ex.printStackTrace();
		}
	}

	private void walkToGate() {
		int steps = 0;
		
		while(steps < BARN_GATE_DISTANCE) {
			setSpeed();
			steps += getSpeed();
			System.out.println("At " + System.currentTimeMillis() + ", "
				+ Thread.currentThread().getName() + " took " + getSpeed() + " steps. "
				+ (BARN_GATE_DISTANCE - steps) + " to go.");
		}

		System.out.println(Thread.currentThread().getName() + " is now at the gate. ");
	}

	public void raceToFinish() {
		int finishLineDistance = Race.getFinishLineDistance();
		
		while(steps < finishLineDistance) { // && !Race.getWinner()) {
			//setSpeed();
			if(getSteps() != 0) {
				setSpeed(Race.getLastHorse());
			}
			else {
				setSpeed();
			}
			setSteps(getSpeed());
			System.out.println("At " + System.currentTimeMillis() + ", "
				+ Thread.currentThread().getName() + " took " + getSpeed() + " steps. "
				+ (finishLineDistance - getSteps()) + " to go.");
		}

		if(!Race.getWinner()) {
			setWinner();
			Race.setWinner();
		}
	}

}