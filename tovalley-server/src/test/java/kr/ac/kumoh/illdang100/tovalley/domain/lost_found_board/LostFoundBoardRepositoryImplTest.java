package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class LostFoundBoardRepositoryImplTest extends DummyObject {
    @Autowired
    private LostFoundBoardRepository lostFoundBoardRepository;
    @Autowired
    private LostFoundBoardImageRepository lostFoundBoardImageRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
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
    @DisplayName(value = "검색어 없음, 해결 완료 포함")
    public void getLostFoundBoardListTest() {

        // given
        LostFoundBoardListReqDto lostFoundBoardListReqDto = new LostFoundBoardListReqDto(null, List.of(1L), "", true);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("desc"), "createdDate"));

        // when
        Slice<LostFoundBoardListRespDto> lostFoundBoardListBySlice = lostFoundBoardRepository.getLostFoundBoardListBySlice(lostFoundBoardListReqDto, pageRequest);

        // then
        assertThat(lostFoundBoardListBySlice.getContent()).hasSize(2);
        assertThat(lostFoundBoardListBySlice.getContent().get(0).getTitle()).isEqualTo("title2");
        assertThat(lostFoundBoardListBySlice.getContent().get(1).getTitle()).isEqualTo("title1");
    }

    @Test
    @DisplayName(value = "검색어 포함, 해결 완료 포함")
    public void getLostFoundBoardListTest_검색어() {

        // given
        LostFoundBoardListReqDto lostFoundBoardListReqDto = new LostFoundBoardListReqDto(LostFoundEnum.FOUND.toString(), List.of(2L), "content", true);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("desc"), "createdDate"));

        // when
        Slice<LostFoundBoardListRespDto> lostFoundBoardListBySlice = lostFoundBoardRepository.getLostFoundBoardListBySlice(lostFoundBoardListReqDto, pageRequest);

        // then
        assertThat(lostFoundBoardListBySlice.getContent()).hasSize(2);
    }

    @Test
    @DisplayName(value = "검색어 없음, 해결 완료 제외")
    public void getLostFoundBoardListTest_해결완료제외() {

        // given
        LostFoundBoardListReqDto lostFoundBoardListReqDto = new LostFoundBoardListReqDto(LostFoundEnum.LOST.toString(), List.of(1L), null, true);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("desc"), "createdDate"));

        // when
        Slice<LostFoundBoardListRespDto> lostFoundBoardListBySlice = lostFoundBoardRepository.getLostFoundBoardListBySlice(lostFoundBoardListReqDto, pageRequest);

        // then
        assertThat(lostFoundBoardListBySlice.getContent()).hasSize(2);
    }

    @Test
    @DisplayName(value = "카테고리 없음, 계곡 선택 안함, 검색어 없음, 해결 완료 제외")
    public void getLostFoundBoardListTest_default() {

        // given
        LostFoundBoardListReqDto lostFoundBoardListReqDto = new LostFoundBoardListReqDto(null, null, null, true);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("desc"), "createdDate"));

        // when
        Slice<LostFoundBoardListRespDto> lostFoundBoardListBySlice = lostFoundBoardRepository.getLostFoundBoardListBySlice(lostFoundBoardListReqDto, pageRequest);

        // then
        assertThat(lostFoundBoardListBySlice.getContent()).hasSize(4);
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

        commentRepository.save(newMockComment(1L, 1L));
        commentRepository.save(newMockComment(2L, 1L));
        lostFoundBoardImageRepository.save(newMockLostFoundBoardImage(1L, 3L, new ImageFile("fileName", "fileUrl")));
    }
    private void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE water_place ALTER COLUMN water_place_id RESTART WITH 1").executeUpdate();
    }
}