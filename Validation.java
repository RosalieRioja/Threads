//validation testBranch
import java.io.Console;

public class Validation {

	private static Console console = System.console();

	//validate input to integer only
	public static int enterInteger(String label) {

		int value = 0;
		boolean success = false;

		while(!success) {
			try {
				System.out.print(label);
				value = Integer.parseInt(console.readLine());

				if(value < 1) {
					success = false;
				}
				else {
					success = true;
				}
			}
			catch(NumberFormatException ex) {
				System.out.println("Please enter an integer.");
			}
		}

		return value;

	}//end enterInteger

	//validate input to approx. 3 characters
	public static String enter3Chars(String label) {
		String value = "";
		boolean success = false;

		while(!success) {
			System.out.print(label);
			value = console.readLine();

			if(value.length() == 3) {
				success = true;
			}
			else {
				System.out.println("Please enter 3 characters.");
			}
		}

		return value;
	}//end enter3Chars

	public static String acceptString(String label) {
		String value = "";
		boolean success = false;

		while(!success) {
			System.out.print(label);
			value = console.readLine();

			if(value.trim().isEmpty()) {
				System.out.println("Input cannot be empty.");
			}
			else {
				success = true;
			}
		}

		return value;
	}//end acceptString

	public static int validNoOfHorses(String label) {
		int value = 0;
		boolean success = false;

		while(!success) {
			value = enterInteger(label);

			if(value < Race.getMinHorses()) {
				System.out.println("No. of horses should be at least 3.");	
			}
			else {
				success = true;
			}
		}

		return value;
	}

	public static void main(String args[]) {
		//System.out.println("\n" + enter3Chars());
	}
}
