package kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LostFoundEnum {
    LOST("찾아요"), FOUND("찾았어요");
    private String value;
}
