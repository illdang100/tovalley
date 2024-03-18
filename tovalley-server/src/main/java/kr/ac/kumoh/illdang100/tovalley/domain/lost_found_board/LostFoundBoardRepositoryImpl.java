package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.LostFoundBoardListReqDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;

import java.util.List;
import org.springframework.data.domain.Sort;

import static kr.ac.kumoh.illdang100.tovalley.domain.comment.QComment.*;
import static kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.QLostFoundBoard.*;
import static kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.QLostFoundBoardImage.*;
import static kr.ac.kumoh.illdang100.tovalley.domain.member.QMember.*;
import static kr.ac.kumoh.illdang100.tovalley.domain.water_place.QWaterPlace.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.ListUtil.*;

public class LostFoundBoardRepositoryImpl implements LostFoundBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public LostFoundBoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Slice<LostFoundBoardListRespDto> getLostFoundBoardListBySlice(LostFoundBoardListReqDto lostFoundBoardListReqDto, Pageable pageable) {
        JPAQuery<LostFoundBoardListRespDto> query = queryFactory
                .select(Projections.constructor(LostFoundBoardListRespDto.class,
                        lostFoundBoard.id,
                        lostFoundBoard.title,
                        lostFoundBoard.content,
                        member.nickname,
                        JPAExpressions.select(comment.id.count()).from(comment).where(comment.lostFoundBoardId.eq(lostFoundBoard.id)),
                        lostFoundBoard.createdDate,
                        JPAExpressions.select(lostFoundBoardImage.imageFile.storeFileUrl).from(lostFoundBoardImage).where(lostFoundBoardImage.lostFoundBoardId.eq(lostFoundBoard.id)).limit(1),
                        lostFoundBoard.lostFoundEnum
                ))
                .from(lostFoundBoard)
                .leftJoin(lostFoundBoard.waterPlace, waterPlace)
                .join(member).on(lostFoundBoard.member.id.eq(member.id))
                .groupBy(lostFoundBoard.id);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 카테고리(LOST / FOUND)
        String category = lostFoundBoardListReqDto.getCategory();
        if (category != null) {
            booleanBuilder.and(lostFoundBoard.lostFoundEnum.eq(LostFoundEnum.valueOf(category)));
        }

        // 검색어 (제목, 내용 포함)
        String searchWord = lostFoundBoardListReqDto.getSearchWord();
        if (searchWord != null && StringUtils.isBlank(searchWord)) {
            booleanBuilder.andAnyOf(
                    lostFoundBoard.title.toLowerCase().containsIgnoreCase(searchWord.toLowerCase()),
                    lostFoundBoard.content.toLowerCase().containsIgnoreCase(searchWord.toLowerCase())
            );
        }

        // 계곡 아이디
        List<Long> waterPlaceIds = lostFoundBoardListReqDto.getWaterPlaceId();
        if (!isEmptyList(waterPlaceIds)) {
            booleanBuilder.and(lostFoundBoard.waterPlace.id.in(waterPlaceIds));
        }

        // 해결된 게시글 제외
        if (!lostFoundBoardListReqDto.isResolved()) {
            booleanBuilder.and(lostFoundBoard.isResolved.eq(false));
        }
        List<LostFoundBoardListRespDto> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lostFoundBoard.createdDate.desc())
                .where(booleanBuilder)
                .fetch();

        return new SliceImpl<>(results, pageable, hasNextPage(results, pageable.getPageSize()));
    }

    @Override
    public Slice<MyLostFoundBoardRespDto> findSliceMyLostFoundBoardsByMemberId(Long memberId, Pageable pageable) {

        JPAQuery<MyLostFoundBoardRespDto> query = queryFactory
                .select(Projections.constructor(MyLostFoundBoardRespDto.class,
                        lostFoundBoard.id,
                        lostFoundBoard.title,
                        lostFoundBoard.createdDate
                ))
                .from(lostFoundBoard)
                .where(lostFoundBoard.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    lostFoundBoard.getType(), lostFoundBoard.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<MyLostFoundBoardRespDto> result = query.fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private boolean hasNextPage(List<LostFoundBoardListRespDto> contents, int pageSize) {
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            return true;
        }
        return false;
    }

}
