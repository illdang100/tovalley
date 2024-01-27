package kr.ac.kumoh.illdang100.tovalley.web.api;

import kr.ac.kumoh.illdang100.tovalley.service.lost_found_board.LostFoundBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LostFoundBoardController {
    private final LostFoundBoardService lostFoundBoardService;
}
