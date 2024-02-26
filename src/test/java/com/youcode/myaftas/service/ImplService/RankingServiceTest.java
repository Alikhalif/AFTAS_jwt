package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Utils.RankingId;
import com.youcode.myaftas.entities.*;
import com.youcode.myaftas.repositories.HuntingRepository;
import com.youcode.myaftas.repositories.RankingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RankingServiceTest {
    @Mock
    private HuntingRepository huntingRepository;

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RankingServiceImpl underTest;


    @Test
    void getOne() {
        RankingId rankingId = RankingId.builder()
                .code("COMP1")
                .id(1)
                .build();

        Ranking ranking = Ranking.builder()
                .id(rankingId)
                .rank(1)
                .score(100)
                .member(new Member())
                .competition(new Competition())
                .build();

        RankingRepository rankingRepository = mock(RankingRepository.class);
        when(rankingRepository.findById(rankingId)).thenReturn(Optional.of(ranking));
        Ranking result = rankingRepository.getOne(rankingId);
        assertNotNull(result);
    }

    @Test
    public void testCalculate() {
        String competitionName = "CompetitionName";
        List<Hunting> huntingList = createDummyHuntingList(5);

        when(huntingRepository.findAllByCompetitionCode(eq(competitionName))).thenReturn(huntingList);

        when(huntingRepository.findHuntingByCompetitionCodeAndMemberId(eq(competitionName), anyInt())).thenReturn(huntingList);

        when(rankingRepository.findById(any())).thenReturn(Optional.empty());

        when(modelMapper.map(any(), eq(Ranking.class))).thenReturn(new Ranking());
        when(rankingRepository.save(any())).thenReturn(new Ranking());

        underTest.calculate(competitionName);


    }

    public static Hunting createDummyHunting() {
        Competition competition = Competition.builder().code("sss").build();
        //Member member = Member.builder().id(1).name("Dummy Member").build();
        Fish fish = Fish.builder().name("ssdd").name("Dummy Fish").build();

        return Hunting.builder()
                .id(1)
                .nomberOfFish(5) // Set the number of fish as needed
                .competition(competition)
                //.member(member)
                .fish(fish)
                .build();
    }

    public static List<Hunting> createDummyHuntingList(int numberOfHuntings) {
        List<Hunting> huntingList = new ArrayList<>();
        for (int i = 0; i < numberOfHuntings; i++) {
            huntingList.add(createDummyHunting());
        }
        return huntingList;
    }


}



