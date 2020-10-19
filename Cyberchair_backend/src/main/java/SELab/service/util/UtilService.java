package SELab.service.util;

import SELab.domain.User;
import SELab.exception.*;
import SELab.repository.*;
import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.security.jwt.JwtConfigProperties;
import SELab.security.jwt.JwtTokenUtil;
import SELab.security.jwt.SampleManager;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
@Service
public class UtilService {
    Logger logger = LoggerFactory.getLogger(UtilService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    public UtilService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    private void InformationCheck(String usrname, String password, String email) {

        String usernamePattern = "^[A-Za-z-\\-][A-Za-z_\\d\\-]{4,31}$";
        String pdPattern = "^[a-zA-Z_\\d\\-]{6,32}$";
        String emailPattern = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$";

        if (!Pattern.matches(usernamePattern, usrname))
            throw new IllegalUserNameException();
        if (!Pattern.matches(pdPattern, password)) {
            throw new PasswordLowSecurityAlertException();
        } else {
            int appearance = 0;
            String[] pdPatterns = new String[]{".*[a-zA-Z].*", ".*\\d.*", ".*[_\\-].*"};
            for (String pattern : pdPatterns) {
                if (Pattern.matches(pattern, password)) appearance++;
            }
            if (appearance < 2) throw new PasswordLowSecurityAlertException();
        }
        if (!Pattern.matches(emailPattern, email))
            throw new IllegalEmailAddressException();
    }

    public ResponseWrapper<?> Register(RegisterRequest request) {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        InformationCheck(username, password, email);

        if (userRepository.findByEmail(email) != null){
            throw new EmailHasBeenRegisteredException(email);//邮箱已被注册
        }

        if (userRepository.findByUsername(username) != null) {//用户名已经被注册
            throw new UsernameHasBeenRegisteredException(username);
        }

        User user = new User(username, request.getFullname(), BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()), request.getEmail(), request.getInstitution(), request.getRegion());//该user构造完成
        userRepository.save(user);//添加用户

        return new ResponseWrapper<>(200, ResponseGenerator.success, null);//注册成功
    }

    public ResponseWrapper<?> login(LoginRequest request){
        if (userRepository.findByUsername(request.getUsername()) == null) {
            throw new UserNamedidntExistException(request.getUsername());
        }
        try {
            SampleManager sampleManager = new SampleManager(userRepository);
            Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            Authentication result = sampleManager.authenticate(authentication);//校验
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            //invalid username
            throw new AuthenticationFailedException("Authentication failed, invalid username or password");
        }
        //passed the authentication, return the userDetail
        HashMap<String, Object> body = ResponseGenerator.generate(userRepository.findByUsername(request.getUsername()),
                new String[]{"username"}, null);
        JwtConfigProperties jwtConfigProperties = new JwtConfigProperties();
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(jwtConfigProperties);
        body.put("token", jwtTokenUtil.generateToken(userRepository.findByUsername(request.getUsername())));
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> getUserinfo(String username){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNamedidntExistException(username);
        }//邀请对象是否存在
        HashMap<String, HashMap<String, Object>> body = new HashMap<>();
        HashMap<String, Object> response = ResponseGenerator.generate(user,
                new String[]{"username","fullname","email","institution","region"}, null);

        body.put("UserInformation",response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> searchUsersbyFullname(String fullname){
        Streamable<User> users = userRepository.findByFullnameContains(fullname);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (User user: users) {
            HashMap<String, Object> userInfo = ResponseGenerator.generate(user,
                    new String[]{"username","fullname","email","institution","region"}, null);
            response.add(userInfo);
        }
        body.put("users",response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public byte[] getPdfContent(String pdfUrl){
        File file;
        FileInputStream inputStream=null;
        byte[] bytes=null;
        try {
            file = new File(pdfUrl);
            inputStream = new FileInputStream(file);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
        }catch (Exception e){
            logger.info("pdf get error");
        }finally {
            try{
                if(inputStream!=null)
                    inputStream.close();
            }catch (Exception e){
                logger.info("FileStream close error");
            }
        }
        return bytes;
    }


}
