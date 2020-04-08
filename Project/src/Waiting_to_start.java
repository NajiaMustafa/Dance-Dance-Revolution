import java.io.File;
import java.io.IOException;

public class Waiting_to_start implements State {

	@Override
	public void next(Game_Context vmc) {//check if player wants to play easy or hard. change to game_playing once an answer is received
		Music music = new Music();
		String path = new File("").getAbsolutePath() + "\\pacman_beginning.wav";
		char answer;
		boolean notstart = true;
		System.out.println("Would you like to play the easy or hard level? [e/h]");
		while (notstart) {
		try {
			if (System.in.available() > 0) {
				answer = (char) System.in.read();//read from the console
				if (answer == 'e') {
					vmc.m = new msg((byte) 0b00000000);//start message from JAVA to arduino
					vmc.score = new Score_easy();
					notstart = false;//break from the while loop
				}
				else if (answer == 'h') {
					vmc.m = new msg((byte) 0b00001000);//start message from JAVA to arduino
					vmc.score = new Score_hard();
					notstart = false;//break from the while loop
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		System.out.println("Game is starting");
		music.playmusic(path);//play game beginning music 
		vmc.setstate(new Game_Playing());
	}
	@Override
	public void prev(Game_Context vmc) {
		// TODO Auto-generated method stub
		
	}

}
