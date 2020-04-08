
public abstract class Score {//template pattern
	int score = 0;
	public void incrementScore() {
		scoreincrement();//primitive depends on whether easy mode or hard
		printscore();//print the score when the game is over
	}

	public void printscore() {
		System.out.print(score);
	};

	public abstract void scoreincrement();//will be defined by the children
}
