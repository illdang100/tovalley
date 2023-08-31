package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto;
import kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto;
import kr.ac.kumoh.illdang100.tovalley.form.accident.CreateAccidentForm;
import kr.ac.kumoh.illdang100.tovalley.service.accident.AccidentService;
import kr.ac.kumoh.illdang100.tovalley.service.water_place.WaterPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentReqDto.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AccidentController {

    private final AccidentService accidentService;
    private final WaterPlaceService waterPlaceService;

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

        return "redirect:/admin/water-places/" + waterPlaceId;
    }

    @PostMapping("/water-places/{waterPlaceId}/accidents/{accidentId}/delete")
    public String deleteAccident(@PathVariable("waterPlaceId") Long waterPlaceId,
                                 @PathVariable("accidentId") Long accidentId) {

        accidentService.deleteAccident(waterPlaceId, accidentId);

        return "redirect:/admin/water-places/" + waterPlaceId;
    }
}
