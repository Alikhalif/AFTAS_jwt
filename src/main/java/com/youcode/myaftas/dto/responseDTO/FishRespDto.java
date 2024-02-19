package com.youcode.myaftas.dto.responseDTO;

import com.youcode.myaftas.dto.HuntingDto;
import com.youcode.myaftas.dto.LevelDto;
import lombok.Data;

import java.util.List;

@Data
public class FishRespDto {
    private String name;

    private Double averageWeight;

    private LevelDto level;

    private List<HuntingDto> huntingList;
}
