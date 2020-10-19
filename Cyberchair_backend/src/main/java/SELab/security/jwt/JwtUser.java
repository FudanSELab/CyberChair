package SELab.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtUser implements UserDetails {
    private final String Username;
    private final String Password;
    private final Long id;
    private final Collection<? extends GrantedAuthority> authorities;
    boolean enabled;

    public JwtUser(
            Long id,
            String username,
            String password, Collection<? extends GrantedAuthority> authorities,
            boolean enabled
    ) {
        this.id = id;
        this.Username = username;
        this.Password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }


    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return Username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return Password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }


}