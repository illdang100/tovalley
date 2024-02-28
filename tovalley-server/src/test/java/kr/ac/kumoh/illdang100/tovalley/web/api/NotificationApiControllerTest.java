package kr.ac.kumoh.illdang100.tovalley.web.api;

import static kr.ac.kumoh.illdang100.tovalley.util.TokenUtil.createTestAccessToken;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotification;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotificationRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import kr.ac.kumoh.illdang100.tovalley.service.notification.NotificationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class NotificationApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatNotificationRepository chatNotificationRepository;

    @Autowired
    private JwtProcess jwtProcess;

    private String accessToken;
    private final String nickname = "일당백";
    private final String username = "username";

    @BeforeEach
    public void setUp() {
        dataSetting();
        accessToken = createTestAccessToken(memberRepository, jwtProcess, username);
    }

    @Test
    public void findChatNotifications_test() throws Exception {

        // given


        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/notifications")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("메시지 알림 목록 조회에 성공하였습니다"))
                .andExpect(jsonPath("$.data[0].hasRead").value(false))
                .andExpect(jsonPath("$.data[5].hasRead").value(true))
                .andDo(MockMvcResultHandlers.print());

        List<ChatNotification> all = chatNotificationRepository.findAll();
        assertThat(all.get(0).getHasRead()).isTrue();
        assertThat(all.get(1).getHasRead()).isTrue();
        assertThat(all.get(2).getHasRead()).isTrue();
        assertThat(all.get(3).getHasRead()).isTrue();
        assertThat(all.get(4).getHasRead()).isTrue();
        assertThat(all.get(5).getHasRead()).isTrue();
        assertThat(all.get(6).getHasRead()).isTrue();
        assertThat(all.get(7).getHasRead()).isTrue();
        assertThat(all.get(8).getHasRead()).isTrue();
        assertThat(all.get(9).getHasRead()).isTrue();
    }

    private void dataSetting() {
        Member sender = newMember("username1", "nickname1");
        Member recipient = newMember(username, nickname);
        memberRepository.save(sender);
        memberRepository.save(recipient);

        Long recipientId = recipient.getId();
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));

        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", false));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", false));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", false));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", false));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", false));

        em.clear();
    }
}