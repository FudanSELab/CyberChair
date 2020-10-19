package SELab.service;

import SELab.repository.*;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.PCMemberInvitationRequest;
import SELab.request.user.InvitationRepoRequest;
import SELab.request.meeting.*;
import SELab.request.user.ArticleRequest;
import SELab.request.util.LoginRequest;
import SELab.request.util.RegisterRequest;
import SELab.service.admin.AdminService;
import SELab.service.meeting.MeetingArticleService;
import SELab.service.meeting.MeetingReviewService;
import SELab.service.meeting.MeetingUtilService;
import SELab.service.user.UserArticleService;
import SELab.service.user.UserInvitationService;
import SELab.service.user.UserMeetingService;
import SELab.service.util.UtilService;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@org.springframework.stereotype.Service
@RestController
public class Service {

    Logger logger = LoggerFactory.getLogger(Service.class);

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private ChairRelationRepository chairRelationRepository;
    private MeetingRepository meetingRepository;
    private PCMemberRelationRepository pcMemberRelationRepository;
    private ReviewRelationRepository reviewRelationRepository;
    private PostRepository postRepository;
    private RebuttalRepository rebuttalRepository;

    private UtilService utilService;
    private AdminService adminService;
    private MeetingArticleService meetingArticleService;
    private MeetingUtilService meetingUtilService;
    private UserArticleService userArticleService;
    private UserInvitationService userInvitationService;
    private UserMeetingService userMeetingService;
    private MeetingReviewService meetingReviewService;
    private static String fetched = " have been fetched";
    private static String requested = " have been requested";
    private static String forArticle = "for Article ";

    @Autowired
    public Service(UserRepository userRepository,
                   ArticleRepository articleRepository,
                   ChairRelationRepository chairRelationRepository,
                   MeetingRepository meetingRepository,
                   PCMemberRelationRepository pcMemberRelationRepository,
                   ReviewRelationRepository reviewRelationRepository,
                   PostRepository postRepository,
                   RebuttalRepository rebuttalRepository,
                   UtilService utilService,
                   AdminService adminService,
                   MeetingArticleService meetingArticleService,
                   MeetingUtilService meetingUtilService,
                   UserArticleService userArticleService,
                   UserInvitationService userInvitationService,
                   UserMeetingService userMeetingService,
                   MeetingReviewService meetingReviewService
    ) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.chairRelationRepository = chairRelationRepository;
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
        this.reviewRelationRepository = reviewRelationRepository;
        this.postRepository = postRepository;
        this.rebuttalRepository = rebuttalRepository;
        this.utilService = utilService;
        this.adminService = adminService;
        this.meetingArticleService = meetingArticleService;
        this.meetingUtilService = meetingUtilService;
        this.userArticleService = userArticleService;
        this.userInvitationService = userInvitationService;
        this.userMeetingService = userMeetingService;
        this.meetingReviewService = meetingReviewService;
    }


    public Service(){}

    public ResponseWrapper<?> register(RegisterRequest request) {
        ResponseWrapper<?> ret = utilService.Register(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Added registered user's username: "+request.getUsername());
        }
        return ret;
    }

    public ResponseWrapper<?> login(LoginRequest request) {
        ResponseWrapper<?> ret = utilService.login(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("User named "+request.getUsername() +" login success");
        }
        return  ret;
    }

    public ResponseWrapper<?> getUserinfo(String username) {
        ResponseWrapper<?> ret = utilService.getUserinfo(username);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Information of User named "+username +fetched);
        }
        return  ret;
    }

    public ResponseWrapper<?> searchUsersbyFullname(String fullname) {
        ResponseWrapper<?> ret = utilService.searchUsersbyFullname(fullname);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Information of User whose fullname is "+fullname +fetched);
        }
        return  ret;
    }

    public byte[] getPdfContent(String pdfUrl) {
        byte[] ret = utilService.getPdfContent(pdfUrl);
        if(ret!=null){
            logger.debug("File content of  "+ pdfUrl +" has been fetched");
        }
        return  ret;
    }

    public ResponseWrapper<?> meetingApplication(MeetingApplicationRequest request) {
        ResponseWrapper<?> ret = meetingUtilService.meetingApplication(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Meeting named "+ request.getMeetingName() +" has been added");
        }
        return  ret;
    }

    public ResponseWrapper<?> getmeetingInfo(String meetingName) {
        ResponseWrapper<?> ret = meetingUtilService.getmeetingInfo(meetingName);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Information of Meeting named "+ meetingName +fetched);
        }
        return  ret;
    }

    public ResponseWrapper<?> pcmInvitation(PCMemberInvitationRequest request) {
        ResponseWrapper<?> ret = meetingUtilService.pcmInvitation(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Invitation of Meeting named "+ request.getMeetingName() +" to "+"User named " + request.getPcMemberName() + " has been added");
        }
        return  ret;
    }

    public ResponseWrapper<?> getInvitationStatus(String meetingName) {
        ResponseWrapper<?> ret = meetingUtilService.getInvitationStatus(meetingName);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Invitations of Meeting named "+ meetingName +fetched);
        }
        return  ret;
    }

    public ResponseWrapper<?> getArticleDetail(String articleId){
        logger.debug("service for article detail called. article id = " + articleId);
        return  userArticleService.getArticleDetail(articleId);
    }

    public ResponseWrapper<?> getqueueingApplication() {
        ResponseWrapper<?> ret = adminService.getqueueingApplication();
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Queuing applications have been fetched by admin");
        }
        return  ret;
    }

    public ResponseWrapper<?> getalreadyApplication() {
        ResponseWrapper<?> ret = adminService.getalreadyApplication();
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Dealed applications have been fetched by admin");
        }
        return  ret;
    }

    public ResponseWrapper<?> applicationRatify(ApplicationRatifyRequest request) {
        ResponseWrapper<?> ret = adminService.applicationRatify(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Status of Meeting named "+ request.getMeetingName() + fetched);
        }
        return  ret;
    }

    public ResponseWrapper<?> chairMeeting(String username){
        ResponseWrapper<?> ret = userMeetingService.chairMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Meeting list "+ username + " role as chair has been fetched.");
        }
        return  ret;
    }

    public ResponseWrapper<?> pcMemberMeeting(String username){
        ResponseWrapper<?> ret = userMeetingService.pcMemberMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Meeting list "+ username + " role as pcMember has been fetched.");
        }
        return  ret;
    }

    public ResponseWrapper<?> authorMeeting(String username){
        ResponseWrapper<?> ret = userMeetingService.authorMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Meeting list "+ username + " role as author has been fetched.");
        }
        return  ret;
    }

    public ResponseWrapper<?> availableMeeting(String username){
        ResponseWrapper<?> ret = userMeetingService.availableMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Meeting list available to "+ username + fetched);
        }
        return  ret;
    }

    public ResponseWrapper<?> undealedNotifications(String username){
        ResponseWrapper<?> ret = userInvitationService.undealedNotifications(username);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("undealed messages of " + username + fetched);
        }
        return ret;
    }

    public ResponseWrapper<?> submitNewArticle(ArticleRequest request, String rootDir){
        ResponseWrapper<?> ret = userArticleService.uploadNewArticle(request, rootDir);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("user " + request.getUsername() + " submit a essay title " + request.getEssayTitle()
            +" to meeting " + request.getMeetingName() + " at date " + new Date());
        }
        return ret;
    }

    public ResponseWrapper<?> updateArticle(String articleId, ArticleRequest request, String rootDir){
        ResponseWrapper<?> ret = userArticleService.updateExistedArticle(articleId, request, rootDir);
        if(ret.getResponseMessage().equals((ResponseGenerator.success))){
            logger.info("user " + request.getUsername() + " update the article with id " +
                    articleId + " at time " + new Date());
        }
        return ret;
    }

    public ResponseWrapper<?> getReviewsOfArticle(String articleId){
        ResponseWrapper<?> ret = userArticleService.getAllReviews(articleId);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("the reviews of article with id " + articleId + " is requested");
        }
        return ret;
    }

    public ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request) {
        ResponseWrapper<?> ret = meetingArticleService.beginSubmission(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Submission begin for Meeting named " + request.getMeetingName());
        }
        return ret;
    }

    public ResponseWrapper<?> getInfoOfReview(String pcMemberName, String meetingName) {
        ResponseWrapper<?> ret = meetingArticleService.getInfoOfReview(pcMemberName,meetingName);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Review Information of " + pcMemberName + "in " + meetingName + requested);
        }
        return ret;
    }

    public ResponseWrapper<?> getInfoOfArticleToReview(String pcMemberName, String articleId) {
        ResponseWrapper<?> ret = meetingArticleService.getInfoOfArticleToReview(pcMemberName,articleId);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Article Information of Reviewer " + pcMemberName + forArticle + articleId + requested);
        }
        return ret;
    }

    public ResponseWrapper<?> invitationRepo(InvitationRepoRequest request){
        ResponseWrapper<?> ret = userInvitationService.invitationRepo(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Invitation Repo by "+ request.getUsername() + "to " + request.getMeetingName() + " have done.");
        }
        return ret;
    }

    public ResponseWrapper<?> review(ReviewRequest request) {
        ResponseWrapper<?> ret = meetingArticleService.review(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Review of Reviewer " + request.getPcMemberName() + forArticle + request.getArticleid() + " have been submitted");
        }
        return ret;
    }

    public ResponseWrapper<?> undealedNotificationsNum(String username){
        ResponseWrapper<?> ret = userInvitationService.undealedNotificationsNum(username);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("the num of undealed messages of " + username + "has been fetched.");
        }
        return ret;
    }

    public ResponseWrapper<?> getAlreadyReviewedInfo(String pcMemberName, String articleId) {
        ResponseWrapper<?> ret = meetingArticleService.getAlreadyReviewedInfo(pcMemberName,articleId);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Review of Reviewer " + pcMemberName + forArticle + articleId + requested);
        }
        return ret;
    }

    public ResponseWrapper<?> alreadyDealedNotifications(String username){
        ResponseWrapper<?> ret = userInvitationService.alreadyDealedNotifications(username);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("alreadyDealed messages of " + username + "has been fetched.");
        }
        return ret;
    }
    
    public ResponseWrapper<?> beginReview(BeginReviewRequest request) {
        ResponseWrapper<?> ret = meetingArticleService.beginReview(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Meeting named " + request.getMeetingName() + " review begin");
        }
        return ret;
    }

    public ResponseWrapper<?> getSubmissionList(String authorName, String meetingName) {
        ResponseWrapper<?> ret = meetingUtilService.getSubmissionList(authorName,meetingName);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Meeting named " + meetingName + " Articles of "+ authorName +fetched);
        }
        return ret;
    }

    public ResponseWrapper<?> reviewPublish(ResultPublishRequest request) {
        ResponseWrapper<?> ret = meetingUtilService.reviewPublish(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Meeting named " + request.getMeetingName() + " have published reviews");
        }
        return ret;
    }

    public ResponseWrapper<?> reviewPost(ReviewPostRequest request) {
        ResponseWrapper<?> ret = meetingReviewService.reviewPost(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Review PostMessage: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> getPostList(String articleId, String postStatus) {
        ResponseWrapper<?> ret = meetingReviewService.getPostList(articleId,postStatus);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Get postList article: ID " + articleId +" Post Status: " + postStatus);
        }
        return ret;
    }

    public ResponseWrapper<?> updateReview(UpdateReviewRequest request) {
        ResponseWrapper<?> ret = meetingReviewService.updateReview(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("update Review: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> reviewConfirm(ReviewConfirmRequest request) {
        ResponseWrapper<?> ret = meetingReviewService.reviewConfirm(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Review Confirm: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> rebuttal(RebuttalRequest request) {
        ResponseWrapper<?> ret = meetingReviewService.rebuttal(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Rebuttal: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> getRebuttalInfo(String articleId) {
        ResponseWrapper<?> ret = meetingReviewService.getRebuttalInfo(articleId);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Get Rebuttal Info for article: ID " + articleId);
        }
        return ret;
    }

    public ResponseWrapper<?> finalPublish(FinalPublishRequest request) {
        ResponseWrapper<?> ret = meetingReviewService.finalPublish(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Final Publish: " + request.toString());
        }
        return ret;
    }
}
