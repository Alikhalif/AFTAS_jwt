package com.youcode.myaftas.dto.responseDTO;

import com.youcode.myaftas.dto.HuntingDto;
import com.youcode.myaftas.dto.RankingDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompetitionRespDto {
    private String code;

    private LocalDate date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer numberOfParticipants;

    private String location;

    private Double amount;

    private List<RankingDto> rankingList;

    private List<HuntingDto> huntingList;
}
