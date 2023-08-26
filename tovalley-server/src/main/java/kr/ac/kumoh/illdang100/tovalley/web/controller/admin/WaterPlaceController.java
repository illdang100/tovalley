package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import kr.ac.kumoh.illdang100.tovalley.service.page.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class WaterPlaceController {

    private final PageService pageService;

    /**
     * @param model
     * @return 관리자용 물놀이 장소 리스트 페이지
     */
    @GetMapping("/review-list")
    public String adminWaterPlaceList(Model model) {

        return "admin/water_place/waterPlaceList";
    }

    /**
     * @param waterPlaceId 조회하고자 하는 물놀이 장소 pk
     * @param model
     * @return 관리자용 물놀이 장소 상세조회 페이지
     */
    @GetMapping("/review/{id}")
    public String adminWaterPlaceDetail(@PathVariable("id") Long waterPlaceId,
                                  Model model) {

        return "admin/water_place/waterPlaceDetail";
    }

    /**
     * @param waterPlaceId
     * @param model
     * @return 물놀이 장소 수정 페이지
     */
    @GetMapping("/review/{id}/edit")
    public String updateWaterPlaceForm(@PathVariable("id") Long waterPlaceId,
                                        Model model) {

        return "admin/water_place/updateWaterPlaceForm";
    }

    /**
     * 물놀이 장소 정보 수정 후 상세보기 페이지로 리다이렉트
     * @param waterPlaceId
     * @param model
     * @return 관리자용 물놀이 상세보기 페이지
     */
    @PostMapping("/review/{id}/edit")
    public String updateWaterPlace(@PathVariable("id") Long waterPlaceId,
                                        Model model) {

        return "redirect:/admin/review/" + waterPlaceId;
    }

    /**
     * @param model
     * @return 물놀이 장소 등록 페이지
     */
    @GetMapping("/water-places/new")
    public String createWaterPlaceForm(Model model) {

        model.addAttribute("form", new CreateWaterPlaceForm());

        return "admin/water_place/createWaterPlaceForm";
    }

    /**
     * 물놀이 장소 등록 후 리스트 페이지로 리다이렉트
     * @param waterPlaceImage
     * @param form
     * @return 관리자용 물놀이 장소 리스트 페이지
     */
    @PostMapping(value = "/water-places/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createWaterPlace(@RequestPart(value = "image", required = false) MultipartFile waterPlaceImage,
                                   CreateWaterPlaceForm form) {

        return "redirect:/admin/review-list";
    }
}
