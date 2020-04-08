import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {
		
		// Use the Debug class to see what is going on.
		// Will be saved in the project directory 
		// e.g. /Users/izualkernan/eclipse-workspace/Message-Oriented
		
		Debug.tracefile("debugtrace.txt");
		// Create a dispatcher 
		Dispatcher d = new Dispatcher();
		
		// Create one proxy object for each piece of end-node hardware 
		Proxy p1 = new Proxy(d, (byte)0);
		Proxy p2 = new Proxy(d, (byte)1);
		Game a = new Game(p1, p2);//create the game
	}
}


