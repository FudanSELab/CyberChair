package SELab.service.IntegrationTest;

import SELab.domain.User;
import SELab.repository.UserRepository;
import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.service.util.UtilService;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RegisterAndLoginTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilService utilService;
    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void registerAndLoginTest(){
        RegisterRequest registerRequest = new RegisterRequest(TestUtility.registerRequest_UTIL);
        assertNotNull(utilService.Register(registerRequest));
        User user = userRepository.findByUsername(registerRequest.getUsername());
        assertNotNull(utilService.login(new LoginRequest(user.getUsername(),registerRequest.getPassword())));
    }
}
