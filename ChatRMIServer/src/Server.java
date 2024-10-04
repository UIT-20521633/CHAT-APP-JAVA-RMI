import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import readFlie.readFile;


public class Server {

	public static int port=9876;
	//Khởi tạo 1 thanh ghi mới
	public static Registry reg;
	public static void main(String[] args) throws RemoteException {
		try {
			
			try{
				//tạo ra thanh ghi ở server với port tùy chọn
				reg = LocateRegistry.createRegistry(port);
				System.out.println("RMI Server ready");

				
			}
			catch(RemoteException e) {
				e.printStackTrace();
			}
			//Tạo Chat
			iChat chatSkeleton = new ChatImp();
			//Đăng ký object cho thanh ghi
			//rebind vào registry nghĩa là thêm "chat" vào bảng đăng ký để client có thể lookup 
			reg.rebind("chat", chatSkeleton);
			System.out.println("Group Chat RMI Server is running...!");
            System.out.println("RMI Registry created successfully on port "+port);
            System.out.println("============================================================");

		} catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: RemoteException occurred");
           
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: Exception occurred");
        }
	} 
	
}
