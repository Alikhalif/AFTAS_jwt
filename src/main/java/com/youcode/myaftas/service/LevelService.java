package com.youcode.myaftas.service;

import com.youcode.myaftas.dto.LevelDto;
import com.youcode.myaftas.dto.responseDTO.LevelRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LevelService {
    LevelRespDto create(LevelDto levelDto);
    void delete(Integer id);
    LevelRespDto getOne(Integer id);
    List<LevelRespDto> findAll();
    LevelRespDto update(Integer id, LevelDto levelDto);
    Page<LevelRespDto> findWithPagination(Pageable pageable);
}
