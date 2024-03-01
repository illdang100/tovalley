package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.notification.NotificationRespDto.ChatNotificationRespDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/api/auth/notifications")
    public ResponseEntity<?> findChatNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                   @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Slice<ChatNotificationRespDto> result
                = notificationService.getChatNotificationsByMemberId(principalDetails.getMember().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "메시지 알림 목록 조회에 성공하였습니다", result), HttpStatus.OK);
    }

    @DeleteMapping("/api/auth/notifications")
    public ResponseEntity<?> deleteNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        notificationService.deleteAllNotificationsOfMember(principalDetails.getMember().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅 메시지 알림 삭제에 성공하였습니다", null), HttpStatus.OK);
    }

    @DeleteMapping("/api/auth/notifications/{notificationId}")
    public ResponseEntity<?> deleteSingleNotification(@PathVariable("notificationId") Long notificationId,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {

        notificationService.deleteSingleChatNotification(principalDetails.getMember().getId(), notificationId);
        return new ResponseEntity<>(new ResponseDto<>(1, "채팅 메시지 알림 삭제에 성공하였습니다", null), HttpStatus.OK);
    }
}
