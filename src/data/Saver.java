package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Saver {
	private String path = "";

	public Saver() {
		setPath();
	}

	private boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0); 
	}

	private boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("mac") >= 0); 
	}

	private void setPath() {
		String username = System.getProperty("user.name");
		if(isMac()) {
			//path = "/Users/" + username + "/Library/Application Support/TowerOfHanoi/";
			path = "/Users/" + username + "/Public/TowerOfHanoi/";
		}
		else if(isWindows()) {
			path = "/Users/" + username + "/AppData/Roaming/TowerOfHanoi/";
		}
	}

	public void saveGame(Highscores scores) {
		if(!saveDirExists())
			createSaveDir();
		try {
			FileOutputStream fileOut = new FileOutputStream(path + "Highscores.hsd");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(scores);
			out.close();
			fileOut.close();
		}
		catch(IOException ioe) {System.err.println("IOException saving 1"); ioe.printStackTrace();}
	}

	public Highscores getSavedGame() {
		if(!saveDirExists()) {
			createSaveDir();
			Highscores newHS = new Highscores();
			saveGame(newHS);
			return newHS;
		}
		else if(saveDirExists() && !savedGameExists()) {
			Highscores newHS = new Highscores();
			saveGame(newHS);
			return newHS;
		}
		
		try {
			FileInputStream fileIn = new FileInputStream(path + "Highscores.hsd");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Highscores hsd = (Highscores) in.readObject();
			in.close();
			return hsd;
		}
		catch(IOException ioe) {System.err.println("IOException saving 2"); ioe.printStackTrace();}
		catch(ClassNotFoundException cnfe) {System.err.println("ClassNotFoundException saving");}
		return null;
	}

	public boolean savedGameExists() {
		if(!saveDirExists())
			createSaveDir();
		File[] files = new File(path).listFiles();
		for(File f : files)
			if(f.getName().equals("Highscores.hsd"))
				return true;
		return false;
	}

	private void createSaveDir() {
		File saveDir = new File(path);
		saveDir.mkdir();
	}

	private boolean saveDirExists() {
		File saveDir = new File(path);
		return saveDir.exists();
	}
}
