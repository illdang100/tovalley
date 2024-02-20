package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Pageable;

@ActiveProfiles("test")
@DataJpaTest
class ChatRoomRepositoryTest extends DummyObject {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

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
    public void findSliceChatRoomsByMemberId_test() {

        // given
        Long memberId = 1L; // 또는 테스트하려는 특정 멤버의 ID
        int pageSize = 10; // 페이지 크기 설정
        Pageable pageable = PageRequest.of(0, pageSize);

        // when
        Slice<ChatRoomRespDto> sliceChatRooms = chatRoomRepository.findSliceChatRoomsByMemberId(memberId, pageable);
        List<ChatRoomRespDto> content = sliceChatRooms.getContent();
        // then
        assertThat(sliceChatRooms.getContent()).isNotEmpty(); // 내용이 비어있지 않음
        assertThat(sliceChatRooms.getSize()).isEqualTo(pageSize); // 페이지 크기가 일치
        assertThat(content.get(0).getChatRoomTitle()).isEqualTo("recipientNick1 와(과)의 채팅방입니다.");
        assertThat(content.get(3).getChatRoomTitle()).isEqualTo("recipientNick4 와(과)의 채팅방입니다.");
    }

    private void autoIncrementReset() {

        em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE chat_room ALTER COLUMN chat_room_id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        Member sender = memberRepository.save(newMember("username1", "nickname1"));
        Member recipient1 = memberRepository.save(newMember("username2", "recipientNick1"));
        Member recipient2 = memberRepository.save(newMember("username3", "recipientNick2"));
        Member recipient3 = memberRepository.save(newMember("username4", "recipientNick3"));
        Member recipient4 = memberRepository.save(newMember("username5", "recipientNick4"));
        Member recipient5 = memberRepository.save(newMember("username6", "recipientNick5"));

        chatRoomRepository.save(newChatRoom(sender, recipient1));
        chatRoomRepository.save(newChatRoom(sender, recipient2));
        chatRoomRepository.save(newChatRoom(sender, recipient3));
        chatRoomRepository.save(newChatRoom(sender, recipient4));
        chatRoomRepository.save(newChatRoom(sender, recipient5));
    }
}