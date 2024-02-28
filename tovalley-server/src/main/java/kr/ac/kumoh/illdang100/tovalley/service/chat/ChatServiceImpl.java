package kr.ac.kumoh.illdang100.tovalley.service.chat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessage;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatMessageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotification;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatNotificationRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoom;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.ChatRoomRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Message;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.Notification;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.kafka.NotificationType;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.redis.ChatRoomParticipant;
import kr.ac.kumoh.illdang100.tovalley.domain.chat.redis.ChatRoomParticipantRedisRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatReqDto.CreateNewChatRoomReqDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageListRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatMessageRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.CreateNewChatRoomRespDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.service.notification.NotificationService;
import kr.ac.kumoh.illdang100.tovalley.util.ChatUtil;
import kr.ac.kumoh.illdang100.tovalley.util.KafkaVO;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final KafkaSender kafkaSender;
    private final ChatRoomParticipantRedisRepository chatRoomParticipantRedisRepository;
    private final ChatNotificationRepository chatNotificationRepository;
    private final MongoTemplate mongoTemplate;
    private final NotificationService notificationService;

    /**
     * 특정 상대방과의 채팅방이 존재하지 않는다면, 새로운 채팅방 생성해서 응답 만약 상대방과 기존 채팅방이 존재한다면 기존 채팅방 pk 응답
     *
     * @param senderId   채팅방 생성을 요청하는 사용자 pk
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public CreateNewChatRoomRespDto createOrGetChatRoom(Long senderId, CreateNewChatRoomReqDto requestDto) {

        // 요청하는 사용자와 상대방 간에 채팅방이 있는지 확인하는 로직 필요
        String recipientNick = requestDto.getRecipientNick();

        Optional<ChatRoom> findChatRoomOpt
                = chatRoomRepository.findByMemberIdAndOtherMemberNickname(senderId, recipientNick);

        // 존재한다면 기존 채팅방 Pk 반환
        if (findChatRoomOpt.isPresent()) {
            return new CreateNewChatRoomRespDto(false, findChatRoomOpt.get().getId());
        }

        List<Member> members = memberRepository.findByIdOrNickname(senderId, recipientNick);
        validateMembers(members);

        Member sender = getSender(members, senderId);
        Member recipient = getRecipient(members, senderId);

        return createNewChatRoom(sender, recipient);
    }

    private void validateMembers(List<Member> members) {
        if (members.size() != 2) {
            throw new CustomApiException("사용자가 존재하지 않습니다");
        }
    }

    private Member getSender(List<Member> members, Long senderId) {
        for (Member member : members) {
            if (member.getId().equals(senderId)) {
                return member;
            }
        }
        throw new CustomApiException("발신자를 찾을 수 없습니다");
    }

    private Member getRecipient(List<Member> members, Long senderId) {
        for (Member member : members) {
            if (!member.getId().equals(senderId)) {
                return member;
            }
        }
        throw new CustomApiException("수신자를 찾을 수 없습니다");
    }

    private CreateNewChatRoomRespDto createNewChatRoom(Member sender, Member recipient) {
        ChatRoom chatRoom = ChatRoom.createNewChatRoom(sender, recipient);
        ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);
        return new CreateNewChatRoomRespDto(true, createdChatRoom.getId());
    }

    /**
     * 채팅방 목록을 Slice 형태로 전달
     *
     * @param memberId 채팅방 목록을 요청하는 사용자 pk
     * @param pageable 페이징 상세 정보
     * @return 채팅방 목록
     */
    @Override
    public Slice<ChatRoomRespDto> getChatRooms(Long memberId, Pageable pageable) {

        Slice<ChatRoomRespDto> sliceChatRoomsByMemberId
                = chatRoomRepository.findSliceChatRoomsByMemberId(memberId, pageable);

        List<ChatRoomRespDto> processedChatRooms = processChatRooms(sliceChatRoomsByMemberId.getContent(), memberId);

        // 마지막 메시지 시간으로 내림차순 정렬
        List<ChatRoomRespDto> sortedChatRooms = sortChatRoomsByLastMessageTime(processedChatRooms);

        return new SliceImpl<>(sortedChatRooms, pageable, sliceChatRoomsByMemberId.hasNext());
    }

    private List<ChatRoomRespDto> processChatRooms(List<ChatRoomRespDto> chatRooms, Long memberId) {
        for (ChatRoomRespDto chatRoom : chatRooms) {
            processSingleChatRoom(chatRoom, memberId);
        }
        return chatRooms;
    }

    private void processSingleChatRoom(ChatRoomRespDto chatRoom, Long memberId) {
        long unreadMessageCount = countUnReadMessages(chatRoom.getChatRoomId(), memberId);
        chatRoom.changeUnReadMessageCount(unreadMessageCount);
        updateChatRoomWithLastMessageIfExists(chatRoom);
    }

    private void updateChatRoomWithLastMessageIfExists(ChatRoomRespDto chatRoom) {
        Optional<ChatMessage> lastMessageOpt = findLastMessageInChatRoom(chatRoom.getChatRoomId());
        lastMessageOpt.ifPresent((lastMessage) -> updateChatRoomWithLastMessage(chatRoom, lastMessage));
    }

    private Optional<ChatMessage> findLastMessageInChatRoom(Long chatRoomId) {
        return chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
    }

    private void updateChatRoomWithLastMessage(ChatRoomRespDto chatRoom, ChatMessage lastMessage) {
        chatRoom.changeLastMessage(lastMessage.getContent(),
                ChatUtil.convertZdtStringToLocalDateTime(lastMessage.getCreatedAt()));
    }

    private long countUnReadMessages(Long chatRoomId, Long memberId) {
        Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
                .and("readCount").is(1)
                .and("senderId").ne(memberId));

        return mongoTemplate.count(query, ChatMessage.class);
    }

    private List<ChatRoomRespDto> sortChatRoomsByLastMessageTime(List<ChatRoomRespDto> chatRooms) {
        return chatRooms.stream()
                .sorted(Comparator.comparing(ChatRoomRespDto::getLastMessageTime,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 채팅 메시지 목록 조회
     *
     * @param memberId   채팅 메시지 목록을 요청하는 사용자 pk
     * @param chatRoomId 채팅방 pk
     * @param pageable   페이징 상세 정보
     * @return 채팅 메시지 목록
     */
    @Override
    public ChatMessageListRespDto getChatMessages(Long memberId, Long chatRoomId, String lastChatMessageId,
                                                  Pageable pageable) {
        // 0. 요청한 사용자가 속한 채팅방이 맞는지 검증
        validateChatRoom(memberId, chatRoomId);

        // 1. MongoDB로부터 원하는 페이지 번호와 크기에 해당하는 데이터를 가져온다.
        Slice<ChatMessage> chatMessageSlice = getChatMessageSlice(chatRoomId, lastChatMessageId, pageable);

        // 2. 가져온 데이터를 DTO로 변환한다.
        List<ChatMessageRespDto> chatMessageRespDtos = convertToChatMessageRespDto(chatMessageSlice.getContent(),
                memberId);

        // 3. 변환된 DTO를 기반으로 새로운 Slice를 생성한다.
        Slice<ChatMessageRespDto> chatMessageRespDtoSlice
                = new SliceImpl<>(chatMessageRespDtos, pageable, chatMessageSlice.hasNext());

        // 4. Slice 객체와 함께 응답 DTO를 생성하고 반환한다.
        return new ChatMessageListRespDto(memberId, chatRoomId, chatMessageRespDtoSlice);
    }

    private void validateChatRoom(Long memberId, Long chatRoomId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByIdAndMemberId(chatRoomId, memberId);
        if (chatRoom.isEmpty()) {
            throw new CustomApiException("해당 사용자가 속한 채팅방이 아닙니다");
        }
    }

    private Slice<ChatMessage> getChatMessageSlice(Long chatRoomId, String lastChatMessageId, Pageable pageable) {
        return lastChatMessageId == null
                ? chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable)
                : findChatMessagesWithObjectId(chatRoomId, lastChatMessageId, pageable);
    }

    public Slice<ChatMessage> findChatMessagesWithObjectId(Long chatRoomId, String lastChatMessageId,
                                                           Pageable pageable) {
        Query query = getChatMessagesQuery(chatRoomId, lastChatMessageId, pageable);
        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class);
        return createChatMessagesSlice(messages, pageable);
    }

    private Query getChatMessagesQuery(Long chatRoomId, String lastChatMessageId, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("chatRoomId").is(chatRoomId)
                .andOperator(Criteria.where("_id").lt(new ObjectId(lastChatMessageId))));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        query.limit(pageable.getPageSize() + 1);
        return query;
    }

    private Slice<ChatMessage> createChatMessagesSlice(List<ChatMessage> messages, Pageable pageable) {
        boolean hasNext = messages.size() > pageable.getPageSize();
        if (hasNext) {
            messages = messages.subList(0, pageable.getPageSize());
        }
        return new SliceImpl<>(messages, pageable, hasNext);
    }

    private List<ChatMessageRespDto> convertToChatMessageRespDto(List<ChatMessage> chatMessages, Long memberId) {
        return chatMessages.stream()
                .map(chatMessage -> new ChatMessageRespDto(chatMessage, memberId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void sendMessage(Message message, Long senderId) {

        Long chatRoomId = message.getChatRoomId();
        boolean allMembersParticipatingInChatRoom = isAllMembersParticipatingInChatRoom(chatRoomId);
        processMessage(message, senderId, allMembersParticipatingInChatRoom);

        ChatMessage chatMessage = message.convertToChatMessage();
        chatMessageRepository.save(chatMessage);

        if (!allMembersParticipatingInChatRoom) {
            // 상대방에게 알림 전송
            notificationService.sendNotification(message, senderId, chatRoomId);
        }

        // 메시지 전송
        kafkaSender.sendMessage(KafkaVO.KAFKA_CHAT_TOPIC, message);
    }

    private boolean isAllMembersParticipatingInChatRoom(Long chatRoomId) {
        List<ChatRoomParticipant> chatRoomParticipants = getChatRoomParticipants(chatRoomId);
        return chatRoomParticipants.size() == ChatUtil.MAX_PARTICIPANTS_PER_CHATROOM;
    }

    private void processMessage(Message message, Long senderId, boolean allMembersParticipatingInChatRoom) {
        int readCount = calculateReadCount(allMembersParticipatingInChatRoom);
        updateMessageBeforeSending(message, senderId, readCount);
    }

    private int calculateReadCount(boolean allMembersParticipatingInChatRoom) {
        return allMembersParticipatingInChatRoom ? 0 : 1;
    }

    private void updateMessageBeforeSending(Message message, Long senderId, int readCount) {
        message.prepareMessageForSending(senderId, ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString(), readCount);
    }

    @Override
    public void saveChatRoomParticipantToRedis(Long memberId, Long chatRoomId) {
        ChatRoomParticipant chatRoomParticipant = new ChatRoomParticipant(memberId, chatRoomId);
        chatRoomParticipantRedisRepository.save(chatRoomParticipant);
    }

    @Override
    public void deleteChatRoomParticipantFromRedis(Long memberId) {
        List<ChatRoomParticipant> chatRoomParticipants
                = chatRoomParticipantRedisRepository.findByMemberId(memberId);

        if (chatRoomParticipants != null && !chatRoomParticipants.isEmpty()) {
            chatRoomParticipantRedisRepository.deleteAll(chatRoomParticipants);
        }
    }

    @Override
    public void updateUnreadMessages(Long memberId, Long chatRoomId) {
        Query query = new Query(Criteria.where("chatRoomId")
                .is(chatRoomId)
                .and("readCount").is(1)
                .and("senderId").ne(memberId));

        Update update = new Update().set("readCount", 0);
        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }

    @Override
    public Optional<Long> getOtherMemberIdByChatRoomId(Long chatRoomId, Long memberId) {
        List<ChatRoomParticipant> chatRoomParticipants = getChatRoomParticipants(chatRoomId);

        return chatRoomParticipants == null ? Optional.empty() : findOtherMember(chatRoomParticipants, memberId);
    }

    private List<ChatRoomParticipant> getChatRoomParticipants(Long chatRoomId) {
        return chatRoomParticipantRedisRepository.findByChatRoomId(chatRoomId);
    }

    private Optional<Long> findOtherMember(List<ChatRoomParticipant> chatRoomParticipants, Long memberId) {
        return chatRoomParticipants.stream()
                .map(ChatRoomParticipant::getMemberId)
                .filter(id -> !id.equals(memberId))
                .findFirst();
    }
}
