package com.youcode.myaftas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hunting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "nomber Of Fish Should not be Empty")
    private Integer nomberOfFish;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "competition id should not be null")
    private Competition competition;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "member id should not be null")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "fish_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "fish id should not be null")
    private Fish fish;

}
