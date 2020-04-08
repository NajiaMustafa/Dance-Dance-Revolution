
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialPortHandle {

    SerialPort sp;
    String path;

    public SerialPortHandle(String path) {
        super();
        this.sp = new SerialPort(path);
        ;
        this.path = path;
        try {
            sp.openPort();
            sp.setParams(9600, 8, 1, 0);
        } catch (SerialPortException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// Open serial port

    }

    public byte readByte() {
        byte[] buff = null;
        try
        {
        	buff = sp.readBytes(1);	//read 1 byte
        }
        catch (SerialPortException e) {
        	e.printStackTrace();
        }
        return buff[0];
    }
    public void writeByte(byte a) {
    	try {
    		sp.writeByte(a); //write to the Serial Monitor
    	}
    	catch(SerialPortException e) {
        	e.printStackTrace();
        }
    }
}