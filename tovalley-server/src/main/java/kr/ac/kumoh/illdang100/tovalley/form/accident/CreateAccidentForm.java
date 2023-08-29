package kr.ac.kumoh.illdang100.tovalley.form.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreateAccidentForm {

    @NotNull
    private LocalDate accidentDate;

    @NotNull
    private AccidentEnum accidentCondition;

    @NotNull
    @Min(1)
    private Integer peopleNum;
}
