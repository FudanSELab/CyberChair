package SELab.request.util;

/**
 * @author TianyiZhang
 */
public class RegisterRequest {
    private String username;
    private String fullname;
    private String password;
    private String email;
    private String institution;
    private String region;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String fullname, String password, String email, String institution, String region) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.institution = institution;
        this.region = region;
    }

    public RegisterRequest(RegisterRequest registerRequest) {
        this.username = registerRequest.getUsername();
        this.fullname = registerRequest.getFullname();
        this.password = registerRequest.getPassword();
        this.email = registerRequest.getEmail();
        this.institution = registerRequest.getInstitution();
        this.region = registerRequest.getRegion();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}

