package SELab.exception;

import SELab.exception.user.ArticleNotFoundException;
import SELab.utility.response.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * You can handle all of your exceptions here.
 *
 * @author LBW
 */
@ControllerAdvice
@ResponseBody
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ErrorResponseConstruct(400, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameHasBeenRegisteredException.class)
    ResponseEntity<?> handlerUsernameHasBeenRegisteredException(UsernameHasBeenRegisteredException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNamedidntExistException.class)
    ResponseEntity<?> UserNamedidntExistExceptionHandler(UserNamedidntExistException ex) {
        return ErrorResponseConstruct(200, ex.getMessage(), HttpStatus.ACCEPTED);
    }


    @ExceptionHandler(AuthenticationFailedException.class)
    ResponseEntity<?> AuthenticationFailedExceptionHandler(AuthenticationFailedException ex) {
        return ErrorResponseConstruct(403, ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalUserNameException.class)
    ResponseEntity<?> IllegalUserNameExceptionHandler(IllegalUserNameException ex) {
        return ErrorResponseConstruct(400, "illegal username", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordLowSecurityAlertException.class)
    ResponseEntity<?> PasswordLowSecurityAlertExceptionHandler(PasswordLowSecurityAlertException ex) {
        return ErrorResponseConstruct(400, "password security low", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalEmailAddressException.class)
    ResponseEntity<?> IllegalEmailAddressException(IllegalEmailAddressException ex) {
        return ErrorResponseConstruct(400, "illegal password", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ArticleNotFoundException.class)
    ResponseEntity<?> ArticleNotFoundExceptionHandler(ArticleNotFoundException ex){
        return ErrorResponseConstruct(404, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerError.class)
    ResponseEntity<?> InternalServerErrorHandler(InternalServerError ex){
        logger.error(ex);
        return ErrorResponseConstruct(500, "internal server seems down", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MeetingNameHasbeenregisteredException.class)
    ResponseEntity<?> MeetingNameHasbeenregisteredExceptionHandler(MeetingNameHasbeenregisteredException ex){
        logger.error(ex);
        return ErrorResponseConstruct(400, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AtLeastOneTopicException.class)
    ResponseEntity<?> AtLeastOneTopicExceptionHandler(AtLeastOneTopicException ex){
        logger.error(ex);
        return ErrorResponseConstruct(200, ex.getMessage(), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(MeetingOfNoExistenceException.class)
    ResponseEntity<?> MeetingOfNoExistenceExceptionHandler(MeetingOfNoExistenceException ex){
        logger.error(ex);
        return ErrorResponseConstruct(200, ex.getMessage(), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(InvitationTargetIsForbiddenException.class)
    ResponseEntity<?> InvitationTargetIsForbiddenExceptionHandler(InvitationTargetIsForbiddenException ex){
        logger.error(ex);
        return ErrorResponseConstruct(400, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MeetingStatusUnavailableForPCMemberInvitationException.class)
    ResponseEntity<?> MeetingStatusUnavailableForPCMemberInvitationExceptionHandler(MeetingStatusUnavailableForPCMemberInvitationException ex){
        logger.error(ex);
        return ErrorResponseConstruct(403, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailHasBeenRegisteredException.class)
    ResponseEntity<?> EmailHasBeenRegisteredExceptionHandler(EmailHasBeenRegisteredException ex){
        logger.error(ex);
        return ErrorResponseConstruct(400, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MeetingUnavaliableToOperateException.class)
    ResponseEntity<?> MeetingUnavaliableToOperateExceptionHandler(MeetingUnavaliableToOperateException ex){
        logger.error(ex);
        return ErrorResponseConstruct(200, ex.getMessage(), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(RejectToReviewException.class)
    ResponseEntity<?> RejectToReviewExceptionHandler(RejectToReviewException ex){
        logger.error(ex);
        return ErrorResponseConstruct(200, ex.getMessage(), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(NoneArticleToReviewException.class)
    ResponseEntity<?> NoneArticleToReviewExceptionHandler(NoneArticleToReviewException ex){
        logger.error(ex);
        return ErrorResponseConstruct(200, ex.getMessage(), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(AtLeastThreePCMemberException.class)
    ResponseEntity<?> AtLeastThreePCMemberExceptionHandler(AtLeastThreePCMemberException ex){
        logger.error(ex);
        return ErrorResponseConstruct(200, ex.getMessage(), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(MeetingStatusUnAvailableToReviewException.class)
    ResponseEntity<?> MeetingStatusUnAvailableToReviewExceptionHandler(MeetingStatusUnAvailableToReviewException ex){
        logger.error(ex);
        return ErrorResponseConstruct(403, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(cannotAssignArticlesException.class)
    ResponseEntity<?> cannotAssignArticlesExceptionHandler(cannotAssignArticlesException e){
        logger.error(e);
        return ErrorResponseConstruct(200, e.getMessage(), HttpStatus.ACCEPTED);
    }


    private ResponseEntity<?> ErrorResponseConstruct(Integer code, String reason, HttpStatus status) {
        HashMap<String, String> reasonMap = new HashMap<>();
        reasonMap.put("reason", reason);
        ResponseWrapper<HashMap<String, String>> resp = new ResponseWrapper<>(
                code,
                "fail",
                reasonMap
        );
        return new ResponseEntity<>(resp, status);
    }



}
