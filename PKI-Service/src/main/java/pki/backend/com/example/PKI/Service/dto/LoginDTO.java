package pki.backend.com.example.PKI.Service.dto;

public class LoginDTO {
    public String email;
    public String password;

    public LoginDTO(){

    }
    public LoginDTO(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
