import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/chatapp";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Thêm dòng này
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean register(String username, String password,String email) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(String username, String password) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loginWithFacebook(String facebookId) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE facebook_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, facebookId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void sendMessage(String sender, String receiver, String messageType, InputStream content) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO messages (sender, receiver, message_type, content) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setString(3, messageType);
            statement.setBlob(4, content);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getMessages() {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM messages";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<User> getUser() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT username, status FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String status = resultSet.getString("status");
                users.add(new User(username, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public static void saveMessage(Message msg) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO messages (username, msg, date, type, receiver, fileData) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, msg.getUsername());
            statement.setString(2, msg.getMsg());
            statement.setTimestamp(3, new Timestamp(msg.getDate().getTime()));
            statement.setString(4, msg.getType());
            statement.setString(5, msg.getReceiver());
            statement.setBytes(6, msg.getFileData());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM messages";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Message msg = new Message();
                msg.setUsername(resultSet.getString("username"));
                msg.setMsg(resultSet.getString("msg"));
                msg.setDate(resultSet.getTimestamp("date"));
                msg.setType(resultSet.getString("type"));
                msg.setReceiver(resultSet.getString("receiver"));
                msg.setFileData(resultSet.getBytes("fileData"));
                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    public static void updateUserStatus(String username, String status) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE users SET status = ? WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, status);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
