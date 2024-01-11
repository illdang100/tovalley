package kr.ac.kumoh.illdang100.tovalley.domain.member;

import kr.ac.kumoh.illdang100.tovalley.dto.admin.AdminChangeRoleRespDto.SearchMembersRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberRepositoryCustom {

    Slice<SearchMembersRespDto> findSliceMembersByNickname(String nickname, Pageable pageable);
}
