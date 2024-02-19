package com.youcode.myaftas.service;

import com.youcode.myaftas.dto.FishDto;
import com.youcode.myaftas.dto.responseDTO.FishRespDto;
import com.youcode.myaftas.entities.Fish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FishService {
    FishRespDto create(FishDto fishDto);
    void delete(String name);
    FishRespDto getOne(String name);
    List<Fish> searchFishsByName(String name);
    List<FishRespDto> findAll();
    FishRespDto update(String name, FishDto fishDto);
    Page<FishRespDto> findWithPagination(Pageable pageable);
}
