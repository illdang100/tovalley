package kr.ac.kumoh.illdang100.tovalley.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class ChatUtil {

    public static final String MEMBER_ID = "memberId";

    public static LocalDateTime convertZdtStringToLocalDateTime(String zdtString) {

        ZonedDateTime zdt = ZonedDateTime.parse(zdtString);
        return zdt.toLocalDateTime();
    }
}
