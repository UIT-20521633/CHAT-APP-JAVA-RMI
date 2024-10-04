import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String msg;
	private Date date;
	private String type;
	private String receiver;
	private byte[] fileData; // Để lưu trữ ảnh, tệp, và tin nhắn thoại

	public Message(String username, String msg, Date date, String type, String receiver, byte[] fileData) {
		this.username = username;
		this.msg = msg;
		this.date = date;
		this.type = type;
		this.receiver = receiver;
		this.fileData= fileData;
	}

	// Default constructor
	public Message() {
		// TODO Auto-generated constructor stub
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
}
