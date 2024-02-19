package com.youcode.myaftas.dto.responseDTO;

import com.youcode.myaftas.dto.FishDto;
import lombok.Data;

import java.util.List;

@Data
public class LevelRespDto {
    private Integer id;

    private String description;

    private Integer point;

    private List<FishDto> fishList;
}
