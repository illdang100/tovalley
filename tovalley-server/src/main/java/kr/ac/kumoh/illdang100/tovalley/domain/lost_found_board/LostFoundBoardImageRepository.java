package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LostFoundBoardImageRepository extends JpaRepository<LostFoundBoardImage, Long> {
    @Query("SELECT li.imageFile.storeFileUrl FROM LostFoundBoardImage li where li.lostFoundBoardId = :lostFoundBoardId")
    List<String> findImageByLostFoundBoardId(@Param("lostFoundBoardId")Long lostFoundBoardId);

    List<LostFoundBoardImage> findByLostFoundBoardId(Long lostFoundBoardId);
}
