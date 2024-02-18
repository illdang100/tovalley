package kr.ac.kumoh.illdang100.tovalley.util;

import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoard;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;

public class AuthorizationUtil {
    public static Boolean isAuthorizedToAccessBoard(LostFoundBoard lostFoundBoard, Long memberId) {
        boolean result = false;
        Member member = lostFoundBoard.getMember();
        if (member != null) {
            result = lostFoundBoard.getMember().getId().equals(memberId);
        }
        return result;
    }
}
