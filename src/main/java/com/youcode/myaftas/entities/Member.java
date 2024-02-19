package com.youcode.myaftas.entities;

import com.youcode.myaftas.enums.IdentityDocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "name should be not Empty")
    private String name;

    @NotBlank(message = "family Name should be not Empty")
    private String familyName;

    @NotNull(message = "accession Date should not be null")
    @Temporal(TemporalType.DATE)
    private LocalDate accessionDate;

    @NotBlank(message = "nationality should not be null")
    private String nationality;

    @NotEmpty(message = "Identity Document Type should not be empty")
    @Enumerated(EnumType.STRING)
    private IdentityDocumentType identityDocumentType;

    @NotNull(message = "Identity number should not be null")
    private String identityNumber;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Ranking> rankingList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Hunting> huntingList;


}
