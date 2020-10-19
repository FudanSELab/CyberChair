package TestUtil;

import SELab.domain.*;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.request.meeting.MeetingApplicationRequest;
import SELab.request.meeting.PCMemberInvitationRequest;
import SELab.request.meeting.ReviewPostRequest;
import SELab.request.user.ArticleRequest;
import SELab.request.user.InvitationRepoRequest;
import SELab.request.util.RegisterRequest;
import SELab.utility.contract.*;
import javafx.util.Pair;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestUtility {


    private static final String username1 = "william000";
    private static final String password = "szySZY000";
    private static final String email1 = "william@foxmail.com";
    private static final String fullname = "william song";
    private static final String region = "Shanghai China";
    private static final String institution = "Fudan University";
    public final static User USER_UTIL  = new User(
            username1,
            fullname,
            password,
            email1,
            institution,
            region
    );

    //RegisterRequest
    private static final String username2 = "william111";
    private static final String email2 = "william@gmail.com";
    public final static RegisterRequest registerRequest_UTIL = new RegisterRequest(
            username2,
            fullname,
            password,
            email2,
            institution,
            region
    );



    private static final String mChairname = "william001";
    private static final String mMeetingName = "WILLIAM MCT";
    private static final String mAcronym = "WACT";
    private static final String mCity = "ShangHai";
    private static final String mRegion = "China";
    private static final String mVenue = "YangPu";
    public static final String[] TOPIC_VALUES = new String[] {"NLP","AI"};
    private static final Set<String> mTopics = new HashSet<>(Arrays.asList(TOPIC_VALUES));
    private static final String mOrganizer = "William Song";
    private static final String mWebPage = "www.williamsong.com";
    private static final String submissionDeadlineDate = "2020-4-21";
    private static final String notificationOfAcceptanceDate = "2020-4-21";
    private static final String conferenceDate = "2020-4-22";
    private static final String mStatus = "SubmittedAvailable";
    private static final Author mAuthor = new Author("Pony Ma","Tencent","GuangDong","tencent@qq.com");
    private static final Pair<Author,Integer> authorPair = new Pair<>(mAuthor, 1);
    private static final Set<Pair<Author,Integer>> mAuthors = new HashSet<Pair<Author,Integer>>(){{add(authorPair);}};

    public static Meeting MEETING_UTIL = new Meeting(
            mChairname,mMeetingName,mAcronym,mRegion,mCity,
            mVenue,mTopics, mOrganizer,
            mWebPage,submissionDeadlineDate,notificationOfAcceptanceDate,
            conferenceDate,mStatus
    );

    public static MeetingApplicationRequest meetingApplicationRequest_UTIL = new MeetingApplicationRequest(
            mChairname,mMeetingName,mAcronym,mRegion,mCity,
            mVenue,mTopics, mOrganizer,
            mWebPage,submissionDeadlineDate,notificationOfAcceptanceDate,
            conferenceDate
    );

    public static final ApplicationRatifyRequest applicationRatifyRequest_UTIL= new ApplicationRatifyRequest(
            "WILLIAM MCT",
            MeetingStatus.applyPassed);

    public static final PCMemberRelation pcMemberRelation_UTIL = new PCMemberRelation(
            null,
            null,
            PCmemberRelationStatus.accepted,
            mTopics);

    public static final PCMemberInvitationRequest pcMemberInvitationRequest_UTIL= new PCMemberInvitationRequest(
            "WILLIAM MCT",
            "PCMember1"
            );

    public static final Article article_UTIL = new Article(
            "Author1",
            "WILLIAM MCT",
            "2020-05-16",
            "Database Description",
            "数据库使⽤Spring JPA data数据库，数据查询⽅法在repository接⼝中确定，根据⽅法名决定查询⽅式",
            "src/main/resources/Author1/2020-05-11/DataBase.pdf",
            ArticleStatus.queuing,
            mTopics,
            mAuthors
    );


    public static final ArticleRequest articleRequest_UTIL = new ArticleRequest(
            "WILLIAM MCT",
            "Author1",
            "Database Description",
            "数据库使⽤Spring JPA data数据库，数据查询⽅法在repository接⼝中确定，根据⽅法名决定查询⽅式",
            "2020-05-11",
            null,
            mTopics,
            mAuthors
    );

    public static final InvitationRepoRequest invitationRepoRequest_UTIL = new InvitationRepoRequest(
            "pcmember1",
            MEETING_UTIL.getMeetingName(),
            PCmemberRelationStatus.accepted,
            mTopics
    );

    public static final ReviewRelation reviewRelation_UTIL = new ReviewRelation(
            null,
            null,
            null,
            ReviewStatus.alreadyReviewed,
            2,
            ReviewConfidence.high,
            "Neither @ContextConfiguration nor @ContextHierarchy found for test class"
    );

    public static final ReviewPostRequest reviewPostRequest = new ReviewPostRequest(
            null,
            null,
            null,
            "expressions contributing x to D(Sk)' Thus, the implementations believe that S = {x, f(x)} is unifiable. (They cannot actually carry out the substitution.",
            RebuttalStatus.beforeRebuttal
    );
}
