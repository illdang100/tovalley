package kr.ac.kumoh.illdang100.tovalley.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentByLostFoundBoardId(Long lostFoundBoardId);

    long countByLostFoundBoardId(Long lostFoundBoardId);

    @Query("select c from Comment c JOIN FETCH c.member m where c.lostFoundBoardId = :lostFoundBoardId")
    List<Comment> findCommentByLostFoundBoardIdFetchJoinMember(@Param("lostFoundBoardId")Long lostFoundBoardId);
}
