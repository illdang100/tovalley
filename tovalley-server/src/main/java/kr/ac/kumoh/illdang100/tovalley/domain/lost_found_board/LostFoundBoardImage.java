package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostFoundBoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lostFoundBoardId;

    private ImageFile imageFile;

    @Builder
    public LostFoundBoardImage(Long id, Long lostFoundBoardId, ImageFile imageFile) {
        this.id = id;
        this.lostFoundBoardId = lostFoundBoardId;
        this.imageFile = imageFile;
    }
}
