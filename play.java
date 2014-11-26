import Player*;
import java.io.*;

public class play{
public static void main(String[] args)
{
try{
    FileInputStream fis = new FileInputStream("music.mp3");
    Player playMP3 = new Player(fis);
    playMP3.play();
}
catch(Exception exc){
    exc.printStackTrace();
    System.out.println("Failed to play the file.");
}
}
}