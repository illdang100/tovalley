package kr.ac.kumoh.illdang100.tovalley.service.lost_found_board;

import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostFoundBoardServiceImpl implements LostFoundBoardService{

    private final LostFoundBoardRepository lostFoundBoardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LostFoundBoardImageRepository lostFoundBoardImageRepository;
}
