package kr.ac.kumoh.illdang100.tovalley.domain.comment;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lostFoundBoardId;

    @Column(nullable = false, length = 30)
    private String authorEmail;

    @Column(nullable = false, length = 256)
    private String content;

    @Builder
    public Comment(Long id, Long lostFoundBoardId, String authorEmail, String content) {
        this.id = id;
        this.lostFoundBoardId = lostFoundBoardId;
        this.authorEmail = authorEmail;
        this.content = content;
    }
}
