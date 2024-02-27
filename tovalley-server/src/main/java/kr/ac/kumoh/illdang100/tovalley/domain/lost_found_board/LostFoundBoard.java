package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
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
    @Column(name = "lost_found_board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_place_id")
    private WaterPlace waterPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 256)
    private String content;

    private Boolean isPosting; // 게시 여부

    private Boolean isResolved; // 해결 완료

    @Enumerated(EnumType.STRING)
    private LostFoundEnum lostFoundEnum; // 찾아요/찾았어요 카테고리

    @PrePersist
    public void prePersist() {
        this.isPosting = this.isPosting == null? true : isPosting;
        this.isResolved = this.isResolved == null? false : isResolved;
    }

    @Builder
    public LostFoundBoard(Long id, WaterPlace waterPlace, Member member, String title, String content, Boolean isPosting, Boolean isResolved, LostFoundEnum lostFoundEnum) {
        this.id = id;
        this.waterPlace = waterPlace;
        this.member = member;
        this.title = title;
        this.content = content;
        this.isPosting = isPosting;
        this.isResolved = isResolved;
        this.lostFoundEnum = lostFoundEnum;
    }

    public void updateLostFoundBoard(WaterPlace waterPlace, String title, String content, LostFoundEnum lostFoundEnum) {
        this.waterPlace = waterPlace;
        this.title = title;
        this.content = content;
        this.lostFoundEnum = lostFoundEnum;
    }

    public void updateResolvedStatus(Boolean isResolved) {
        this.isResolved = isResolved;
    }
}
