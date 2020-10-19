package SELab.service.IntegrationTest;

import SELab.Application;
import SELab.domain.*;
import SELab.repository.*;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.request.meeting.BeginReviewRequest;
import SELab.request.meeting.BeginSubmissionRequest;
import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.ReviewRequest;
import SELab.request.user.ArticleRequest;
import SELab.service.admin.AdminService;
import SELab.service.meeting.MeetingArticleService;
import SELab.service.meeting.MeetingUtilService;
import SELab.service.user.UserArticleService;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.PCmemberRelationStatus;
import SELab.utility.response.ResponseWrapper;
import TestUtil.TestUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MeetingProcessTest {
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
    private MeetingUtilService meetingUtilService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private MeetingArticleService meetingArticleService;

    @Autowired
    private UserArticleService userArticleService;

    private User chair;
    private User pcm1;
    private User pcm2;
    private User author;
    private Article article;
    private Rebuttal rebuttal;

    void setUp(){
        chair = new User("yuchun","yuchundai","abc123456","123@qq.com","abc","ddd");
        pcm1 = new User("pcm1","ppcm1","abc123456","1234@qq.com","a","b");
        pcm2 = new User("pcm2","ppcm2","abc123456","12345@qq.com","a","b");
        author = new User("author","aaa","abc123456","123456@qq.com","a","b");
        userRepository.save(chair);
        userRepository.save(pcm1);
        userRepository.save(pcm2);
        userRepository.save(author);
    }

    void testApply(){
        MeetingApplicationRequest request = TestUtility.meetingApplicationRequest_UTIL;
        request.setChairName("yuchun");
        request.setMeetingName("ACL");
        ResponseWrapper<?> resp = meetingUtilService.meetingApplication(request);
        assertEquals(200, resp.getResponseCode());
    }

    void testRatify(){
        ApplicationRatifyRequest request = new ApplicationRatifyRequest("ACL", MeetingStatus.applyPassed);
        ResponseWrapper<?> resp = adminService.applicationRatify(request);
        assertEquals(200, resp.getResponseCode());
    }

    void testSubmit(){
        BeginSubmissionRequest beginSubmissionRequest = new BeginSubmissionRequest("ACL");
        ResponseWrapper<?> beginResp = meetingArticleService.beginSubmission(beginSubmissionRequest);
        assertEquals(beginResp.getResponseCode(), 200);
        ArticleRequest articleRequest = new ArticleRequest(TestUtility.articleRequest_UTIL);
        File file = new File("C:\\Users\\20236\\Desktop\\Lab5 - Last Dance.pdf");
        FileInputStream inputStream = null;
        MockMultipartFile uploadFile = null;
        try{
            inputStream = new FileInputStream(file);
            uploadFile = new MockMultipartFile(file.getName(), file.getName(),"pdf", inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        articleRequest.setFile(uploadFile);
        articleRequest.setUsername("author");
        articleRequest.setMeetingName("ACL");
        ResponseWrapper<?> resp = userArticleService.uploadNewArticle(articleRequest, "C:\\Users\\20236\\Desktop\\");
        assertEquals(resp.getResponseCode(), 200);
    }

    void testReview(){
        PCMemberRelation relation1 = new PCMemberRelation(TestUtility.pcMemberRelation_UTIL);
        relation1.setPcmemberId(pcm1.getId());
        relation1.setMeetingId(meetingRepository.findByChairName("yuchun").get(0).getId());
        PCMemberRelation relation2 = new PCMemberRelation(TestUtility.pcMemberRelation_UTIL);
        relation2.setPcmemberId(pcm2.getId());
        relation2.setMeetingId(meetingRepository.findByChairName("yuchun").get(0).getId());
        pcMemberRelationRepository.save(relation1);
        pcMemberRelationRepository.save(relation2);

        BeginReviewRequest reviewRequest = new BeginReviewRequest("ACL", "LoadBalancing");
        ResponseWrapper<?> reviewResp = meetingArticleService.beginReview(reviewRequest);
        assertEquals(reviewResp.getResponseCode(), 200);

        article = articleRepository.findByMeetingName("ACL").get(0);
        ReviewRequest review1 = new ReviewRequest("yuchun",article.getId().toString(),"2","very high", "good");
        ReviewRequest review2 = new ReviewRequest("pcm1",article.getId().toString(),"2","very high", "good");
        ReviewRequest review3 = new ReviewRequest("pcm2",article.getId().toString(),"2","very high", "good");
        meetingArticleService.review(review1);
        meetingArticleService.review(review2);
        meetingArticleService.review(review3);
        assertEquals(meetingRepository.findByMeetingName("ACL").getStatus(), "ReviewCompleted");
    }

    void tearDown() {
        userRepository.deleteAll();
        articleRepository.deleteAll();
        meetingRepository.deleteAll();
        reviewRelationRepository.deleteAll();
        pcMemberRelationRepository.deleteAll();
    }

    @Test
    public void processTest(){
        tearDown();
        setUp();
        testApply();
        testRatify();
        testSubmit();
        testReview();
    }

}
