package com.youcode.myaftas.service;

import com.youcode.myaftas.dto.CompetitionDto;
import com.youcode.myaftas.dto.responseDTO.CompetitionRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompititionService {
    CompetitionRespDto create(CompetitionDto competitionDto);
    void delete(String id);
    CompetitionRespDto getOne(String id);
    List<CompetitionRespDto> getAll();
    CompetitionRespDto update(String code, CompetitionDto competitionDto);
    Page<CompetitionRespDto> findWithPagination(Pageable pageable);
}
