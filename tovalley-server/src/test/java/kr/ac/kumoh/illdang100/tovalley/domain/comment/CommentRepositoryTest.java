package kr.ac.kumoh.illdang100.tovalley.domain.comment;

import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class CommentRepositoryTest extends DummyObject {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WaterPlaceRepository waterPlaceRepository;

    @Autowired
    private LostFoundBoardRepository lostFoundBoardRepository;

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
    void deleteByIdAndLostFoundBoardIdTest() {
        // given
        Long commentId = 1L;
        Long lostFoundBoardId = 1L;

        // when
        int deletedCount = commentRepository.deleteByIdAndLostFoundBoardId(commentId, lostFoundBoardId);

        // then
        assertThat(deletedCount).isEqualTo(1);
    }


    private void dataSetting() {
        Member member = newMember("username", "사용자");
        memberRepository.save(member);

        WaterPlace waterPlace1 = newWaterPlace("금오계곡", "경상북도", 3.0, 3);
        waterPlaceRepository.save(waterPlace1);
        WaterPlace waterPlace2 = newWaterPlace("대구계곡", "경상북도", 3.0, 3);
        waterPlaceRepository.save(waterPlace2);

        lostFoundBoardRepository.save(newMockLostFoundBoard(1L, "title1", "1234", member, false, LostFoundEnum.LOST, waterPlace1));
        lostFoundBoardRepository.save(newMockLostFoundBoard(2L, "title2", "1234", member, true, LostFoundEnum.LOST, waterPlace1));
        lostFoundBoardRepository.save(newMockLostFoundBoard(3L, "title3", "content3", member, false, LostFoundEnum.FOUND, waterPlace2));
        lostFoundBoardRepository.save(newMockLostFoundBoard(4L, "title4", "content4", member, true, LostFoundEnum.FOUND, waterPlace2));

        commentRepository.save(newMockComment(1L, 1L, member));
        commentRepository.save(newMockComment(2L, 1L, member));
    }

    private void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE comment ALTER COLUMN comment_id RESTART WITH 1").executeUpdate();
    }
}