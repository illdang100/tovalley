package kr.ac.kumoh.illdang100.tovalley.domain.notification;

import java.util.List;
import javax.persistence.EntityManager;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ChatNotificationRepositoryTest extends DummyObject {

    @Autowired
    private ChatNotificationRepository chatNotificationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        autoIncrementReset();
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    public void deleteByIdAndRecipientId_test() {
        // given
        Long chatNotificationId = 1L;
        Long recipientId = 2L;

        // when
        int count = chatNotificationRepository.deleteByIdAndRecipientId(chatNotificationId, recipientId);

        // then
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    public void deleteByRecipientId_test() {

        List<ChatNotification> all = chatNotificationRepository.findAll();
        for (ChatNotification chatNotification : all) {
            System.out.println("chatNotification = " + chatNotification);
        }
        // given
        Long recipientId = 2L;

        // when
        chatNotificationRepository.deleteByRecipientId(recipientId);

        // then
        chatNotificationRepository.findAll();

        em.clear();
        List<ChatNotification> chatNotifications = chatNotificationRepository.findAll();
        Assertions.assertThat(chatNotifications.size()).isEqualTo(4);
    }

    @Test
    public void deleteByRecipientIdAndChatRoomId_test() {
        // given
        Long recipientId = 2L;
        Long chatRoomId = 1L;

        // when
        chatNotificationRepository.deleteByRecipientIdAndChatRoomId(recipientId, chatRoomId);

        // then
        List<ChatNotification> chatNotifications = chatNotificationRepository.findAll();
        Assertions.assertThat(chatNotifications.size()).isEqualTo(4);
    }

    private void autoIncrementReset() {

        em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE chat_notification ALTER COLUMN chat_notification_id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        Member sender = memberRepository.save(newMember("username1", "nickname1"));
        Member recipient = memberRepository.save(newMember("username2", "recipientNick1"));
        Member recipient2 = memberRepository.save(newMember("username3", "recipientNick2"));

        Long recipientId = recipient.getId();
        Long recipientId2 = recipient2.getId();
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId, 1L, "test", true));

        chatNotificationRepository.save(newChatNotification(sender, recipientId2, 2L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId2, 2L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId2, 2L, "test", true));
        chatNotificationRepository.save(newChatNotification(sender, recipientId2, 2L, "test", true));
    }
}