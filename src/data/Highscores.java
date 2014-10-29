package data;

import java.io.Serializable;


public class Highscores implements Serializable {
	private static final long serialVersionUID = -6449573L;
	
	private String[] scores;

	public Highscores() {
		scores = new String[8];
		for(int i = 0; i < scores.length; i++)
			scores[i] = "59 59 999";
	}

	public boolean add(int numDisks, String newScore) {
		String oldScore = scores[numDisks - 2];

		if(compareTimes(oldScore, newScore) == -1) {
			scores[numDisks - 2] = newScore;
			return true;
		}
		else
			return false;
	}

	public int compareTimes(String oldTime, String newTime) {
		String[] oldScoreParts = oldTime.split(" ");
		String[] newScoreParts = newTime.split(" ");
		
		int oldMins = Integer.parseInt(oldScoreParts[0]);
		int oldSecs = Integer.parseInt(oldScoreParts[1]);
		int oldDecs = Integer.parseInt(oldScoreParts[2]);
		
		int newMins = Integer.parseInt(newScoreParts[0]);
		int newSecs = Integer.parseInt(newScoreParts[1]);
		int newDecs = Integer.parseInt(newScoreParts[2]);
		
		if(oldTime.equals(newTime))
			return 0;
		if(newMins > oldMins)
			return 1;
		else if(newMins == oldMins && newSecs > oldSecs)
			return 1;
		else if(newMins == oldMins && newSecs == oldSecs && newDecs > oldDecs)
			return 1;
		return -1;
	}

	public String get(int numDisks) {
		return scores[numDisks - 2];
	}
	
	@Override
	public String toString() {
		String toReturn = "";
		for(int i = 2; i <= 9; i++) {
			if(!get(i).equals("59 59 999"))
				toReturn += "  \t" + i + " Disks - " + get(i) + "\n\n";
			else
				toReturn += "  \t" + i + " Disks - " + "No best"+ "\n\n";
		}
		return toReturn;
	}
}