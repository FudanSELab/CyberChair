package SELab.service.util;

import SELab.domain.User;
import SELab.exception.*;
import SELab.repository.ArticleRepository;
import SELab.repository.UserRepository;
import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UtilServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private RegisterRequest registerRequest = new RegisterRequest(TestUtility.registerRequest_UTIL);

    @Autowired
    private UtilService utilService;

    @BeforeEach
    void setUp() {
        utilService.Register(new RegisterRequest(
                TestUtility.USER_UTIL.getUsername(),
                TestUtility.USER_UTIL.getFullname(),
                TestUtility.USER_UTIL.getPassword(),
                TestUtility.USER_UTIL.getEmail(),
                TestUtility.USER_UTIL.getInstitution(),
                TestUtility.USER_UTIL.getRegion())
        );
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();
    }

    @Test
    void register() {
        usernamePatternCheck();
        passwordSecurityCheck1();
        passwordSecurityCheck2();
        emailPatternCheck();
        normalRegistrationCheck();
        usernameDuplicateRegister();
        emailDuplicateRegister();
    }

    @Test
    void login() {
        loginWithNotExistUser();
        loginAuthenticationFailed();
    }

    @Test
    void getUserinfo() {
        Exception thrown = assertThrows(UserNamedidntExistException.class,
                ()->utilService.getUserinfo("wrongName"));
        assertEquals("username " + "wrongName" + " didn't exist!", thrown.getMessage());

        assertNotNull(utilService.getUserinfo(TestUtility.USER_UTIL.getUsername()));
    }

    @Test
    void searchUsersbyFullname() {
        User user1 = new User(TestUtility.USER_UTIL);
        user1.setUsername("william111");
        user1.setEmail("wiliam@163.com");
        userRepository.save(user1);

        ResponseWrapper<?> ret = utilService.searchUsersbyFullname(user1.getFullname());
        HashMap<String, Set<HashMap<String, Object>>> retMap = (HashMap<String, Set<HashMap<String, Object>>>)ret.getResponseBody();
        Set<HashMap<String, Object>> retSet = retMap.get("users");
        assertEquals(2,retSet.size());
    }

    @Test
    void getPdfContent() {
        assertNotNull(utilService.getPdfContent("src/main/resources/Author1/2020-05-11/DataBase.pdf"));
    }


    //RegisterTest
    void  normalRegistrationCheck(){
        ResponseWrapper<?> wrapper = utilService.Register(registerRequest);
        assertEquals(ResponseGenerator.success,wrapper.getResponseMessage());
        userRepository.deleteAll();
    }

    void usernameDuplicateRegister(){
        userRepository.save(TestUtility.USER_UTIL);
        RegisterRequest registerRequest1 = new RegisterRequest(registerRequest);
        registerRequest1.setUsername(TestUtility.USER_UTIL.getUsername());
        Exception thrown = assertThrows(UsernameHasBeenRegisteredException.class,
                ()->utilService.Register(registerRequest1));
        assertEquals("Username '" + registerRequest1.getUsername() + "' has been registered", thrown.getMessage());
        userRepository.deleteAll();
    }

    void emailDuplicateRegister(){
        userRepository.save(TestUtility.USER_UTIL);
        RegisterRequest registerRequest1 = new RegisterRequest(registerRequest);
        registerRequest1.setEmail(TestUtility.USER_UTIL.getEmail());
        Exception thrown = assertThrows(EmailHasBeenRegisteredException.class,
                ()->utilService.Register(registerRequest1));
        assertEquals("Email " + registerRequest1.getEmail() + " has been registered", thrown.getMessage());
        userRepository.deleteAll();
    }


    void usernamePatternCheck(){

        RegisterRequest registerRequest1 = new RegisterRequest(registerRequest);
        String InvalidUsername="will";
        registerRequest1.setUsername(InvalidUsername);

        Exception thrown = assertThrows(
                IllegalUserNameException.class,
                ()->utilService.Register(registerRequest1));

        assertEquals("User Name is illegal!",thrown.getMessage());
        userRepository.deleteAll();
    }

    void passwordSecurityCheck1(){

        RegisterRequest registerRequest1 = new RegisterRequest(registerRequest);
        String InvalidPassword = "will";
        registerRequest1.setPassword(InvalidPassword);

        Exception thrown = assertThrows(PasswordLowSecurityAlertException.class,
                ()-> utilService.Register(registerRequest1));
        assertEquals("Password is of low security!", thrown.getMessage());
        userRepository.deleteAll();
    }

    void passwordSecurityCheck2(){

        RegisterRequest registerRequest1 = new RegisterRequest(registerRequest);
        String InvalidPassword = "williiiiiii";
        registerRequest1.setPassword(InvalidPassword);

        Exception thrown = assertThrows(PasswordLowSecurityAlertException.class,
                ()-> utilService.Register(registerRequest1));
        assertEquals("Password is of low security!", thrown.getMessage());
        userRepository.deleteAll();
    }


    void emailPatternCheck(){

        RegisterRequest registerRequest1 = new RegisterRequest(registerRequest);
        String InvalidEmail = "williiiiiii";
        registerRequest1.setEmail(InvalidEmail);
        Exception thrown = assertThrows(IllegalEmailAddressException.class,
                ()-> utilService.Register(registerRequest1));
        assertEquals("illegal email address", thrown.getMessage());
        userRepository.deleteAll();
    }

    //loginTest
    void loginWithNotExistUser(){
        LoginRequest loginRequest = new LoginRequest("testUser1", "testPassword1");

        Exception thrown = assertThrows(UserNamedidntExistException.class,
                ()->utilService.login(loginRequest));

        assertEquals("username " + "testUser1" + " didn't exist!", thrown.getMessage());

        //test successful login
        LoginRequest loginRequest2 = new LoginRequest(TestUtility.USER_UTIL.getUsername(),TestUtility.USER_UTIL.getPassword());
        assertNotNull(utilService.login(loginRequest2));
    }

    void loginAuthenticationFailed(){
        LoginRequest loginRequest = new LoginRequest(TestUtility.USER_UTIL.getUsername(), "wrongpassword");

        Exception thrown = assertThrows(AuthenticationFailedException.class,
                ()->utilService.login(loginRequest));

        assertEquals("Authentication failed, invalid username or password", thrown.getMessage());

        //test successful login
        LoginRequest loginRequest2 = new LoginRequest(TestUtility.USER_UTIL.getUsername(),TestUtility.USER_UTIL.getPassword());
        assertNotNull(utilService.login(loginRequest2));

    }
}