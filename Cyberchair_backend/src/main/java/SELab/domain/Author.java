package SELab.domain;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

public class Author implements Serializable {
    @JSONField(name = "fullname")
    private String fullname;
    @JSONField(name = "institution")
    private String institution;
    @JSONField(name = "region")
    private String region;
    @JSONField(name = "email")
    private String email;

    public Author(){}

    public Author(String fullname, String institution, String region, String email) {
        this.fullname = fullname;
        this.institution = institution;
        this.region = region;
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
