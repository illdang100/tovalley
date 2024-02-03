package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LostFoundBoardRepository extends JpaRepository<LostFoundBoard, Long>, LostFoundBoardRepositoryCustom {
}
