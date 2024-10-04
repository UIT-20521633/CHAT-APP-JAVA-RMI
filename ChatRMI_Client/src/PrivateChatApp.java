import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class PrivateChatApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtSend;
    private JList<Object> txtRead;
    private DefaultListModel<Object> docMessage;
    private iChat client;
    private String username;
    private String receiver;

    public PrivateChatApp(iChat client, String username, String receiver) {
        this.client = client;
        this.username = username;
        this.receiver = receiver;

        setTitle("Private Chat with " + receiver);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        docMessage = new DefaultListModel<>();
        txtRead = new JList<>(docMessage);
        JScrollPane scrollPane = new JScrollPane(txtRead);
        scrollPane.setBounds(10, 11, 564, 282);
        contentPane.add(scrollPane);

        txtSend = new JTextField();
        txtSend.setBounds(10, 304, 465, 46);
        contentPane.add(txtSend);
        txtSend.setColumns(10);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        btnSend.setBounds(485, 304, 89, 46);
        contentPane.add(btnSend);

        // Cập nhật tin nhắn từ người nhận
        updateMessageList();
    }

    private void sendMessage() {
        Message msg = new Message();
        msg.setUsername(username);
        msg.setDate(new Date());
        msg.setMsg(txtSend.getText());
        msg.setType("text");
        msg.setReceiver(receiver);
        txtSend.setText("");
        try {
            client.sendPrivateMessage(msg);
            updateMessageList(); // Cập nhật lại danh sách tin nhắn sau khi gửi
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateMessageList() {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Message> allMessages = client.getPrivateMessages(username, receiver);
                docMessage.clear();
                for (Message message : allMessages) {
                    String formattedMessage = message.getDate() + "  " + message.getUsername() + ": " + message.getMsg();
                    docMessage.addElement(formattedMessage);
                }
                txtRead.revalidate();
                txtRead.repaint();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
