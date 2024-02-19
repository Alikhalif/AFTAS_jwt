package com.youcode.myaftas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fish {
    @Id
    @NotBlank(message = "Fish Name Should not be Empty")
    private String name;

    @NotNull(message = "Fish Average Weight Should not be Null")
    @Positive(message = "Fish average weight should be a positive value")
    private double averageWeight;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "level_id")
    private Level level;

    @OneToMany(mappedBy = "fish", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Hunting> huntingList;

}
