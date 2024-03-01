package kr.ac.kumoh.illdang100.tovalley.domain.comment;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private Long lostFoundBoardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 256)
    private String content;

    @Builder
    public Comment(Long id, Long lostFoundBoardId, Member member, String content) {
        this.id = id;
        this.lostFoundBoardId = lostFoundBoardId;
        this.member = member;
        this.content = content;
    }
}
