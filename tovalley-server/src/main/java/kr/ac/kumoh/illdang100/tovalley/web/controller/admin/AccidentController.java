package kr.ac.kumoh.illdang100.tovalley.web.controller.admin;

import kr.ac.kumoh.illdang100.tovalley.form.accident.CreateAccidentForm;
import kr.ac.kumoh.illdang100.tovalley.service.accident.AccidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AccidentController {

    private final AccidentService accidentService;

    @PostMapping("/water-places/{id}/accidents/new")
    public String createAccident(@PathVariable("id") Long waterPlaceId,
            @ModelAttribute @Valid CreateAccidentForm form) {

        accidentService.saveNewAccident(waterPlaceId, form);

        return "redirect:/admin/water-places/" + waterPlaceId;
    }

    @DeleteMapping("/water-places/{waterPlaceId}/accidents/{accidentId}")
    public String deleteAccident(@PathVariable("waterPlaceId") Long waterPlaceId,
                                 @PathVariable("accidentId") Long accidentId) {

        accidentService.deleteAccident(waterPlaceId, accidentId);

        return "redirect:/admin/water-places/" + waterPlaceId;
    }
}
