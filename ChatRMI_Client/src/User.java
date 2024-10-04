import java.io.Serializable;

public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
    private String status;

    public User(String username, String status) {
        this.username = username;
        this.status = status;
    }
    public User() {
       
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

	public void setUsername(String username) {
		this.username = username;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}
