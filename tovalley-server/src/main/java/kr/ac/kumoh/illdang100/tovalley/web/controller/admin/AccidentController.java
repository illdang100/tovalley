package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import kr.ac.kumoh.illdang100.tovalley.form.accident.CreateAccidentForm;
import kr.ac.kumoh.illdang100.tovalley.service.accident.AccidentService;
import kr.ac.kumoh.illdang100.tovalley.service.water_place.WaterPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/th/admin")
@Slf4j
public class AccidentController {

    private final AccidentService accidentService;

    @PostMapping("/water-places/{id}/accidents/new")
    public String createAccident(@PathVariable("id") Long waterPlaceId,
                                 @ModelAttribute("addAccidentCond") @Valid CreateAccidentForm form,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            log.debug("createAccident error 처리 동작!!");
            redirectAttributes.addFlashAttribute("errorMessage", "사고 등록 실패!!");
        } else {
            accidentService.saveNewAccident(waterPlaceId, form);
            redirectAttributes.addFlashAttribute("successMessage", "사고 등록 성공!!");
        }

        return "redirect:/th/admin/water-places/" + waterPlaceId;
    }

    @PostMapping("/water-places/{waterPlaceId}/accidents/{accidentId}/delete")
    public String deleteAccident(@PathVariable("waterPlaceId") Long waterPlaceId,
                                 @PathVariable("accidentId") Long accidentId) {

        accidentService.deleteAccident(waterPlaceId, accidentId);

        return "redirect:/th/admin/water-places/" + waterPlaceId;
    }
}
