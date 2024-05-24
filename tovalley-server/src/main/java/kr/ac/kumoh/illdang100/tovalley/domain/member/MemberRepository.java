package kr.ac.kumoh.illdang100.tovalley.domain.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    List<Member> findByIdOrNickname(Long id, String nickname);
}
