package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostFoundBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long waterPlaceId;

    @Column(nullable = false, length = 30)
    private String authorEmail;

    @Column(nullable = false, length = 256)
    private String content;

    private Boolean isPosting;

    private Boolean isResolved;

    @PrePersist
    public void prePersist() {
        this.isPosting = this.isPosting == null? true : isPosting;
        this.isResolved = this.isResolved == null? false : isResolved;
    }

    @Builder
    public LostFoundBoard(Long id, Long waterPlaceId, String authorEmail, String content, Boolean isPosting, Boolean isResolved) {
        this.id = id;
        this.waterPlaceId = waterPlaceId;
        this.authorEmail = authorEmail;
        this.content = content;
        this.isPosting = isPosting;
        this.isResolved = isResolved;
    }
}
