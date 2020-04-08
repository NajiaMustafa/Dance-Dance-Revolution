
public class Game_Context {
	public msg m;
	static byte Rightspot = 0b00000001;//right spot
	static byte Wrongspot = 0b00000010;//wrong spot
	static byte Hardhit = 0b00000011;//hard hit
	static byte Weakhit = 0b00000100;//weak hit
	static byte Error = 0b00000101;//error
	static byte Over = 0b00000110;//game over
	static byte Increment = 0b00000111;//increment score
	Score score;
	private State state = new Waiting_to_start();//wait for the game to start
	public void setstate(State c) {
		state = c;
	}
	public void next() {
		state.next(this);
	}
	public void prev() {
		state.prev(this);
	}
}
