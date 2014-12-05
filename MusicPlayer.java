import javazoom.jl.player.Player;
import java.io.*;
import java.util.*;
public class MusicPlayer extends Thread{
	
	public void run()
	{
		while (true){
			try{
			    FileInputStream fis = new FileInputStream("JingleBellsRock.mp3");
			    Player playMP3 = new Player(fis);
			    playMP3.play();
			}
			catch(Exception exc){
			    exc.printStackTrace();
			    System.out.println("Failed to play the file.");
			}
		}
	}
}
 