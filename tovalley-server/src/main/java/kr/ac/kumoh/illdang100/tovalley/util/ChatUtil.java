package kr.ac.kumoh.illdang100.tovalley.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ChatUtil {

    public static final String MEMBER_ID = "memberId";
    public static final String SUBSCRIPTIONS = "subscriptions";
    public static final int MAX_PARTICIPANTS_PER_CHATROOM = 2;

    public static LocalDateTime convertZdtStringToLocalDateTime(String zdtString) {

        // ZonedDateTime zdt = ZonedDateTime.parse(zdtString);
        // ZonedDateTime kstZdt = zdt.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        // return kstZdt.toLocalDateTime();

        ZonedDateTime zdt = ZonedDateTime.parse(zdtString);
        return zdt.toLocalDateTime();
    }
}
