package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardImage;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;
import static kr.ac.kumoh.illdang100.tovalley.util.ImageUtil.MAX_IMAGE_COUNT;
import static kr.ac.kumoh.illdang100.tovalley.util.ListUtil.isEmptyList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundBoardImageServiceImpl implements LostFoundBoardImageService {
    private final LostFoundBoardImageRepository lostFoundBoardImageRepository;
    private final LostFoundBoardRepository lostFoundBoardRepository;

    @Override
    @Transactional
    public void saveLostFoundImageFile(List<ImageFile> uploadImageFiles, Long lostFoundBoardId) {
        uploadImageFiles.stream()
                .map(imageFile -> LostFoundBoardImage.builder().lostFoundBoardId(lostFoundBoardId).imageFile(imageFile).build())
                .forEach(lostFoundBoardImageRepository::save);
    }

    @Override
    @Transactional
    public void deleteLostFoundImageFiles(List<String> imageFileUrls, Long lostFoundBoardId, Long memberId) {

        if (!isAuthorizedToAccessLostFoundBoardImage(lostFoundBoardId, memberId)) {
            throw new CustomApiException("게시글 작성자에게만 권한이 있습니다");
        }

        Map<String, LostFoundBoardImage> imageFileMap = lostFoundBoardImageRepository.findByLostFoundBoardId(lostFoundBoardId)
                .stream()
                .collect(Collectors.toMap(
                        foundImage -> foundImage.getImageFile().getStoreFileUrl(),
                        Function.identity()
                ));

        for (String imageUrl : imageFileUrls) {
            LostFoundBoardImage foundImage = imageFileMap.get(imageUrl);
            if (foundImage != null) {
                lostFoundBoardImageRepository.delete(foundImage);
            }
        }
    }

    public Boolean isAuthorizedToAccessLostFoundBoardImage(Long lostFoundBoardId, Long memberId) {
        Member member = findLostFoundBoardByIdWithMemberOrElseThrowEx(lostFoundBoardRepository, lostFoundBoardId).getMember();
        return member != null && member.getId().equals(memberId);
    }

    @Override
    public void validateImageCount(LostFoundBoardUpdateReqDto lostFoundBoardUpdateReqDto, LostFoundBoard findLostFoundBoard) {
        int currentImageSize = lostFoundBoardImageRepository.findImageByLostFoundBoardId(findLostFoundBoard.getId()).size();
        int newImageSize = !isEmptyList(lostFoundBoardUpdateReqDto.getPostImage()) ? lostFoundBoardUpdateReqDto.getPostImage().size() : 0;
        int deleteImageSize = !isEmptyList(lostFoundBoardUpdateReqDto.getDeleteImage()) ? lostFoundBoardUpdateReqDto.getDeleteImage().size() : 0;

        if (currentImageSize - deleteImageSize + newImageSize > MAX_IMAGE_COUNT) {
            throw new CustomApiException("게시글에 저장할 수 있는 최대 이미지 개수를 초과했습니다");
        }
    }

    @Override
    public List<String> deleteLostFoundBoardImageInBatch(Long lostFoundBoardId, Long memberId) {
        List<LostFoundBoardImage> findLostFoundBoardImageList = lostFoundBoardImageRepository.findByLostFoundBoardId(lostFoundBoardId);

        if (!isEmptyList(findLostFoundBoardImageList)) {
            lostFoundBoardImageRepository.deleteAllByIdInBatch(findLostFoundBoardImageList.stream()
                    .map(LostFoundBoardImage::getId)
                    .collect(Collectors.toList()));
        }

        return findLostFoundBoardImageList.stream()
                .map(image -> image.getImageFile().getStoreFileName())
                .collect(Collectors.toList());
    }
}
