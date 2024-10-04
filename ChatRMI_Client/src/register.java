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

public class register extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtEmail;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private iChat client;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // register frame = new register(client);
                    // frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public register(iChat client) {
        this.client = client;
        setForeground(Color.DARK_GRAY);
        setFont(new Font("Arial", Font.BOLD, 17));
        setTitle("REGISTER - CHAT RMI");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\DELL\\Downloads\\chat.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 476, 737);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        txtEmail = new JTextField();
        txtEmail.setHorizontalAlignment(SwingConstants.CENTER);
        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtEmail.setBackground(new Color(147, 112, 219));
        txtEmail.setBounds(89, 266, 252, 48);
        contentPane.add(txtEmail);
        txtEmail.setColumns(10);

        txtUsername = new JTextField();
        txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
        txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtUsername.setBackground(new Color(147, 112, 219));
        txtUsername.setBounds(89, 346, 252, 48);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);

        txtPassword = new JPasswordField();
        txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
        txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtPassword.setBackground(new Color(147, 112, 219));
        txtPassword.setBounds(89, 426, 252, 48);
        contentPane.add(txtPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setHorizontalAlignment(SwingConstants.CENTER);
        txtConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 25));
        txtConfirmPassword.setBackground(new Color(147, 112, 219));
        txtConfirmPassword.setBounds(89, 506, 252, 48);
        contentPane.add(txtConfirmPassword);

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());

                if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                    if (password.equals(confirmPassword)) {
                        try {
                            if (client.register(username, password, email)) {
                                JOptionPane.showMessageDialog(register.this,
                                        "Registration successful. You can now log in.", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                                setVisible(false);
                                new login(client).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(register.this,
                                        "Registration failed. Username might already be taken.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(register.this,
                                "Passwords do not match. Please try again.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(register.this, "Please fill in all fields.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnRegister.setForeground(UIManager.getColor("Button.disabledShadow"));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setFont(new Font("Tahoma", Font.BOLD, 17));
        btnRegister.setBounds(141, 580, 155, 35);
        contentPane.add(btnRegister);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new login(client).setVisible(true);
            }
        });
        btnLogin.setForeground(UIManager.getColor("Button.disabledShadow"));
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 17));
        btnLogin.setBounds(141, 630, 155, 35);
        contentPane.add(btnLogin);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setForeground(new Color(230, 230, 250));
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblEmail.setBounds(89, 232, 110, 26);
        contentPane.add(lblEmail);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(new Color(230, 230, 250));
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblUsername.setBounds(89, 312, 110, 26);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(new Color(230, 230, 250));
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblPassword.setBounds(89, 392, 110, 26);
        contentPane.add(lblPassword);

        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setForeground(new Color(230, 230, 250));
        lblConfirmPassword.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblConfirmPassword.setBounds(89, 472, 170, 26);
        contentPane.add(lblConfirmPassword);

        JLabel lblRegister = new JLabel("");
        lblRegister.setIcon(new ImageIcon("C:\\Users\\DELL\\Downloads\\download (1).jpg"));
        lblRegister.setFont(new Font("Tahoma", Font.PLAIN, 41));
        lblRegister.setBounds(0, 0, 469, 700);
        contentPane.add(lblRegister);
    }
}
