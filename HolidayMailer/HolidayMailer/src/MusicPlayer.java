import javazoom.jl.player.Player;
import java.io.*;
import java.util.*;
public class MusicPlayer extends Thread
{
	private Player playMP3;
	private boolean play=true;
	public void run()
	{
		while (play)
		{
			try{
			    FileInputStream fis = new FileInputStream("JingleBellsRock.mp3");
			    playMP3 = new Player(fis);
			    playMP3.play();
			}
			catch(Exception exc)
			{
			    exc.printStackTrace();
			    System.out.println("Failed to play the file.");
			}
		}
	}
	public void kill()
	{
		play=false;
		playMP3.close();
	}
	
	public boolean getPlay()
	{
		return play;
	}
}
 