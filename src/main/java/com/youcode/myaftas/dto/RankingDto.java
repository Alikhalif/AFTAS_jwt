package com.youcode.myaftas.dto;

import com.youcode.myaftas.Utils.RankingId;
import lombok.Data;

@Data
public class RankingDto {
    //private RankingId id;

    private Integer rank;

    private Integer score;

    private Integer member_id;

    private String competition_id;
}
