package com.youcode.myaftas.Utils;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class RankingId implements Serializable {

    private String code;
    private Integer id;
}
