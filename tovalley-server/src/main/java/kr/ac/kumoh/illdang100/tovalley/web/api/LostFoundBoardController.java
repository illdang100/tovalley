package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.security.auth.PrincipalDetails;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardImageService;
import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardService;
import kr.ac.kumoh.illdang100.tovalley.util.ListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;

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

        List<MultipartFile> postImage = lostFoundBoardSaveReqDto.getPostImage();
        List<ImageFile> uploadImageFiles = new ArrayList<>();
        if (ListUtil.isEmptyList(postImage)) {
            uploadImageFiles = s3Service.upload(postImage, FileRootPathVO.LOST_FOUND_BOARD_PATH);
        }

        lostFoundBoardImageService.saveLostFoundImageFile(uploadImageFiles, saveLostFoundBoardId);

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글이 정상적으로 등록되었습니다", saveLostFoundBoardId), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/auth/lostItem")
    public ResponseEntity<?> updateLostFoundBoard(@RequestBody @Valid LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto,
                                                  BindingResult bindingResult,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {
        lostFoundBoardService.updateLostFoundBoard(lostFoundBoardUpdateReqDto, principalDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "분실물 게시글이 정상적으로 수정되었습니다", null), HttpStatus.OK);
    }
}
