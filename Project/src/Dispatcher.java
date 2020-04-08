import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import jssc.SerialPortException;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Dispatcher implements Runnable, Subject  {

	SerialPortHandle sph; 
	ArrayList<Observer> proxies;
	HashMap<Byte, Observer> table = new HashMap<Byte, Observer>(); // list to keep track of proxies
	byte id;
	byte payload;

	Dispatcher() {
		proxies = new ArrayList<Observer>();//an array list of Observer
		sph = new SerialPortHandle("COM6");//open communication with the serial port
		new Thread(this).start();//call the run function
	}

	public byte decodeID(byte b) {
		return (byte) (b >> 7);//the id is the most significant bit
	}

	public byte decodePayLoad(byte b) {
		return (byte) (b & 0b00000111);//the last 3 bits are the message
	}

	public void send_msg(Proxy proxy, msg m) throws IOException {

		byte pl = decodePayLoad(m.value);
		sph.writeByte(pl);
	}
	@Override
	public void run() {
		while (true) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// read from serial port (bytes)
			byte b = 0;
			b = sph.readByte();
			// should read and decode the message
			id = decodeID(b); // return the id part of the message byte
			payload = decodePayLoad(b);
			notifyObserver();//will update the proxy the message is intended for
			}
		}
	public void registerObserver(Observer proxy) {
		// add a new proxy to your list of known proxies
		try {
			Debug.trace("Adding " + proxy + " to list "
					+ "of proxies known to " + this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Proxy p = (Proxy)proxy;
		table.put(p.getId(), p);
		proxies.add(p);
	}

	@Override
	public void notifyObserver() {
		// update the observer the message is indented for
		// proxy object with message
		 // return the reference to the corresponding proxy
		Proxy p;
		if (proxies.size() > 0) {
			// figure out the index of the proxy the message is intended for
			p = (Proxy) table.get(id);
			p.update(new msg(id, payload));
	}
}
	@Override
	public void removeObserver(Observer o) {
		int i = proxies.indexOf(o);
		if (i >= 0)
			proxies.remove(i);
	}
}
