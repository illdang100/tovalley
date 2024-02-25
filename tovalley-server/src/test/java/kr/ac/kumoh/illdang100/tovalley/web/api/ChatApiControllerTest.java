package kr.ac.kumoh.illdang100.tovalley.web.api;

import static kr.ac.kumoh.illdang100.tovalley.util.TokenUtil.createTestAccessToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoom;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.CreateNewChatRoomReqDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class ChatApiControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private JwtProcess jwtProcess;

    private String accessToken;
    private final String nickname = "일당백";
    private final String chatNickname1 = "챗멤버1";
    private final String chatNickname2 = "챗멤버2";
    private final String username = "username";

    @BeforeEach
    public void setUp() {
        dataSetting();
        accessToken = createTestAccessToken(memberRepository, jwtProcess, username);
    }

    @Test
    @DisplayName("채팅방 새로 생성하는 테스트")
    public void createChatRoom_test1() throws Exception {

        // given
        CreateNewChatRoomReqDto reqDto = new CreateNewChatRoomReqDto(chatNickname1);

        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/chatroom")
                .content(requestBody)
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("채팅방이 생성되었습니다"))
                .andExpect(jsonPath("$.data.new").value(true))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("기존 채팅방 반환하는 테스트")
    public void createChatRoom() throws Exception {

        // given
        CreateNewChatRoomReqDto reqDto = new CreateNewChatRoomReqDto(chatNickname2);

        String requestBody = om.writeValueAsString(reqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/chatroom")
                .content(requestBody)
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("기존 채팅방을 응답합니다"))
                .andExpect(jsonPath("$.data.new").value(false))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("sender - 채팅방 목록 조회 컨트롤러 테스트")
    public void findChatRoomList_sender_test() throws Exception {

        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/chatroom")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("채팅방 목록 조회를 성공했습니다"))
                .andExpect(jsonPath("$.data.content[0].chatRoomTitle").value("nickname44 와(과)의 채팅방입니다."))
                .andExpect(jsonPath("$.data.content[1].chatRoomTitle").value("nickname33 와(과)의 채팅방입니다."))
                .andExpect(jsonPath("$.data.content[2].chatRoomTitle").value("챗멤버2 와(과)의 채팅방입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("recipient - 채팅방 목록 조회 컨트롤러 테스트")
    public void findChatRoomList_recipient_test() throws Exception {

        // given
        String accessToken = createTestAccessToken(memberRepository, jwtProcess, "username3");

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/chatroom")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("채팅방 목록 조회를 성공했습니다"))
                .andExpect(jsonPath("$.data.content[0].chatRoomTitle").value("일당백 와(과)의 채팅방입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("채팅방 목록 조회 컨트롤러 테스트")
    public void findChatMessages() throws Exception {

        // given
        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/chat/messages/1")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
//                .param("lastMessageId", content6.getId())
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("채팅 메시지 목록 조회에 성공했습니다"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("채팅방 목록 조회 컨트롤러 테스트 - 사용자가 속한 채팅방이 아닌 경우")
    public void findChatMessages_failure_test() throws Exception {

        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/auth/chat/messages/3")
                .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("해당 사용자가 속한 채팅방이 아닙니다"))
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        Member sender = newMember(username, nickname);
        Member sender2 = newMember("username22", "nickname22");
        memberRepository.save(sender);
        memberRepository.save(sender2);
        memberRepository.save(newMember("username2", chatNickname1));
        Member recipient = newMember("username3", chatNickname2);
        Member recipient2 = newMember("username33", "nickname33");
        Member recipient3 = newMember("username44", "nickname44");
        memberRepository.save(recipient);
        memberRepository.save(recipient2);
        memberRepository.save(recipient3);
        ChatRoom chatRoom = chatRoomRepository.save(newChatRoom(sender, recipient));
        ChatRoom chatRoom2 = chatRoomRepository.save(newChatRoom(sender, recipient2));
        ChatRoom chatRoom3 = chatRoomRepository.save(newChatRoom(sender2, recipient2));
        ChatRoom chatRoom4 = chatRoomRepository.save(newChatRoom(sender, recipient3));
//        chatMessageRepository.save(newChatMessage(chatRoom3.getId(), recipient2.getId(), "content1"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content1"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content2"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content3"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content4"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content5"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content6"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content7"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content8"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content9"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content10"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content11"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content12"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content13"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content14"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content15"));
//
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content16"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content17"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content18"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content19"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content20"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content21"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content22"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content23"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content24"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content25"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content26"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content27"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content28"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), recipient.getId(), "content29"));
//        chatMessageRepository.save(newChatMessage(chatRoom.getId(), sender.getId(), "content30"));
        em.clear();
    }
}