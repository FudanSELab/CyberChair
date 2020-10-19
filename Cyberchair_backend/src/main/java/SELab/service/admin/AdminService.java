package SELab.service.admin;

import SELab.domain.Meeting;
import SELab.exception.MeetingOfNoExistenceException;
import SELab.exception.MeetingUnavaliableToOperateException;
import SELab.repository.*;
import SELab.request.admin.ApplicationRatifyRequest;
import SELab.utility.contract.MeetingStatus;
import SELab.utility.response.ResponseGenerator;
import SELab.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    public AdminService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Transactional
    public ResponseWrapper<?> getqueueingApplication() {
        List<Meeting> meetings = meetingRepository.findByStatus(MeetingStatus.unprocessed);
        return ResponseGenerator.injectObjectFromListToResponse("queueingApplication",meetings,new String[]{"chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate"},null);
    }
    @Transactional
    public ResponseWrapper<?> getalreadyApplication() {
        List<Meeting> meetings = meetingRepository.findByStatusNot(MeetingStatus.unprocessed);
        return ResponseGenerator.injectObjectFromListToResponse("alreadyApplication",meetings,new String[]{"chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate"},null);
    }
    @Transactional
    public ResponseWrapper<?> applicationRatify(ApplicationRatifyRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if(meeting==null){
            throw new MeetingOfNoExistenceException(meetingName);
        }
        if(!meeting.getStatus().equals(MeetingStatus.unprocessed)){
            throw new MeetingUnavaliableToOperateException(meetingName);
        }
        meeting.setStatus(request.getApprovalStatus());
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }
}
