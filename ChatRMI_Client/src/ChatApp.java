import javax.sound.sampled.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatApp extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtSend;
    public iChat client;
    private JList<String> txtListuser;
    private DefaultListModel<Object> docMessage;  // Thay đổi kiểu của DefaultListModel để chấp nhận các loại đối tượng khác nhau
    private DefaultListModel<String> modelList;
    private JLabel lblUsername;
    private volatile boolean updateNeeded = false;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private String username;
    private JTextPane txtRead;  // Đổi từ JList sang JTextPane

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Registry reg = LocateRegistry.getRegistry("localhost", 9876);
                iChat ichat = (iChat) reg.lookup("chat");
                ChatApp frame = new ChatApp(ichat, "UNKNOWN");
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ChatApp(iChat client, String username) throws RemoteException {
        this.client = client;
        this.username = username;

        setForeground(Color.WHITE);
        setFont(new Font("Dialog", Font.PLAIN, 16));
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\DELL\\Downloads\\chat.png"));
        setTitle(username.toUpperCase() + " CONSOLE");
        setResizable(false);

        setBounds(100, 100, 800, 700);
        contentPane = new JPanel();
        contentPane.setForeground(new Color(255, 255, 255));
        contentPane.setBackground(new Color(64, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        modelList = new DefaultListModel<>();
        txtListuser = new JList<>(modelList);
        txtListuser.setFont(new Font("Arial", Font.BOLD, 20));
        txtListuser.setBounds(30, 55, 155, 587);
        contentPane.add(txtListuser);

        JLabel lblNewLabel_1 = new JLabel("List Users");
        lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\ChatRMI_Client\\src\\icon\\User.png"));
        lblNewLabel_1.setBackground(Color.BLACK);
        lblNewLabel_1.setForeground(Color.BLACK);
        lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 15));
        lblNewLabel_1.setBounds(30, 10, 155, 43);
        contentPane.add(lblNewLabel_1);

        lblUsername = new JLabel(username.toUpperCase());
        lblUsername.setIcon(new ImageIcon("C:\\Users\\DELL\\Downloads\\icons8-avatar-40 (1).png"));
        lblUsername.setForeground(new Color(255, 255, 255));
        lblUsername.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 21));
        lblUsername.setBounds(213, 9, 193, 39);
        contentPane.add(lblUsername);

        JButton btnLogout = new JButton("");
        btnLogout.setBorder(null);
        btnLogout.setIcon(new ImageIcon("C:\\Users\\DELL\\Downloads\\icons8-logout-40.png"));
        btnLogout.addActionListener(e -> {
            try {
                int confirmed = JOptionPane.showConfirmDialog(ChatApp.this, "Do you want to log out the program?",
                        "Logout Confirmation", JOptionPane.OK_CANCEL_OPTION);
                if (confirmed == JOptionPane.OK_OPTION) {
                    client.Logout(username);
                    updateUserList();
                    setVisible(false);
                    new login(client).setVisible(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnLogout.setBackground(new Color(64, 128, 128));
        btnLogout.setForeground(UIManager.getColor("Button.disabledForeground"));
        btnLogout.setFont(new Font("Times New Roman", Font.BOLD, 17));
        btnLogout.setBounds(711, 10, 88, 43);
        contentPane.add(btnLogout);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(ChatApp.this, "Do you want to exit the program?",
                        "Exit Confirmation", JOptionPane.OK_CANCEL_OPTION);
                if (confirmed == JOptionPane.OK_OPTION) {
                    try {
                        client.Logout(username);
                        updateUserList();
                        System.exit(0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        docMessage = new DefaultListModel<>();
        txtRead = new JTextPane(); // Đổi từ JList sang JTextPane
        txtRead.setFont(new Font("Tahoma", Font.PLAIN, 13));
        txtRead.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtRead);
        scrollPane.setBounds(213, 55, 560, 534);
        contentPane.add(scrollPane);
        
        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setBackground(new Color(255, 255, 255));
        panel.setBounds(213, 599, 563, 43);
        contentPane.add(panel);
                panel.setLayout(null);
                
                        JButton btnSendImage = new JButton("");
                        btnSendImage.setBorder(null);
                        btnSendImage.setBounds(0, 0, 49, 43);
                        panel.add(btnSendImage);
                        btnSendImage.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\ChatRMI_Client\\src\\icon\\icons8-photo-gallery-40.png"));
                        btnSendImage.addActionListener(e -> sendImage());
                        btnSendImage.setBackground(new Color(255, 255, 255));
                        btnSendImage.setForeground(new Color(255, 255, 255));
                        btnSendImage.setFont(new Font("Times New Roman", Font.BOLD, 17));
                        
                                JButton btnSend = new JButton("");
                                btnSend.setBorder(null);
                                btnSend.setBounds(497, 0, 66, 43);
                                panel.add(btnSend);
                                btnSend.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\ChatRMI_Client\\src\\icon\\icons8-send-40.png"));
                                btnSend.addActionListener(e -> sendMessage());
                                btnSend.setBackground(new Color(255, 255, 255));
                                btnSend.setForeground(UIManager.getColor("Button.disabledForeground"));
                                btnSend.setFont(new Font("Times New Roman", Font.BOLD, 17));
                                
                                        JButton btnSendFile = new JButton("");
                                        btnSendFile.setBorder(null);
                                        btnSendFile.setBounds(99, 0, 49, 43);
                                        panel.add(btnSendFile);
                                        btnSendFile.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\ChatRMI_Client\\src\\icon\\icons8-attach-40.png"));
                                        btnSendFile.addActionListener(e -> sendFile());
                                        btnSendFile.setBackground(new Color(255, 255, 255));
                                        btnSendFile.setForeground(new Color(255, 255, 255));
                                        btnSendFile.setFont(new Font("Times New Roman", Font.BOLD, 17));
                                        
                                                JButton btnRecordVoice = new JButton("");
                                                btnRecordVoice.setBorder(null);
                                                btnRecordVoice.setBounds(46, 0, 56, 43);
                                                panel.add(btnRecordVoice);
                                                btnRecordVoice.setIcon(new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\ChatRMI_Client\\src\\icon\\icons8-voice-40.png"));
                                                btnRecordVoice.addActionListener(e -> recordAndSendVoiceMessage());
                                                btnRecordVoice.setBackground(new Color(255, 255, 255));
                                                btnRecordVoice.setForeground(new Color(255, 255, 255));
                                                btnRecordVoice.setFont(new Font("Times New Roman", Font.BOLD, 17));
                                                
                                                        txtSend = new JTextField();
                                                        txtSend.setToolTipText("");
                                                        txtSend.setFont(new Font("Arial", Font.PLAIN, 16));
                                                        txtSend.setBorder(null);
                                                        txtSend.setBounds(158, 0, 336, 43);
                                                        panel.add(txtSend);
                                                        txtSend.setColumns(10);
        txtSend.addActionListener(e -> sendMessage());

        updateUserList();
        updateMessageList();
        addPrivateMessageButton();

        executor.submit(() -> {
            try {
                while (true) {
                    if (checkForNewUsers() || checkForNewMessages()) {
                        setUpdateNeeded(true);
                        updateUIIfNeeded();
                    }
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    private synchronized void setUpdateNeeded(boolean updateNeeded) {
        this.updateNeeded = updateNeeded;
    }

    private synchronized boolean isUpdateNeeded() {
        return updateNeeded;
    }

    private synchronized void updateUIIfNeeded() throws RemoteException {
        if (isUpdateNeeded()) {
            SwingUtilities.invokeLater(() -> {
                try {
                    updateUserList();
                    updateMessageList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            setUpdateNeeded(false);
        }
    }

    private synchronized boolean checkForNewUsers() throws RemoteException {
        int size = modelList.size();
        String[] currentUsers = new String[size];
        for (int i = 0; i < size; i++) {
            currentUsers[i] = modelList.get(i);
        }
        List<User> newUsers = client.getUser();
        String[] newUsernames = new String[newUsers.size()];
        for (int i = 0; i < newUsers.size(); i++) {
            newUsernames[i] = newUsers.get(i).getUsername();
        }
        return !Arrays.equals(currentUsers, newUsernames);
    }


    private synchronized boolean checkForNewMessages() throws RemoteException {
        int currentMessageCount = docMessage.getSize();
        int newMessageCount = client.getAllMessage().size();
        return newMessageCount > currentMessageCount;
    }
    
  
    private int lastMessageCount = 0; // Biến để lưu số lượng tin nhắn đã hiển thị

    public synchronized void updateMessageList() throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Message> allMessages = client.getAllMessage();
                int newMessageCount = allMessages.size();

                // Kiểm tra xem có tin nhắn mới không
                if (newMessageCount > lastMessageCount) {
                    StyledDocument doc = txtRead.getStyledDocument();

                    // Kiểm tra vị trí thanh cuộn trước khi cập nhật
                    JScrollBar verticalScrollBar = ((JScrollPane) txtRead.getParent().getParent()).getVerticalScrollBar();
                    boolean scrollToBottom = verticalScrollBar.getValue() + verticalScrollBar.getVisibleAmount() == verticalScrollBar.getMaximum();

                    SimpleAttributeSet leftAlign = new SimpleAttributeSet();
                    StyleConstants.setAlignment(leftAlign, StyleConstants.ALIGN_LEFT);

                    for (int i = lastMessageCount; i < newMessageCount; i++) {
                        Message message = allMessages.get(i);
                        if (message.getType().equals("text")) {
                            String formattedMessage = message.getDate() + "  " + message.getUsername() + ": " + message.getMsg();
                            try {
                                doc.insertString(doc.getLength(), formattedMessage + "\n", null);
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                        } else if (message.getType().equals("image")) {
                            BufferedImage img = ImageIO.read(new ByteArrayInputStream(message.getFileData()));
                            if (img != null) {
                                BufferedImage resizedImg = resizeImage(img, 200, 200); // Điều chỉnh kích thước hình ảnh
                                StyleConstants.setIcon(leftAlign, new ImageIcon(resizedImg));
                                try {
                                    doc.insertString(doc.getLength(), message.getDate() + "  " + message.getUsername() + ": ", null);
                                    doc.insertString(doc.getLength(), "\n", leftAlign);
                                } catch (BadLocationException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String errorMessage = message.getDate() + "  " + message.getUsername() + ": Sent an image (error loading image)";
                                try {
                                    doc.insertString(doc.getLength(), errorMessage + "\n", null);
                                } catch (BadLocationException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (message.getType().equals("file")) {
                            JButton fileButton = new JButton("Open " + message.getMsg());
                            fileButton.addActionListener(e -> openFile(message.getFileData(), message.getMsg()));
                            try {
                                doc.insertString(doc.getLength(), message.getDate() + "  " + message.getUsername() + ": ", null);
                                txtRead.insertComponent(fileButton);
                                doc.insertString(doc.getLength(), "\n", null);
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                        } else if (message.getType().equals("voice")) {
                            JButton playButton = new JButton("Play Voice Message");
                            playButton.addActionListener(e -> playAudio(message.getFileData()));
                            try {
                                doc.insertString(doc.getLength(), message.getDate() + "  " + message.getUsername() + ": ", null);
                                txtRead.insertComponent(playButton);
                                doc.insertString(doc.getLength(), "\n", null);
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // Cuộn xuống cuối nếu cần
                    if (scrollToBottom) {
                        txtRead.setCaretPosition(doc.getLength());
                    }

                    // Cập nhật số lượng tin nhắn đã hiển thị
                    lastMessageCount = newMessageCount;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            txtRead.revalidate();
            txtRead.repaint();
        });
    }




    private void openFile(byte[] fileData, String fileName) {
        try {
            String extension = getFileExtension(fileName);
            File tempFile = File.createTempFile(fileName, "." + extension);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileData);
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                System.out.println("Desktop is not supported, file saved at: " + tempFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return lastIndexOfDot == -1 ? "" : fileName.substring(lastIndexOfDot + 1);
    }

    private void playAudio(byte[] audioData) {
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateUserList() throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            modelList.clear();
            try {
                List<User> userList = client.getUser();
                for (User user : userList) {
                    if ("online".equals(user.getStatus())) {
                        String path = "C:\\Users\\DELL\\eclipse-workspace\\ChatRMI_Client\\src\\icon\\icons8-green-dot-10.png";
                        File file = new File(path);
                        if (file.exists()) {
                            String imagePath = file.toURI().toString();
                            modelList.addElement("<html>" + user.getUsername() + " <img src='" + imagePath + "' width='10' height='10'></html>");
                        } else {
                            modelList.addElement("<html>" + user.getUsername() + "</html>");
                        }
                    } else {
                        modelList.addElement(user.getUsername());
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            txtListuser.revalidate();
            txtListuser.repaint();
        });
    }






    private synchronized void sendMessage() {
        Message msg = new Message();
        msg.setUsername(username);
        msg.setDate(new Date());
        msg.setMsg(txtSend.getText());
        msg.setType("text");
        txtSend.setText("");
        try {
            client.sendMsg(msg);
            updateMessageList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void sendImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byte[] fileData = new byte[(int) selectedFile.length()];
                fis.read(fileData);

                Message msg = new Message();
                msg.setUsername(username);
                msg.setDate(new Date());
                msg.setMsg(selectedFile.getName());
                msg.setType("image");
                msg.setFileData(fileData);

                client.sendImage(msg);
                updateMessageList();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private synchronized void sendFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byte[] fileData = new byte[(int) selectedFile.length()];
                fis.read(fileData);

                Message msg = new Message();
                msg.setUsername(username);
                msg.setDate(new Date());
                msg.setMsg(selectedFile.getName());
                msg.setFileData(fileData);

                String fileType = Files.probeContentType(selectedFile.toPath());
                if (fileType != null && fileType.startsWith("image")) {
                    msg.setType("image");
                    client.sendImage(msg);
                } else {
                    msg.setType("file");
                    client.sendFile(msg);
                }
                updateMessageList();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addPrivateMessageButton() {
        txtListuser.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<String> list = (JList<String>) evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    String selectedUser = list.getModel().getElementAt(index).replaceAll("<[^>]*>", ""); // Remove HTML tags
                    if (!selectedUser.equals(username)) {
                        PrivateChatApp privateChatApp = new PrivateChatApp(client, username, selectedUser);
                        privateChatApp.setVisible(true);
                    }
                }
            }
        });
    }


    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private synchronized void recordAndSendVoiceMessage() {
        AudioRecorder recorder = new AudioRecorder();
        byte[] audioData = recorder.recordAudio(10); // Ghi âm trong 10 giây

        if (audioData != null) {
            Message msg = new Message();
            msg.setUsername(username);
            msg.setDate(new Date());
            msg.setMsg("Voice Message");
            msg.setType("voice");
            msg.setFileData(audioData);

            try {
                client.sendVoiceMessage(msg);
                updateMessageList();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void dispose() {
        super.dispose();
        try {
            client.Logout(username);
            updateUserList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
    }

    public synchronized void updateUser(List<User> userList) throws RemoteException {
        updateUserList();
    }

    public synchronized void updateMessage(List<Message> allMessages) throws RemoteException {
        updateMessageList();
    }
}