package com.youcode.myaftas.service;

import com.youcode.myaftas.dto.RankingDto;
import com.youcode.myaftas.dto.responseDTO.RankingRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RankingService {
    RankingDto create(RankingDto rankingDto);
    void calculate(String compitition_name);
    List<RankingDto> sortByScore(List<RankingDto> rankings);
    List<RankingRespDto> getTop3Rank(String compitition_name);
    void delete(String code, Integer id);
    RankingDto getOne(String code, Integer id);
    List<RankingDto> findAll();
    RankingDto update(String code, Integer id, RankingDto rankingDto);
    Page<RankingDto> findWithPagination(Pageable pageable);
}
