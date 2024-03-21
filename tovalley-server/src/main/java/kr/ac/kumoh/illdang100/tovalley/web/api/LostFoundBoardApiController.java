package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardRespDto.MyLostFoundBoardRespDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardImageService;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.ImageUtil.*;
import static kr.ac.kumoh.illdang100.tovalley.util.ListUtil.isEmptyList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LostFoundBoardApiController {
    private final LostFoundBoardService lostFoundBoardService;
    private final LostFoundBoardImageService lostFoundBoardImageService;
    private final S3Service s3Service;

    @PostMapping(value = "/auth/lostItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveLostFoundBoard(@ModelAttribute @Valid LostFoundBoardSaveReqDto lostFoundBoardSaveReqDto,
                                                BindingResult bindingResult,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        Long saveLostFoundBoardId = lostFoundBoardService.saveLostFoundBoard(lostFoundBoardSaveReqDto, principalDetails.getMember().getId());

        List<ImageFile> uploadImageFiles = uploadImageFile(s3Service, lostFoundBoardSaveReqDto.getPostImage());
        lostFoundBoardImageService.saveLostFoundImageFile(uploadImageFiles, saveLostFoundBoardId);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글이 정상적으로 등록되었습니다", saveLostFoundBoardId), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/auth/lostItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLostFoundBoard(@ModelAttribute @Valid LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto,
                                                  BindingResult bindingResult,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        Long memberId = principalDetails.getMember().getId();
        Long lostFoundBoardId = lostFoundBoardUpdateReqDto.getLostFoundBoardId();

        LostFoundBoard lostFoundBoard = lostFoundBoardService.updateLostFoundBoard(lostFoundBoardUpdateReqDto, memberId);

        lostFoundBoardImageService.validateImageCount(lostFoundBoardUpdateReqDto, lostFoundBoard);

        List<String> deleteImage = lostFoundBoardUpdateReqDto.getDeleteImage();
        if (!isEmptyList(deleteImage)) {
            lostFoundBoardImageService.deleteLostFoundImageFiles(deleteImage, lostFoundBoardId, memberId);
            s3Service.deleteFiles(deleteImage);
        }

        List<MultipartFile> postImage = lostFoundBoardUpdateReqDto.getPostImage();
        if (!isEmptyList(postImage)) {
            List<ImageFile> uploadImageFiles = uploadImageFile(s3Service, postImage);
            lostFoundBoardImageService.saveLostFoundImageFile(uploadImageFiles, lostFoundBoardId);
        }

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글이 정상적으로 수정되었습니다", null), HttpStatus.OK);
    }

    @PatchMapping(value = "/auth/lostItem/{lostFoundBoardId}")
    public ResponseEntity<?> updateResolvedStatus(@PathVariable(value = "lostFoundBoardId")Long lostFoundBoardId,
                                                  @RequestParam Boolean isResolved,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {

        lostFoundBoardService.updateResolvedStatus(lostFoundBoardId, isResolved, principalDetails.getMember().getId());

        String result = isResolved ? "해결 완료" : "해결 미완료";
        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글 상태[" + result + "]가 변경되었습니다", null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/auth/lostItem/{lostFoundBoardId}")
    public ResponseEntity<?> deleteLostFoundBoard(@PathVariable("lostFoundBoardId") long lostFoundBoardId,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {

        // 분실물 게시글 삭제
        List<String> deleteLostFoundBoardImageFileName = lostFoundBoardService.deleteLostFoundBoard(lostFoundBoardId, principalDetails.getMember().getId());

        // s3 파일 삭제
        s3Service.deleteFiles(deleteLostFoundBoardImageFileName);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글이 정상적으로 삭제되었습니다", null), HttpStatus.OK);
    }

    @GetMapping("/auth/my-board")
    public ResponseEntity<?> getMyLostFoundBoards(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                  @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Slice<MyLostFoundBoardRespDto> result = lostFoundBoardService.getMyLostFoundBoards(
                principalDetails.getMember().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "사용자의 게시글 목록 조회에 성공하였습니다", result), HttpStatus.OK);
    }
}
