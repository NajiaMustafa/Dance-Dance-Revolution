import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Proxy implements Runnable, Observer, Subject {
	
	//InputStream in;
	//OutputStream out;
	Dispatcher d;
	byte id;
	ArrayList<Observer> Games;
	msg message;//save the message gotten from the dispatcher here to update the game thread
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	Proxy(Dispatcher d, byte i) throws IOException{
		
		this.d = d;
		this.id = i;
		Games = new ArrayList<Observer>();
		//register yourself as a proxy with the dispatcher
		d.registerObserver(this);//register the proxy with the dispatcher
		
		//start your thread
		new Thread(this).start();
	}
	void send_msg(msg m) throws IOException {
	   // tell the dispatcher to send your message
       d.send_msg(this, m);
	}
	
	public void run() {}
	public void call_back(msg msg) {
	    message = msg;
	    notifyObserver();//notify game thread a message was recieved
	    
	}
	public void update(msg msg) {
		call_back(msg);
	}
	@Override
	public void registerObserver(Observer o) {
		Game p = (Game) o;
		Games.add(p);
	}
	@Override
	public void removeObserver(Observer o) {
		int i = Games.indexOf(o);
		if (i >= 0)
			Games.remove(i);
		
	}
	@Override
	public void notifyObserver() {
		for (int i = 0; i < Games.size(); i++) {
			Observer o = Games.get(i);
			o.update(message);//update the Game thread
		}
	}
}
