package kr.ac.kumoh.illdang100.tovalley.service.comment;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.Comment;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

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