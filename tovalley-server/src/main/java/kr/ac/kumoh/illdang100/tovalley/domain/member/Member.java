package kr.ac.kumoh.illdang100.tovalley.domain.member;

import kr.ac.kumoh.illdang100.tovalley.domain.BaseTimeEntity;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true,nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 30)
    private String memberName;

    @Column(unique = true, length = 20)
    private String nickname;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(length = 60) // 패스워드 인코딩(BCrypt)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberEnum role; // ADMIN, CUSTOMER

    @Embedded
    private ImageFile imageFile;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeImageFile(ImageFile imageFile) {
        this.imageFile = imageFile;
    }
}
