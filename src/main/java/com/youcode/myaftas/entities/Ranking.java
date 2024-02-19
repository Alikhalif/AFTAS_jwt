package com.youcode.myaftas.entities;

import com.youcode.myaftas.Utils.RankingId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ranking {
    @EmbeddedId
    private RankingId id;

    @NotNull(message = "Rank should not be null")
    @Min(value = 1, message = "Rank should be at least 1")
    private Integer rank;

    @NotNull(message = "Score should not be null")
    @PositiveOrZero(message = "Score should not be negative")
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "member id should not be null")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "competition id should not be null")
    private Competition competition;

}
