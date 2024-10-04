import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtusername;
    private JPasswordField txtpassword;
    private iChat client;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // login frame = new login(client);
                    // frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public login(iChat client) {
        this.client = client;
        setForeground(Color.DARK_GRAY);
        setFont(new Font("Arial", Font.BOLD, 17));
        setTitle("CHAT RMI");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\DELL\\Downloads\\chat.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 975, 666);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        txtusername = new JTextField();
        txtusername.setBounds(591, 327, 252, 48);
        txtusername.setHorizontalAlignment(SwingConstants.CENTER);
        txtusername.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtusername.setBackground(new Color(147, 112, 219));
        contentPane.add(txtusername);
        txtusername.setColumns(10);
        
        txtpassword = new JPasswordField();
        txtpassword.setBounds(591, 407, 252, 48);
        txtpassword.setHorizontalAlignment(SwingConstants.CENTER);
        txtpassword.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtpassword.setBackground(new Color(147, 112, 219));
        contentPane.add(txtpassword);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(591, 479, 252, 35);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtusername.getText();
                String password = new String(txtpassword.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    try {
                        if (client.login(username, password)) {
                            setVisible(false);
                            ChatApp chatApp = new ChatApp(client, username); // Truyền tên người dùng
                            chatApp.updateUser(client.getUser());
                            chatApp.updateMessage(client.getAllMessage());
                            chatApp.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(login.this,
                                    "Invalid username or password. Please try again.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(login.this, "Please enter a username and password.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLogin.setForeground(UIManager.getColor("Button.disabledShadow"));
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 17));
        contentPane.add(btnLogin);

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setBorder(null);
        btnRegister.setBounds(806, 549, 105, 35);
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new register(client).setVisible(true);
            }
        });
        btnRegister.setForeground(new Color(255, 255, 255));
        btnRegister.setBackground(new Color(64, 0, 128));
        btnRegister.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(btnRegister);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(591, 296, 110, 26);
        lblUsername.setForeground(new Color(230, 230, 250));
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 18));
        contentPane.add(lblUsername);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(591, 382, 110, 26);
        lblPassword.setForeground(new Color(230, 230, 250));
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 18));
        contentPane.add(lblPassword);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(211, 168, 255));
        panel.setBounds(0, 0, 503, 629);
        contentPane.add(panel);
        panel.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Created By UIT-20521633");
        lblNewLabel.setForeground(new Color(0, 128, 255));
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(79, 446, 347, 38);
        panel.add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\ChatRMI_Client\\src\\icon\\chat_bg-240 (1) (2).png"));
        lblNewLabel_1.setBounds(0, 117, 503, 378);
        panel.add(lblNewLabel_1);
                
                JLabel lblDontHaveAn = new JLabel("Don't have an account?");
                lblDontHaveAn.setHorizontalAlignment(SwingConstants.CENTER);
                lblDontHaveAn.setForeground(new Color(230, 230, 250));
                lblDontHaveAn.setFont(new Font("Tahoma", Font.BOLD, 18));
                lblDontHaveAn.setBounds(572, 553, 252, 26);
                contentPane.add(lblDontHaveAn);
        
                JLabel lblLogin = new JLabel("LOGIN");
                lblLogin.setBounds(502, 0, 1002, 629);
                lblLogin.setIcon(new ImageIcon("C:\\Users\\DELL\\Downloads\\download (1).jpg"));
                lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 41));
                contentPane.add(lblLogin);
                
                JLabel label = new JLabel("New label");
                label.setBounds(669, 90, 45, 13);
                contentPane.add(label);
        
       
    }
}
