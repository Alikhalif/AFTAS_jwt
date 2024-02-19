package com.youcode.myaftas.repositories;

import com.youcode.myaftas.entities.Hunting;
import com.youcode.myaftas.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HuntingRepository extends JpaRepository<Hunting, Integer> {

    //List<Hunting> findByCompetitionCode(String competitionCode);

    @Query("SELECT h FROM Hunting h " +
            "WHERE h.competition.code = :competitionId " +
            "AND h.member.id = :memberId " +
            "AND h.fish.name = :fishId")
    Hunting findByCompetitionIdAndMemberIdAndFishId(
            @Param("competitionId") String competitionId,
            @Param("memberId") Integer memberId,
            @Param("fishId") String fishId
    );


    List<Hunting> findAllByCompetitionCode(String compitition_name);
    List<Hunting> findHuntingByCompetitionCodeAndMemberId(String code, int id);


}
