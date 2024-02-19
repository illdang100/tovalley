package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardImageService;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.ImageUtil.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LostFoundBoardController {
    private final LostFoundBoardService lostFoundBoardService;
    private final LostFoundBoardImageService lostFoundBoardImageService;
    private final S3Service s3Service;

    @PostMapping(value = "/auth/lostItem")
    public ResponseEntity<?> saveLostFoundBoard(@RequestBody @Valid LostFoundBoardSaveReqDto lostFoundBoardSaveReqDto,
                                                BindingResult bindingResult,
                                                @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        Long saveLostFoundBoardId = lostFoundBoardService.saveLostFoundBoard(lostFoundBoardSaveReqDto, principalDetails.getMember().getId());

        List<ImageFile> uploadImageFiles = uploadImageFile(s3Service, lostFoundBoardSaveReqDto.getPostImage());
        lostFoundBoardImageService.saveLostFoundImageFile(uploadImageFiles, saveLostFoundBoardId);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글이 정상적으로 등록되었습니다", saveLostFoundBoardId), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/auth/lostItem")
    public ResponseEntity<?> updateLostFoundBoard(@RequestBody @Valid LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto,
                                                  BindingResult bindingResult,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        Long memberId = principalDetails.getMember().getId();
        List<String> deleteImage = lostFoundBoardUpdateReqDto.getDeleteImage();
        Long lostFoundBoardId = lostFoundBoardUpdateReqDto.getLostFoundBoardId();

        LostFoundBoard lostFoundBoard = lostFoundBoardService.updateLostFoundBoard(lostFoundBoardUpdateReqDto, memberId);

        lostFoundBoardImageService.validateImageCount(lostFoundBoardUpdateReqDto, lostFoundBoard);

        // 사진 파일 삭제
        lostFoundBoardImageService.deleteLostFoundImageFiles(deleteImage, lostFoundBoardId, memberId);
        s3Service.deleteFiles(deleteImage);

        // 새로운 사진 파일 등록
        List<ImageFile> uploadImageFiles = uploadImageFile(s3Service, lostFoundBoardUpdateReqDto.getPostImage());
        lostFoundBoardImageService.saveLostFoundImageFile(uploadImageFiles, lostFoundBoardId);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글이 정상적으로 수정되었습니다", null), HttpStatus.OK);
    }

    @PatchMapping(value = "/auth/lostItem/{lostFoundBoardId}")
    public ResponseEntity<?> updateResolvedStatus(@PathVariable(value = "lostFoundBoardId")Long lostFoundBoardId,
                                                  @RequestParam Boolean isResolved) {

        lostFoundBoardService.updateResolvedStatus(lostFoundBoardId, isResolved);

        String result = isResolved ? "해결 완료" : "해결 미완료";
        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글 상태[" + result + "]가 변경되었습니다", null), HttpStatus.OK);
    }
}
