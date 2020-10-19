package SELab.security.jwt;

import SELab.domain.User;
import SELab.exception.UserNamedidntExistException;
import SELab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class SampleManager implements AuthenticationManager {
    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public SampleManager(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        User target = userRepository.findByUsername(auth.getName());
        if (target == null) {
            throw new UserNamedidntExistException(auth.getName());
        }
        if (BCrypt.checkpw((String) auth.getCredentials(), target.getPassword())) {
            return new UsernamePasswordAuthenticationToken(auth.getName(),
                    auth.getCredentials(), AUTHORITIES);
        }
        throw new BadCredentialsException("Bad Credentials");
    }

    public void setUserRepository(UserRepository Repository) {
        userRepository = Repository;
    }
}