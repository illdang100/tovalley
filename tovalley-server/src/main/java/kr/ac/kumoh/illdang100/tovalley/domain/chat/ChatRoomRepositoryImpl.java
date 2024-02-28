package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import static kr.ac.kumoh.illdang100.tovalley.domain.chat.QChatRoom.chatRoom;
import static kr.ac.kumoh.illdang100.tovalley.domain.member.QMember.member;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import kr.ac.kumoh.illdang100.tovalley.dto.chat.ChatRespDto.ChatRoomRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<ChatRoomRespDto> findSliceChatRoomsByMemberId(Long memberId, Pageable pageable) {

        JPAQuery<ChatRoomRespDto> query = queryFactory
                .select(Projections.constructor(ChatRoomRespDto.class,
                        chatRoom.id,
                        new CaseBuilder()
                                .when(chatRoom.sender.id.eq(memberId))
                                .then(chatRoom.recipient.nickname.concat(" 와(과)의 채팅방입니다."))
                                .otherwise(chatRoom.sender.nickname.concat(" 와(과)의 채팅방입니다.")),
                        new CaseBuilder()
                                .when(chatRoom.sender.id.eq(memberId))
                                .then(chatRoom.recipient.imageFile.storeFileUrl)
                                .otherwise(chatRoom.sender.imageFile.storeFileUrl),
                        new CaseBuilder()
                                .when(chatRoom.sender.id.eq(memberId))
                                .then(chatRoom.recipient.nickname)
                                .otherwise(chatRoom.sender.nickname),
                        chatRoom.createdDate
                ))
                .from(chatRoom)
                .where(chatRoom.sender.id.eq(memberId)
                        .or(chatRoom.recipient.id.eq(memberId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<ChatRoomRespDto> result = query.fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
