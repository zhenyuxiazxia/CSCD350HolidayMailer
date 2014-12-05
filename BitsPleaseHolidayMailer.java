
public class BitsPleaseHolidayMailer {

	public static void main(String[] args) {
		JavaMailer jmailer = new JavaMailer();
		MusicPlayer player = new MusicPlayer();
		player.start();
		jmailer.sendMail();
	}

}