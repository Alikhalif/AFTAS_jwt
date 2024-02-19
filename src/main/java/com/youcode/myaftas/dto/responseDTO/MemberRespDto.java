package com.youcode.myaftas.dto.responseDTO;

import com.youcode.myaftas.dto.HuntingDto;
import com.youcode.myaftas.dto.RankingDto;
import com.youcode.myaftas.enums.IdentityDocumentType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MemberRespDto {
    private Integer id;

    private String name;

    private String familyName;

    private LocalDate accessionDate;

    private String nationality;

    private IdentityDocumentType identityDocument;

    private String identityNumber;

    private List<RankingDto> rankingList;

    private List<HuntingDto> huntingList;
}
