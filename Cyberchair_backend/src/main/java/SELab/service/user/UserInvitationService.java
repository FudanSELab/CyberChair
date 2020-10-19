package SELab.service.user;

import SELab.domain.Meeting;
import SELab.domain.PCMemberRelation;
import SELab.repository.*;
import SELab.request.user.InvitationRepoRequest;
import SELab.utility.contract.PCmemberRelationStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserInvitationService {
    private UserRepository userRepository;
    private MeetingRepository meetingRepository;
    private PCMemberRelationRepository pcMemberRelationRepository;

    @Autowired
    public UserInvitationService(UserRepository userRepository, MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository) {
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
    }

    public ResponseWrapper<?> undealedNotifications(String username){
        Long userId = userRepository.findByUsername(username).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndStatus(userId, PCmemberRelationStatus.undealed);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for(PCMemberRelation relation : relationList){
            Meeting meeting = meetingRepository.findById((long)relation.getMeetingId());
            HashMap<String, Object> invitationInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "chairName","topic"}, null);
            response.add(invitationInfo);
        }
        body.put("invitations", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> invitationRepo(InvitationRepoRequest request){
        Long userId = userRepository.findByUsername(request.getUsername()).getId();
        Long meetingId = meetingRepository.findByMeetingName(request.getMeetingName()).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndMeetingId(userId, meetingId);
        for(PCMemberRelation relation : relationList){
            if (relation.getStatus().equals(PCmemberRelationStatus.undealed)){
                relation.setStatus(request.getResponse());
                if(request.getResponse().equals(PCmemberRelationStatus.accepted)){
                    relation.setTopic(request.getTopics());
                }
                pcMemberRelationRepository.save(relation);
                break;
            }
        }
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    public  ResponseWrapper<?> undealedNotificationsNum(String username){
        Long userId = userRepository.findByUsername(username).getId();
        List<PCMemberRelation> relationList = pcMemberRelationRepository.findByPcmemberIdAndStatus(userId, PCmemberRelationStatus.undealed);
        HashMap<String, Object> body = new HashMap<>();
        body.put("undealedNotificationsNum", relationList.size());
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> alreadyDealedNotifications(String username){
        Long userId = userRepository.findByUsername(username).getId();
        List<PCMemberRelation> relationList1 = pcMemberRelationRepository.findByPcmemberIdAndStatusNot(userId, PCmemberRelationStatus.undealed);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for(PCMemberRelation relation : relationList1){
            HashMap<String, Object> invitaionInfo = ResponseGenerator.generate(relation,
                    new String[]{"status"}, null);
            invitaionInfo.put("meetingName", meetingRepository.findById((long)relation.getMeetingId()).getMeetingName());
            invitaionInfo.put("chairName", meetingRepository.findById((long)relation.getMeetingId()).getChairName());
            response.add(invitaionInfo);
        }
        body.put("alreadyDealedNotifications", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

}
