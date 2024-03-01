package kr.ac.kumoh.illdang100.tovalley.domain.notification;

import static kr.ac.kumoh.illdang100.tovalley.domain.member.QMember.member;
import static kr.ac.kumoh.illdang100.tovalley.domain.notification.QChatNotification.chatNotification;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

public class ChatNotificationRepositoryImpl implements ChatNotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ChatNotificationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<ChatNotification> findSliceChatNotificationsByMemberId(Long memberId, Pageable pageable) {

        JPAQuery<ChatNotification> query = queryFactory
                .select(chatNotification)
                .from(chatNotification)
                .leftJoin(chatNotification.sender, member).fetchJoin()
                .where(chatNotification.recipientId.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    chatNotification.getType(), chatNotification.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<ChatNotification> notifications = query.fetch();

        boolean hasNext = false;
        if (notifications.size() > pageable.getPageSize()) {
            notifications.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(notifications, pageable, hasNext);
    }
}
