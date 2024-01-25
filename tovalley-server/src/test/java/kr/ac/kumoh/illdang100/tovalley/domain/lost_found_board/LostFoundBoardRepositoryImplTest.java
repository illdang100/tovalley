package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.Comment;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static org.assertj.core.api.Assertions.*;

@Rollback(false)
@ActiveProfiles("test")
@DataJpaTest
class LostFoundBoardRepositoryImplTest {
    @Autowired
    private LostFoundBoardRepository lostFoundBoardRepository;
    @Autowired
    private LostFoundBoardImageRepository lostFoundBoardImageRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private WaterPlaceRepository waterPlaceRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName(value = "검색어 없음, 해결 완료 포함")
    public void getLostFoundBoardListTest() {

        // given
        LostFoundBoardListReqDto lostFoundBoardListReqDto = new LostFoundBoardListReqDto(null, List.of("금오계곡"), "", true);
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("desc"), "createdDate"));

        // when
        Slice<LostFoundBoardListRespDto> lostFoundBoardListBySlice = lostFoundBoardRepository.getLostFoundBoardListBySlice(lostFoundBoardListReqDto, pageRequest);

        // then
        assertThat(lostFoundBoardListBySlice.getContent()).hasSize(4);
        assertThat(lostFoundBoardListBySlice.getContent().get(0).getId()).isEqualTo(4L);
        assertThat(lostFoundBoardListBySlice.getContent().get(1).getPostImage()).isEqualTo("fileUrl");
        assertThat(lostFoundBoardListBySlice.getContent().get(2).getContent()).isEqualTo("1234");
        assertThat(lostFoundBoardListBySlice.getContent().get(3).getCommentCnt()).isEqualTo(2);
    }

    @Test
    @DisplayName(value = "검색어 포함, 해결 완료 포함")
    public void getLostFoundBoardListTest_검색어() {

        // given
        LostFoundBoardListReqDto lostFoundBoardListReqDto = new LostFoundBoardListReqDto(LostFoundEnum.LOST.toString(), List.of("금오계곡"), "1234", true);
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
        LostFoundBoardListReqDto lostFoundBoardListReqDto = new LostFoundBoardListReqDto(LostFoundEnum.LOST.toString(), List.of("금오계곡"), null, true);
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
        waterPlaceRepository.save(
                WaterPlace.builder()
                        .id(1L)
                        .waterPlaceName("금오계곡")
                        .province("province")
                        .city("city")
                        .town("town")
                        .subLocation("subLocation")
                        .address("address")
                        .waterPlaceCategory("계곡")
                        .coordinate(new Coordinate("38.10000000", "128.10000000"))
                        .managementType("일반지역")
                        .rating(3.0)
                        .reviewCount(1)
                        .build()
        );
        lostFoundBoardRepository.save(
                LostFoundBoard.builder()
                        .id(1L)
                        .title("title1")
                        .content("1234")
                        .authorEmail("test@naver.com")
                        .isResolved(false)
                        .lostFoundEnum(LostFoundEnum.LOST)
                        .waterPlaceId(1L)
                        .build()
        );
        lostFoundBoardRepository.save(
                LostFoundBoard.builder()
                        .id(2L)
                        .title("title2")
                        .content("1234")
                        .authorEmail("test@naver.com")
                        .isResolved(true)
                        .lostFoundEnum(LostFoundEnum.LOST)
                        .waterPlaceId(1L)
                        .build()
        );
        lostFoundBoardRepository.save(
                LostFoundBoard.builder()
                        .id(3L)
                        .title("title3")
                        .content("content3")
                        .authorEmail("test@naver.com")
                        .isResolved(false)
                        .lostFoundEnum(LostFoundEnum.FOUND)
                        .waterPlaceId(1L)
                        .build()
        );
        lostFoundBoardRepository.save(
                LostFoundBoard.builder()
                        .id(4L)
                        .title("title4")
                        .content("content4")
                        .authorEmail("test@naver.com")
                        .isResolved(true)
                        .lostFoundEnum(LostFoundEnum.FOUND)
                        .waterPlaceId(1L)
                        .build()
        );
        commentRepository.save(
                Comment.builder()
                        .id(1L)
                        .lostFoundBoardId(1L)
                        .authorEmail("user@naver.com")
                        .content("comment")
                        .build()
        );
        commentRepository.save(
                Comment.builder()
                        .id(2L)
                        .lostFoundBoardId(1L)
                        .authorEmail("user@naver.com")
                        .content("comment")
                        .build()
        );
        lostFoundBoardImageRepository.save(
                LostFoundBoardImage.builder()
                        .id(1L)
                        .lostFoundBoardId(3L)
                        .imageFile(new ImageFile("fileName", "fileUrl"))
                        .build()
        );
    }

}