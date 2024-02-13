package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;

import java.util.List;

public interface LostFoundBoardImageService {
    void saveLostFoundImageFile(List<ImageFile> uploadImageFiles, Long lostFoundBoardId);

    void deleteLostFoundImageFiles(List<String> imageFileUrls, Long lostFoundBoardId);
}
