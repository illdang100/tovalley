package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto;

import java.util.List;

public interface LostFoundBoardImageService {
    void saveLostFoundImageFile(List<ImageFile> uploadImageFiles, Long lostFoundBoardId);

    void deleteLostFoundImageFiles(List<String> imageFileUrls, Long lostFoundBoardId, Long memberId);

    void validateImageCount(LostFoundBoardReqDto.LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto, LostFoundBoard findLostFoundBoard);

    List<String> deleteLostFoundBoardImageInBatch(Long lostFoundBoardId);
}
