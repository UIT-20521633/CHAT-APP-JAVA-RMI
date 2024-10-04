import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class client {
 public static void main(String[] args) {
	 try {	
		 //Tìm kiếm thanh ghi ở server dựa trên IP và Port 
         Registry reg = LocateRegistry.getRegistry("localhost", 9876);
         //Lấy ra obj đã đăng ký ớ server bằng lookup dựa vào name đã đăng ký ở server 
         iChat ichat = (iChat) reg.lookup("chat");
         new login(ichat).setVisible(true); 
         // Truyền ichat vào login frame
         
     } catch (Exception e) {
         e.printStackTrace();
     }
}
}
