public class msg {
	
	byte value;
	byte ID;
	byte PayLoad;
	
	msg(byte val)
	{
		this.value=val;
	}
	msg(byte id, byte pl){
		this.ID = id;
		this.PayLoad = pl;
	}
	
	//returns id of the proxy sending the message
	public byte getID() {
		return ID;
	}

	//sets the id
	public void setID(byte iD) {
		ID = iD;
	}

	//returns the actual payload of the message
	public byte getPayLoad() {
		return PayLoad;
	}

	//sets the payload
	public void setPayLoad(byte payLoad) {
		PayLoad = payLoad;
	}
}
