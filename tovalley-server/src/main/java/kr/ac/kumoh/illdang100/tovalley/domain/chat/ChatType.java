package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatType {
    TEXT("텍스트"), IMAGE("이미지");

    private final String value;
}
