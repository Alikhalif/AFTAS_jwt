package com.youcode.myaftas.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.youcode.myaftas.Utils.RankingId;
import com.youcode.myaftas.dto.CompetitionDto;
import com.youcode.myaftas.dto.MemberDto;
import lombok.Data;

import java.util.List;

@Data
public class RankingRespDto {
    private RankingId rankingId;
    private Integer rank;

    private Integer score;

    private MemberDto member;

    private CompetitionDto competition;
}
