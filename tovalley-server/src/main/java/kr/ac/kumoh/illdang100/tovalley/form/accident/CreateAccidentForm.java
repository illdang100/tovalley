package kr.ac.kumoh.illdang100.tovalley.form.accident;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class CreateAccidentForm {

    @NotNull
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate accidentDate;

    @NotNull
    private AccidentEnum accidentCondition;

    @NotNull
    @Min(1)
    private Integer peopleNum;
}
