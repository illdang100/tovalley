package kr.ac.kumoh.illdang100.tovalley.domain.member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import javax.persistence.EntityManager;
import kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleRespDto.SearchMembersRespDto;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class MemberRepositoryImplTest extends DummyObject {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        autoIncrementReset();
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("닉네임 입력 없이 조회 테스트")
    public void findSliceMembersByNickname_test() {

        // given
        PageRequest pageable =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nickname"));

        // when
        List<SearchMembersRespDto> content = memberRepository.findSliceMembersByNickname(null, pageable).getContent();

        // then
        assertThat(content.size()).isEqualTo(10);
        assertThat(content.get(0).getNickname()).isEqualTo("member1");
        assertThat(content.get(0).getRole()).isEqualTo("CUSTOMER");
        assertThat(content.get(1).getNickname()).isEqualTo("member10");
    }

    @Test
    @DisplayName("닉네임 입력하고 조회 테스트")
    public void findSliceMembersByNickname_test2() {

        // given
        String nickname = "member20";
        PageRequest pageable =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nickname"));

        // when
        List<SearchMembersRespDto> content = memberRepository.findSliceMembersByNickname(nickname, pageable).getContent();

        // then
        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0).getNickname()).isEqualTo("member20");
        assertThat(content.get(0).getRole()).isEqualTo("CUSTOMER");
    }

    @Test
    @DisplayName("존재하지 않는 닉네임 입력하고 조회 테스트")
    public void findSliceMembersByNickname_test3() {

        // given
        String nickname = "member888";
        PageRequest pageable =
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nickname"));

        // when
        List<SearchMembersRespDto> content = memberRepository.findSliceMembersByNickname(nickname, pageable).getContent();

        // then
        assertThat(content.size()).isEqualTo(0);
    }

    private void autoIncrementReset() {

        em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1").executeUpdate();
    }

    private void dataSetting() {
        memberRepository.save(newMember("kakao_1234", "member1"));
        memberRepository.save(newMember("kakao_5678", "member2"));
        memberRepository.save(newMember("kakao_9101", "member3"));
        memberRepository.save(newMember("kakao_1121", "member4"));
        memberRepository.save(newMember("kakao_3141", "member5"));
        memberRepository.save(newMember("kakao_4231", "member6"));

        memberRepository.save(newMember("kakao_12334", "member7"));
        memberRepository.save(newMember("kakao_56678", "member8"));
        memberRepository.save(newMember("kakao_91701", "member9"));
        memberRepository.save(newMember("kakao_11291", "member10"));
        memberRepository.save(newMember("kakao_31141", "member11"));
        memberRepository.save(newMember("kakao_42341", "member12"));

        memberRepository.save(newMember("kakao_11234", "member13"));
        memberRepository.save(newMember("kakao_56798", "member14"));
        memberRepository.save(newMember("kakao_91071", "member15"));
        memberRepository.save(newMember("kakao_11261", "member16"));
        memberRepository.save(newMember("kakao_31441", "member17"));
        memberRepository.save(newMember("kakao_43231", "member18"));

        memberRepository.save(newMember("kakao_12634", "member19"));
        memberRepository.save(newMember("kakao_56788", "member20"));
        memberRepository.save(newMember("kakao_91011", "member21"));
        memberRepository.save(newMember("kakao_11121", "member22"));
        memberRepository.save(newMember("kakao_31541", "member23"));
        memberRepository.save(newMember("kakao_42431", "member24"));

        memberRepository.save(newMember("kakao_12374", "member25"));
        memberRepository.save(newMember("kakao_56778", "member26"));
        memberRepository.save(newMember("kakao_91017", "member27"));
        memberRepository.save(newMember("kakao_11217", "member28"));
        memberRepository.save(newMember("kakao_31417", "member29"));
        memberRepository.save(newMember("kakao_42317", "member30"));
    }
}