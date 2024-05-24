package kr.ac.kumoh.illdang100.tovalley.service.comment;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.Comment;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.comment.CommentRespDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest extends DummyObject {

    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LostFoundBoardRepository lostFoundBoardRepository;

    @Test
    void saveCommentTest() {
        // given
        Long memberId = 1L;
        Long commentId = 1L;
        Long waterPlaceId = 1L;
        Long lostFoundBoardId = 1L;
        CommentSaveReqDto commentSaveReqDto = new CommentSaveReqDto("comment");

        Member member = newMockMember(memberId, "username", "nickname", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newMockWaterPlace(waterPlaceId, "금오계곡", "경상북도", 3.5, 3);
        LostFoundBoard lostFoundBoard = newMockLostFoundBoard(lostFoundBoardId, "지갑 찾아요", "지갑 보신 분", member, false, LostFoundEnum.LOST, waterPlace);
        Comment comment = newMockComment(commentId, lostFoundBoardId, member);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(lostFoundBoardRepository.findById(lostFoundBoardId)).thenReturn(Optional.of(lostFoundBoard));
        when(commentRepository.save(any())).thenReturn(comment);

        // when
        CommentSaveRespDto commentSaveRespDto = commentService.saveComment(lostFoundBoardId, commentSaveReqDto, memberId);

        // then
        assertThat(commentSaveRespDto.getCommentContent()).isEqualTo(commentSaveReqDto.getCommentContent());
        assertThat(commentSaveRespDto.getCommentAuthor()).isEqualTo(member.getNickname());
    }


    @Test
    void deleteCommentSuccess() {
        // given
        Long lostFoundBoardId = 1L;
        Long commentId = 1L;

        when(commentRepository.deleteByIdAndLostFoundBoardId(anyLong(), anyLong())).thenReturn(1);

        // when
        assertDoesNotThrow(() -> commentService.deleteComment(lostFoundBoardId, commentId, 1L));

        // then
        verify(commentRepository, times(1)).deleteByIdAndLostFoundBoardId(commentId, lostFoundBoardId);

        Comment deletedComment = commentRepository.findById(commentId).orElse(null);
        assertNull(deletedComment);
    }

}