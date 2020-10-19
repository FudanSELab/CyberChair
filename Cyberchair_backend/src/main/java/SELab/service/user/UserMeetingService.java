package SELab.service.user;

import SELab.domain.Article;
import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.repository.*;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.contract.PCmemberRelationStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserMeetingService {
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private MeetingRepository meetingRepository;
    private PCMemberRelationRepository pcMemberRelationRepository;

    @Autowired
    public UserMeetingService(UserRepository userRepository, ArticleRepository articleRepository, MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
    }

    @Transactional
    public ResponseWrapper<?> chairMeeting(String username){
        List<Meeting> meetingList = meetingRepository.findByChairName(username);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (Meeting meeting : meetingList){
            HashMap<String, Object> meetingInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "conferenceDate", "topic"}, null);
            response.add(meetingInfo);
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
    @Transactional
    public ResponseWrapper<?> pcMemberMeeting(String username){
        Long userId = userRepository.findByUsername(username).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndStatus(userId, PCmemberRelationStatus.accepted);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (PCMemberRelation relation : relationList){
            Meeting meeting = meetingRepository.findById((long)relation.getMeetingId());
            HashMap<String, Object> meetingInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "conferenceDate", "topic"}, null);
            response.add(meetingInfo);
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
    @Transactional
    public ResponseWrapper<?> authorMeeting(String username){
        List<Article> articleList = articleRepository.findByContributorName(username);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        Set<Long>meetingCount = new HashSet<>();
        for(Article article : articleList){
            Meeting meeting = meetingRepository.findByMeetingName(article.getMeetingname());
            if(meeting != null && !meetingCount.contains(meeting.getId())){
                meetingCount.add(meeting.getId());
                response.add(ResponseGenerator.generate(meeting,
                        new String[]{"meetingName", "acronym", "submissionDeadlineDate", "topic"}, null));
            }
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
    @Transactional
    public ResponseWrapper<?> availableMeeting(String username){
        List<Meeting>allMeeting = meetingRepository.findByStatusAndChairNameNot(MeetingStatus.submissionAvaliable, username);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for(Meeting meeting: allMeeting){
            response.add(ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "submissionDeadlineDate", "topic"}, null));
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
}
