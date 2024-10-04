import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
//extend interface remote
//Định nghĩa các phương thức
public interface iChat extends Remote{
//	public Boolean Login(String username) throws RemoteException;
	public void Logout(String username) throws RemoteException;
    public void sendMsg(Message msg) throws RemoteException;
    public List<Message> getAllMessage() throws RemoteException;
    public List<User> getUser() throws RemoteException;
    boolean register(String username, String password, String email) throws RemoteException;
    boolean login(String username, String password) throws RemoteException;
    void sendFile(Message msg) throws RemoteException;
    void sendVoiceMessage(Message msg) throws RemoteException;
    void sendPrivateMessage(Message msg) throws RemoteException;
    void sendImage(Message msg) throws RemoteException;  
    List<Message> getPrivateMessages(String sender, String receiver) throws RemoteException;
	
}