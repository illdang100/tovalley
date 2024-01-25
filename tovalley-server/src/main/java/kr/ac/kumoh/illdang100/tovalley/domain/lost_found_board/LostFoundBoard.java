package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostFoundBoard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long waterPlaceId;

    @Column(nullable = false, length = 30)
    private String authorEmail;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 256)
    private String content;

    private Boolean isPosting; // 게시 여부

    private Boolean isResolved; // 해결 완료

    private LostFoundEnum lostFoundEnum; // 찾아요/찾았어요 카테고리

    @PrePersist
    public void prePersist() {
        this.isPosting = this.isPosting == null? true : isPosting;
        this.isResolved = this.isResolved == null? false : isResolved;
    }

    @Builder
    public LostFoundBoard(Long id, Long waterPlaceId, String authorEmail, String title, String content, Boolean isPosting, Boolean isResolved, LostFoundEnum lostFoundEnum) {
        this.id = id;
        this.waterPlaceId = waterPlaceId;
        this.authorEmail = authorEmail;
        this.title = title;
        this.content = content;
        this.isPosting = isPosting;
        this.isResolved = isResolved;
        this.lostFoundEnum = lostFoundEnum;
    }
}
