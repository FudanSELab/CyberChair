package SELab.service.IntegrationTest;

import SELab.domain.Article;
import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.domain.User;
import SELab.repository.*;
import SELab.request.user.ArticleRequest;
import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.service.user.UserArticleService;
import SELab.service.util.UtilService;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.PCmemberRelationStatus;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ArticleUploadAndUpdateTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private ReviewRelationRepository reviewRelationRepository;
    @Autowired
    private PCMemberRelationRepository pcMemberRelationRepository;

    @Autowired
    private UserArticleService userArticleService;

    private ArticleRequest articleRequest;
    private ArticleRequest articleUpdateRequest;

    private Meeting meeting;

    private User pcm1;
    private User pcm2;



    void setUp(){
        User chair = new User(TestUtility.USER_UTIL);
        chair.setUsername("william001");
        userRepository.save(chair);
        meeting = new Meeting(TestUtility.MEETING_UTIL);
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);
        pcm1 = new User(TestUtility.USER_UTIL);
        pcm1.setUsername("pcmember1");
        pcm1.setEmail("pcmember1@gmail.com");
        userRepository.save(pcm1);
        pcm2 = new User(TestUtility.USER_UTIL);
        pcm2.setUsername("pcmember2");
        pcm2.setEmail("pcmember2@gmail.com");
        userRepository.save(pcm2);
        User author1 = new User(TestUtility.USER_UTIL);
        author1.setUsername("Author1");
        author1.setEmail("author1@gmail.com");
        userRepository.save(author1);
        PCMemberRelation chairRelation = new PCMemberRelation(chair.getId(),meeting.getId(), PCmemberRelationStatus.accepted,meeting.getTopic());
        PCMemberRelation pcm1Relation = new PCMemberRelation(pcm1.getId(),meeting.getId(), PCmemberRelationStatus.accepted,meeting.getTopic());
        PCMemberRelation pcm2Relation = new PCMemberRelation(pcm2.getId(),meeting.getId(), PCmemberRelationStatus.accepted,meeting.getTopic());
        pcMemberRelationRepository.save(chairRelation);
        pcMemberRelationRepository.save(pcm1Relation);
        pcMemberRelationRepository.save(pcm2Relation);

        articleRequest = new ArticleRequest(TestUtility.articleRequest_UTIL);
        File file = new File("src/main/resources/Author1/2020-05-11/DataBase.pdf");
        FileInputStream inputStream = null;
        MockMultipartFile uploadFile = null;
        try{
            inputStream = new FileInputStream(file);
            uploadFile = new MockMultipartFile(file.getName(), file.getName(),"pdf", inputStream);
        }catch (IOException e){
            e.printStackTrace();
//            System.exit(0);
        }
        articleRequest.setFile(uploadFile);

        articleUpdateRequest = new ArticleRequest(TestUtility.articleRequest_UTIL);
        File file1 = new File("src/main/resources/DataBase.pdf");
        FileInputStream inputStream1 = null;
        MockMultipartFile uploadFile1 = null;
        try{
            inputStream1 = new FileInputStream(file1);
            uploadFile1 = new MockMultipartFile(file1.getName(), file1.getName(),"pdf", inputStream1);
        }catch (IOException e){
            e.printStackTrace();
//            System.exit(0);
        }
        articleUpdateRequest.setFile(uploadFile1);
        articleUpdateRequest.setEssayAbstract("Update Abstract");
        articleUpdateRequest.setEssayTitle("Update Title");
    }


    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();
        meetingRepository.deleteAll();
        reviewRelationRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
    }

    @Test
    public void UploadAndUpdateTest(){
        setUp();
        userArticleService.uploadNewArticle(articleRequest, "src/main/resources/");
        Article article = articleRepository.findByContributorNameAndMeetingName(articleRequest.getUsername(), articleRequest.getMeetingName()).get(0);
        Long articleId = article.getId();
        assertEquals(true, article.getArticleAbstract().equals(articleRequest.getEssayAbstract()));
        userArticleService.updateExistedArticle(String.valueOf(articleId), articleUpdateRequest, "src/main/resources/");
        article = articleRepository.findById((long)articleId);
        assertEquals(true, article.getArticleAbstract().equals(articleUpdateRequest.getEssayAbstract()));
        tearDown();
    }
}