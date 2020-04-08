import java.io.IOException;

public class Game implements Observer {
	Game_Context a;
	Proxy p;
	Proxy n;
	Game(Proxy d, Proxy e){//two devices so two proxies
		a = new Game_Context();//declaring a game context 
		p = d;
		n = e;
		p.registerObserver(this);//register the game with the proxies
		n.registerObserver(this);
		startgame();//check if the player wants to start a game
	}
	@Override
	public void update(msg msg) {
		a.m = msg;
		a.next();//update the game_context in the gameplaying state
	}
	public void startgame() {//check if the player wants to start a game if yes ask one of the proxies to send a message
		a.next();
		try {
			p.send_msg(a.m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
