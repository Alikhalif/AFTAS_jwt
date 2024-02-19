package com.youcode.myaftas.repositories;

import com.youcode.myaftas.Utils.RankingId;
import com.youcode.myaftas.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, RankingId> {


    @Query("SELECT COUNT(r) FROM Ranking r WHERE r.competition.code = :competitionCode")
    int countByCompetitionId(@Param("competitionCode") String competitionCode);


    List<Ranking> findByCompetitionCode(String competitionCode);

    List<Ranking> findTop3ByCompetitionCodeOrderByRankAsc(String competitionId);


}
