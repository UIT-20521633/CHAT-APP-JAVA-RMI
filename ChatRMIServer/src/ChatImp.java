import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import readFlie.readFile;
public class ChatImp extends UnicastRemoteObject implements iChat {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ChatImp() throws RemoteException {
		super();
	}
	
	List<Message> msgs = new ArrayList<>();
	List<String> users = new ArrayList<>();


	@Override
	public List<User> getUser() throws RemoteException {
	    return DatabaseConnection.getUser();
	}

	@Override
    public boolean register(String username, String password,String email) throws RemoteException {
        return DatabaseConnection.register(username, password,email);
    }

	public boolean login(String username, String password) throws RemoteException {
	    boolean isLoggedIn = DatabaseConnection.login(username, password);
	    if (isLoggedIn) {
	        DatabaseConnection.updateUserStatus(username, "online");
	    }
	    return isLoggedIn;
	}


//   
	public void Logout(String username) throws RemoteException {
	    DatabaseConnection.updateUserStatus(username, "offline");
	    users.remove(username);
	}

	@Override
	public void sendMsg(Message msg) throws RemoteException {
	    DatabaseConnection.saveMessage(msg);
	}

	@Override
	public List<Message> getAllMessage() throws RemoteException {
	    return DatabaseConnection.getAllMessages();
	}

	@Override
	public void sendFile(Message msg) throws RemoteException {
	    DatabaseConnection.saveMessage(msg);
	}

	@Override
	public void sendVoiceMessage(Message msg) throws RemoteException {
	    DatabaseConnection.saveMessage(msg);
	}

	@Override
	public void sendPrivateMessage(Message msg) throws RemoteException {
	    DatabaseConnection.saveMessage(msg);
	}

	@Override
	public void sendImage(Message msg) throws RemoteException {
	    DatabaseConnection.saveMessage(msg);
	}

	@Override
    public List<Message> getPrivateMessages(String sender, String receiver) throws RemoteException {
        List<Message> privateMessages = new ArrayList<>();
        for (Message msg : msgs) {
            if ((msg.getUsername().equals(sender) && msg.getReceiver().equals(receiver)) ||
                (msg.getUsername().equals(receiver) && msg.getReceiver().equals(sender))) {
                privateMessages.add(msg);
            }
        }
        return privateMessages;
    }




	
	
}
