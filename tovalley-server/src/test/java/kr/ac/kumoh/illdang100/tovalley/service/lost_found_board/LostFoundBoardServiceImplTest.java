package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.service.page.PageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.ImageUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LostFoundBoardServiceImplTest extends DummyObject {
    @InjectMocks
    private LostFoundBoardServiceImpl lostFoundBoardService;
    @InjectMocks
    private PageServiceImpl pageService;
    @Mock
    private LostFoundBoardRepository lostFoundBoardRepository;
    @Mock
    private WaterPlaceRepository waterPlaceRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private LostFoundBoardImageRepository lostFoundBoardImageRepository;

    @Test
    @DisplayName(value = "분실물 게시글 수정")
    public void updateLostFoundBoardTest() {
        // given
        Long memberId = 1L;
        Long waterPlaceId = 1L;
        Long lostFoundBoardId = 1L;

        LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto = new LostFoundBoardUpdateReqDto(lostFoundBoardId, "LOST", waterPlaceId, "잃어버렸어요ㅠㅠ", "지갑 보신 분 계신가요?", null, null);

        // stub
        Member member = newMockMember(memberId, "kakao_1234", "일당백", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newWaterPlace(waterPlaceId, "금오계곡", "경북", 3.0, 3);
        LostFoundBoard lostFoundBoard = newMockLostFoundBoard(lostFoundBoardId, "title", "content", member, false, LostFoundEnum.LOST, waterPlace);

        when(lostFoundBoardRepository.findByIdWithMemberAndWaterPlace(lostFoundBoardId)).thenReturn(Optional.of(lostFoundBoard));
        when(waterPlaceRepository.findById(waterPlaceId)).thenReturn(Optional.of(waterPlace));

        // when
        lostFoundBoardService.updateLostFoundBoard(lostFoundBoardUpdateReqDto, memberId);

        // stub2
        LostFoundBoardDetailRespDto lostFoundBoardDetail = pageService.getLostFoundBoardDetail(lostFoundBoardId, member);

        // then
        assertEquals(lostFoundBoardUpdateReqDto.getTitle(), lostFoundBoardDetail.getTitle());
        assertEquals(lostFoundBoardUpdateReqDto.getContent(), lostFoundBoardDetail.getContent());
    }

    @Test
    @DisplayName(value = "분실물 게시글 수정 - 이미지 개수 초과 (현재: 5개, 삭제: 2개, 추가: 5개")
    public void updateLostFoundBoardExceedImageCountTest() {
        // given
        Long memberId = 1L;
        Long waterPlaceId = 1L;
        Long lostFoundBoardId = 1L;

        List<MultipartFile> newImages = new ArrayList<>();
        List<String> deleteImages = Arrays.asList("fileUrl1", "fileUrl2");
        for (int i = 0; i < MAX_IMAGE_COUNT; i++) {
            newImages.add(new MockMultipartFile("file" + i, "filename" + i + ".jpg", "image/jpeg", new byte[0]));
        }
        LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto = new LostFoundBoardUpdateReqDto(
                lostFoundBoardId, "LOST", waterPlaceId, "잃어버렸어요ㅠㅠ", "지갑 보신 분 계신가요?", newImages, deleteImages
        );

        // stub
        Member member = newMockMember(memberId, "kakao_1234", "일당백", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newWaterPlace(waterPlaceId, "금오계곡", "경북", 3.0, 3);
        LostFoundBoard lostFoundBoard = newMockLostFoundBoard(lostFoundBoardId, "title", "content", member, false, LostFoundEnum.LOST, waterPlace);

        List<String> imageUrlList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            imageUrlList.add( "fileUrl" + i);
        }

        when(lostFoundBoardRepository.findByIdWithMemberAndWaterPlace(lostFoundBoardId)).thenReturn(Optional.of(lostFoundBoard));
        when(waterPlaceRepository.findById(waterPlaceId)).thenReturn(Optional.of(waterPlace));

        try {
            // when
            lostFoundBoardService.updateLostFoundBoard(lostFoundBoardUpdateReqDto, memberId);
        } catch (CustomApiException e) {
            // then
            assertEquals("게시글에 저장할 수 있는 최대 이미지 개수를 초과했습니다", e.getMessage());
        }
    }

    @Test
    @DisplayName("분실물 게시글 수정 - 새로 추가되는 이미지 파일의 개수가 최대 허용 개수를 초과하는 경우")
    public void updateLostFoundBoardExceedMaxImageCountTest() {
        // given
        Long memberId = 1L;
        Long waterPlaceId = 1L;
        Long lostFoundBoardId = 1L;

        List<MultipartFile> newImages = new ArrayList<>();
        List<String> deleteImages = Arrays.asList("fileUrl1", "fileUrl2");
        for (int i = 0; i < MAX_IMAGE_COUNT + 1; i++) {
            newImages.add(new MockMultipartFile("file" + i, "filename" + i + ".jpg", "image/jpeg", new byte[0]));
        }
        LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto = new LostFoundBoardUpdateReqDto(
                lostFoundBoardId, "LOST", waterPlaceId, "잃어버렸어요ㅠㅠ", "지갑 보신 분 계신가요?", newImages, deleteImages
        );

        // stub
        Member member = newMockMember(memberId, "kakao_1234", "일당백", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newWaterPlace(waterPlaceId, "금오계곡", "경북", 3.0, 3);
        LostFoundBoard lostFoundBoard = newMockLostFoundBoard(lostFoundBoardId, "title", "content", member, false, LostFoundEnum.LOST, waterPlace);

        List<String> imageUrlList = new ArrayList<>();
        for (int i = 0; i < MAX_IMAGE_COUNT; i++) {
            imageUrlList.add( "fileUrl" + i);
        }

        when(lostFoundBoardRepository.findByIdWithMemberAndWaterPlace(lostFoundBoardId)).thenReturn(Optional.of(lostFoundBoard));
        when(waterPlaceRepository.findById(waterPlaceId)).thenReturn(Optional.of(waterPlace));

        try {
            // when
            lostFoundBoardService.updateLostFoundBoard(lostFoundBoardUpdateReqDto, memberId);
        } catch (CustomApiException e) {
            // then
            assertEquals("게시글에 저장할 수 있는 최대 이미지 개수를 초과했습니다", e.getMessage());
        }
    }

    @Test
    @DisplayName("분실물 게시글 수정 - 이미지 파일을 추가하지 않거나 삭제하지 않는 경우")
    public void updateLostFoundBoardNoImageChangeTest() {
        // given
        Long memberId = 1L;
        Long waterPlaceId = 1L;
        Long lostFoundBoardId = 1L;

        List<MultipartFile> newImages = new ArrayList<>();
        List<String> deleteImages = new ArrayList<>();
        LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto = new LostFoundBoardUpdateReqDto(
                lostFoundBoardId, "LOST", waterPlaceId, "잃어버렸어요ㅠㅠ", "지갑 보신 분 계신가요?", newImages, deleteImages
        );

        // stub
        Member member = newMockMember(memberId, "kakao_1234", "일당백", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newWaterPlace(waterPlaceId, "금오계곡", "경북", 3.0, 3);
        LostFoundBoard lostFoundBoard = newMockLostFoundBoard(lostFoundBoardId, "title", "content", member, false, LostFoundEnum.LOST, waterPlace);

        List<String> imageUrlList = new ArrayList<>();
        for (int i = 0; i < MAX_IMAGE_COUNT; i++) {
            imageUrlList.add( "fileUrl" + i);
        }

        when(lostFoundBoardRepository.findByIdWithMemberAndWaterPlace(lostFoundBoardId)).thenReturn(Optional.of(lostFoundBoard));
        when(waterPlaceRepository.findById(waterPlaceId)).thenReturn(Optional.of(waterPlace));
        when(lostFoundBoardImageRepository.findImageByLostFoundBoardId(lostFoundBoardId)).thenReturn(imageUrlList);

        // when
        lostFoundBoardService.updateLostFoundBoard(lostFoundBoardUpdateReqDto, memberId);

        // stub2
        LostFoundBoardDetailRespDto lostFoundBoardDetail = pageService.getLostFoundBoardDetail(lostFoundBoardId, member);

        // then
        assertEquals(lostFoundBoardDetail.getPostImages().size(), imageUrlList.size());
    }

    @Test
    @DisplayName("게시물 해결완료 상태 변경(해결 완료 -> 해결 미완료")
    public void updateResolvedStatusToNotResolved() {
        // given
        Long memberId = 1L;
        Long waterPlaceId = 1L;
        Long lostFoundBoardId = 1L;

        // stub
        Member member = newMockMember(memberId, "kakao_1234", "일당백", MemberEnum.CUSTOMER);
        WaterPlace waterPlace = newWaterPlace(waterPlaceId, "금오계곡", "경북", 3.0, 3);
        LostFoundBoard lostFoundBoard = newMockLostFoundBoard(lostFoundBoardId, "title", "content", member, false, LostFoundEnum.LOST, waterPlace);

        when(lostFoundBoardRepository.findById(waterPlaceId)).thenReturn(Optional.of(lostFoundBoard));

        // when
        lostFoundBoardService.updateResolvedStatus(lostFoundBoardId, true);

        // stub2
        when(lostFoundBoardRepository.findByIdWithMemberAndWaterPlace(lostFoundBoardId)).thenReturn(Optional.of(lostFoundBoard));

        LostFoundBoardDetailRespDto lostFoundBoardDetail = pageService.getLostFoundBoardDetail(lostFoundBoardId, member);

        // then
        assertEquals(lostFoundBoardDetail.getIsResolved(), true);
    }
}