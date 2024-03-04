package kr.ac.kumoh.illdang100.tovalley.service.page;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.Comment;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LostFoundBoardServiceImplTest extends DummyObject {
    @InjectMocks
    private PageServiceImpl pageService;
    @Mock
    private LostFoundBoardRepository lostFoundBoardRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private LostFoundBoardImageRepository lostFoundBoardImageRepository;

    @Test
    public void getLostFoundBoardDetails() {
        // given
        long lostFoundBoardId = 1L;
        String memberEmail = "kakao_1234@naver.com";
        Member member = newMockMember(1L, "kakao_1234", "nickname1", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newWaterPlace(1L, "금오계곡", "경북", 3.5, 3);
        LostFoundBoard lostFoundBoard = newMockLostFoundBoard(1L, "지갑 찾아요", "금오계곡에서 지갑 잃어버림 검정색 지갑", member, false, LostFoundEnum.LOST, waterPlace);

        List<Comment> comments = new ArrayList<>();
        comments.add(newMockComment(1L, 1L, member));
        comments.add(newMockComment(2L, 1L, member));
        comments.add(newMockComment(3L, 1L, member));

        List<String> postImages = new ArrayList<>();
        postImages.add("https://imageUrl1.com");
        postImages.add("https://imageUrl2.com");
        postImages.add("https://imageUrl3.com");

        // stub
        when(lostFoundBoardRepository.findByIdWithMemberAndWaterPlace(anyLong())).thenReturn(Optional.of(lostFoundBoard));
        when(commentRepository.findCommentByLostFoundBoardIdFetchJoinMember(anyLong())).thenReturn(comments);
        when(lostFoundBoardImageRepository.findImageByLostFoundBoardId(anyLong())).thenReturn(postImages);
        when(commentRepository.countByLostFoundBoardId(anyLong())).thenReturn((long)comments.size());

        // when
        LostFoundBoardDetailRespDto lostFoundBoardDetail = pageService.getLostFoundBoardDetail(lostFoundBoardId, member);

        // then
        assertThat(lostFoundBoardDetail.getTitle()).isEqualTo("지갑 찾아요");
        assertThat(lostFoundBoardDetail.getContent()).isEqualTo("금오계곡에서 지갑 잃어버림 검정색 지갑");
        assertThat(lostFoundBoardDetail.getCommentCnt()).isEqualTo(3);
        assertThat(lostFoundBoardDetail.getPostImages().size()).isEqualTo(3);
    }

}