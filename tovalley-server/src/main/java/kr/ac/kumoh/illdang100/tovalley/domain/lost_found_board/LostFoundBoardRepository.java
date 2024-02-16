package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LostFoundBoardRepository extends JpaRepository<LostFoundBoard, Long>, LostFoundBoardRepositoryCustom {

    @Query("select lfb from LostFoundBoard lfb JOIN FETCH lfb.member m where lfb.id = :lostFoundBoardId")
    Optional<LostFoundBoard> findByIdWithMember(@Param("lostFoundBoardId")long lostFoundBoardId);
}
