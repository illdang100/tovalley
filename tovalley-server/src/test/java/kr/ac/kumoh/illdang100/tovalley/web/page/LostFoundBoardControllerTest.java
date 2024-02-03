package kr.ac.kumoh.illdang100.tovalley.web.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class LostFoundBoardControllerTest extends DummyObject {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LostFoundBoardRepository lostFoundBoardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WaterPlaceRepository waterPlaceRepository;

    @BeforeEach
    public void setUp() {
        dataSetting();
    }

    @Test
    @WithUserDetails(value = "kakao_123", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName(value = "기본 파라미터 - 카테고리, 물놀이 장소 아이디 1개, 검색어, 해결 완료 여부")
    void getLostFoundBoardList() throws Exception {
        // given
        long valleyId = 1L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/lostItem?" + "category=LOST&valleyId=" + valleyId + "&searchWord=지갑&isResolved=false")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithUserDetails(value = "kakao_123", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName(value = "물놀이 장소 아이디 2개")
    void getLostFoundBoardList_valleys() throws Exception {
        // given
        long valleyId1 = 1L;
        long valleyId2 = 2L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/lostItem?" + "category=LOST&valleyId=" + valleyId1 + "&valleyId=" + valleyId2 + "&searchWord=지갑&isResolved=false")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithUserDetails(value = "kakao_123", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName(value = "카테고리 형식 에러")
    void getLostFoundBoardList_category_error() throws Exception {
        // given
        long valleyId = 1L;

        // when
        ResultActions resultActions = mvc.perform(get("/api/lostItem?category=error&" + "valleyId" + valleyId + "&searchWord=지갑&isResolved=false")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithUserDetails(value = "kakao_123", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName(value = "파라미터 생략 - 물놀이 장소 아이디")
    void getLostFoundBoardList_noPram() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/lostItem?category=LOST&&searchWord=지갑&isResolved=false")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        Member member = newMember("kakao_123", "일당백");
        memberRepository.save(member);

        WaterPlace waterPlace1 = newWaterPlace(1L, "금오계곡", "경상북도", 3.0, 3);
        waterPlaceRepository.save(waterPlace1);
        WaterPlace waterPlace2 = newWaterPlace(2L, "대구계곡", "경상북도", 3.0, 3);
        waterPlaceRepository.save(waterPlace2);

        lostFoundBoardRepository.save(newLostFoundBoard(1L, "지갑 보신 분", "지갑", member, false, LostFoundEnum.LOST, waterPlace1));
        lostFoundBoardRepository.save(newLostFoundBoard(2L, "지갑 찾습니다.", "지갑", member, false, LostFoundEnum.LOST, waterPlace1));
        lostFoundBoardRepository.save(newLostFoundBoard(3L, "폰 찾았어요", "아이폰14", member, false, LostFoundEnum.FOUND, waterPlace2));
        lostFoundBoardRepository.save(newLostFoundBoard(4L, "폰 잃어버리신 분", "갤럭시S20", member, true, LostFoundEnum.FOUND, waterPlace2));

        commentRepository.save(newComment(1L, 1L));
        commentRepository.save(newComment(2L, 1L));

        em.clear();
    }
}