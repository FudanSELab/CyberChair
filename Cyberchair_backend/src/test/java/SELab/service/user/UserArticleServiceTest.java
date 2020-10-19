package SELab.service.user;

import SELab.domain.*;
import SELab.exception.AtLeastOneTopicException;
import SELab.exception.MeetingUnavaliableToOperateException;
import SELab.exception.UserNamedidntExistException;
import SELab.exception.user.ArticleNotFoundException;
import SELab.repository.*;
import SELab.request.user.ArticleDetailRequest;
import SELab.request.user.ArticleRequest;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.PCmemberRelationStatus;
import SELab.utility.contract.ReviewStatus;
import SELab.utility.response.ResponseWrapper;
import TestUtil.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserArticleServiceTest {
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

    public void setUpMeetingBasis() {
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
    }

    @BeforeEach
    void setUp() {

        setUpMeetingBasis();
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
        articleUpdateRequest.setFile(uploadFile);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();
        meetingRepository.deleteAll();
        reviewRelationRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
    }

    @Test
    void getArticleDetail() {
        //检测存在性检测功能
        Exception thrown1 = assertThrows(ArticleNotFoundException.class,
                ()->userArticleService.getArticleDetail("999"));
        assertEquals("article with id " + "999" + "not found",thrown1.getMessage());

        //检测功能是否正常
        Article article = new Article(TestUtility.article_UTIL);
        articleRepository.save(article);
        long id = article.getId();
        assertNotNull(userArticleService.getArticleDetail(Long.toString(id)));
    }
    @Test
    void fileTest(){
        uploadNewArticle();
        updateExistedArticle();
    }


    void uploadNewArticle() {
        articleRequest.setMeetingName("WrongName");
        Exception thrown1 = assertThrows(MeetingUnavaliableToOperateException.class,
                ()->userArticleService.uploadNewArticle(articleRequest,"src/main/resources/"));
        assertEquals("Meeting Name" + "Not created" + " isn't avaliable to do this operation for current status.(Must unproccessed when ratify,ApplyPassed when beginSubmission,SubmissionAvailable when submission and begin review)",
                thrown1.getMessage());
        articleRequest.setMeetingName(TestUtility.articleRequest_UTIL.getMeetingName());

        articleRequest.setUsername("WrongName");
        Exception thrown2 = assertThrows(UserNamedidntExistException.class,
                ()->userArticleService.uploadNewArticle(articleRequest,"src/main/resources/"));
        assertEquals("username " + "not a valid user" + " didn't exist!",
                thrown2.getMessage());
        articleRequest.setUsername(TestUtility.articleRequest_UTIL.getUsername());

        Meeting meeting = meetingRepository.findByMeetingName(TestUtility.MEETING_UTIL.getMeetingName());
        meeting.setStatus(MeetingStatus.reviewCompleted);
        meetingRepository.save(meeting);
        Exception thrown3 = assertThrows(MeetingUnavaliableToOperateException.class,
                ()->userArticleService.uploadNewArticle(articleRequest,"src/main/resources/"));
        assertEquals("Meeting Name" + "update or upload articles" + " isn't avaliable to do this operation for current status.(Must unproccessed when ratify,ApplyPassed when beginSubmission,SubmissionAvailable when submission and begin review)",
                thrown3.getMessage());
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);

        assertNotNull(userArticleService.uploadNewArticle(articleRequest,"src/main/resources/"));
    }

    void updateExistedArticle() {
        Article article = new Article(TestUtility.article_UTIL);
        articleRepository.save(article);
        long id = article.getId();
        assertNotNull(userArticleService.updateExistedArticle(Long.toString(id),articleUpdateRequest,"src/main/resources/"));
    }

    @Test
    void getAllReviews() {

        Article article = new Article(TestUtility.article_UTIL);
        articleRepository.save(article);
        long id = article.getId();
        ReviewRelation reviewRelation1 = new ReviewRelation(pcm1.getId(),meeting.getId(),id, ReviewStatus.alreadyReviewed,2,"Meeting NameNot created isn't avaliable to do this operation","ApplyPassed when beginSubmission,SubmissionAvailable when submission and begin review");
        ReviewRelation reviewRelation2 = new ReviewRelation(pcm2.getId(),meeting.getId(),id, ReviewStatus.alreadyReviewed,1,"Meeting  to do this operation","ApplyPassed when beginSubmission,SubmissionAvailable when submission");
        reviewRelationRepository.save(reviewRelation1);
        reviewRelationRepository.save(reviewRelation2);

        Exception thrown1 = assertThrows(ArticleNotFoundException.class,
                ()->userArticleService.getAllReviews("999"));
        assertEquals("article with id " + "999" + "not found",thrown1.getMessage());

        HashMap<String, Set<HashMap<String, Object>>> ret = (HashMap<String, Set<HashMap<String, Object>>>)userArticleService.getAllReviews(Long.toString(id)).getResponseBody();
        Set<HashMap<String, Object>> retSet = ret.get("reviews");
        assertEquals(2,retSet.size());
        Article article1 = articleRepository.findById((long)article.getId());
        int x = 0;
    }
}