import java.io.File;

public class Game_Playing implements State {

	@Override
	public void next(Game_Context vmc) {//change state only when game over or error if not stay in game_playing state
		Music m = new Music();
		String path = new File("").getAbsolutePath() + "\\pacman_death.wav";//music played when game is over
		// decode the payload message sent by the arduino and change the states based on them
		if (vmc.m.PayLoad == Game_Context.Rightspot) {
			System.out.println("Right spot was hit");
		} 
		else if (vmc.m.PayLoad == Game_Context.Wrongspot) {
			System.out.println("Wrong or no spot was hit");
		} 
		else if (vmc.m.PayLoad == Game_Context.Hardhit) {
			System.out.println("Hard Hit");
		} 
		else if (vmc.m.PayLoad == Game_Context.Weakhit) {
			System.out.println("Weak Hit");
		} 
		else if (vmc.m.PayLoad == Game_Context.Error) {
			System.out.println("Error State");
			m.playmusic(path);
			System.out.println("Total Score = ");// game over so print the score
			vmc.score.printscore();
			vmc.setstate(new End_Game());//change state to end game
			vmc.next();//go to end game state
		} 
		else if (vmc.m.PayLoad == Game_Context.Over) {
			System.out.println("Game Over");
			m.playmusic(path);
			System.out.println("Total Score = ");// game over so print the score
			vmc.score.printscore();
			vmc.setstate(new End_Game());//change state to end game
			vmc.next();//go to end game state
		} 
		else if (vmc.m.PayLoad == Game_Context.Increment) {
			vmc.score.incrementScore();//increment score
		}
	}
	@Override
	public void prev(Game_Context vmc) {
		// TODO Auto-generated method stub

	}
}
