package towerSolver;

import java.util.Scanner;

public class TowersSolver {
	private static String track = "";

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Num plates: ");
		int numPlates = in.nextInt();
		in.nextLine();

		getOrder(numPlates, 'A', 'C', 'B');
		System.out.println(track);
	}

	private static void getOrder(int numPlates, char from, char to, char other) {
		if(numPlates == 1)
			track += from + "-" + to + " ";
		else {
			getOrder(numPlates - 1, from, other, to);
			track += from + "-" + to + " ";
			getOrder(numPlates - 1, other, to, from);
		}
	}
}
