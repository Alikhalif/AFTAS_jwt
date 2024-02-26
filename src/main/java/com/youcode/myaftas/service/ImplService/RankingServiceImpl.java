package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Exception.ResourceNotFoundException;
import com.youcode.myaftas.Utils.RankingId;
import com.youcode.myaftas.dto.RankingDto;
import com.youcode.myaftas.dto.responseDTO.RankingRespDto;
import com.youcode.myaftas.entities.Competition;
import com.youcode.myaftas.entities.Hunting;
import com.youcode.myaftas.entities.Member;
import com.youcode.myaftas.entities.Ranking;
import com.youcode.myaftas.repositories.CompetitionRepository;
import com.youcode.myaftas.repositories.HuntingRepository;
import com.youcode.myaftas.repositories.MemberRepository;
import com.youcode.myaftas.repositories.RankingRepository;
import com.youcode.myaftas.service.CompititionService;
import com.youcode.myaftas.service.HuntingService;
import com.youcode.myaftas.service.RankingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RankingServiceImpl implements RankingService {
    @Autowired
    private RankingRepository rankingRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private HuntingRepository huntingRepository;
    @Autowired
    private CompititionService compititionService;
    @Autowired
    private HuntingService huntingService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public RankingDto create(RankingDto rankingDto){
        if (compititionService.getOne(rankingDto.getCompetition_id()).getNumberOfParticipants() > rankingRepository.countByCompetitionId(rankingDto.getCompetition_id())) {

            Ranking ranking = modelMapper.map(rankingDto, Ranking.class);

            Competition competition = competitionRepository.findById(rankingDto.getCompetition_id())
                    .orElseThrow(() -> new ResourceNotFoundException("not found compitition with id : " + rankingDto.getCompetition_id()));
            ranking.setCompetition(competition);


            Member member = memberRepository.findById(rankingDto.getMember_id())
                    .orElseThrow(() -> new ResourceNotFoundException("not found member with id : " + rankingDto.getMember_id()));
            ranking.setMember(member);


            RankingId rankingId = new RankingId(
                    rankingDto.getCompetition_id(),
                    rankingDto.getMember_id()
            );

            if (rankingRepository.existsById(rankingId)) {
                throw new ResourceNotFoundException("Ranking already to the member");
            } else {
                ranking.setId(rankingId);
            }


            ranking = rankingRepository.save(ranking);
            return modelMapper.map(ranking, RankingDto.class);
        }else{
            throw new ResourceNotFoundException("Oops max this compitition is : "+compititionService.getOne(rankingDto.getCompetition_id()).getNumberOfParticipants());
        }
    }


    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public void calculate(String compitition_name){
        List<Hunting> huntingListList = huntingRepository.findAllByCompetitionCode(compitition_name);
        List<RankingDto> rankings = new ArrayList<>();

        if (!huntingListList.isEmpty()){
            huntingListList.forEach(
                    hunting -> {
                        RankingDto ranking = new RankingDto();
                        ranking.setCompetition_id(
                                hunting.getCompetition().getCode()
                        );
                        ranking.setMember_id(
                                hunting.getMember().getId()
                        );
                        //ranking.setId(rankingId);
                        ranking.setScore(
                                huntingRepository.findHuntingByCompetitionCodeAndMemberId(compitition_name, hunting.getMember().getId())
                                        .stream()
                                        .mapToInt(hunting2 ->hunting2.getNomberOfFish() * hunting2.getFish().getLevel().getPoint())
                                        .sum()
                        );

                        //System.out.println(ranking);
                        rankings.add(ranking);
                    }
            );

            List<RankingDto> rankings2 = sortByScore(rankings);

            AtomicInteger i = new AtomicInteger(1);
            rankings2.forEach(
                    rankingDto -> {
                        //System.out.println(rankingDto);
                        RankingId rankingId = new RankingId(
                                rankingDto.getCompetition_id(),
                                rankingDto.getMember_id()
                        );

                        if(rankingRepository.findById(rankingId).isEmpty()){
                            rankingDto.setRank(i.getAndIncrement());
                            System.out.println(rankingDto);

                            create(rankingDto);
                        }
                    }
            );
        }else {
            throw new ResourceNotFoundException("hunting not found");
        }

    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<RankingDto> sortByScore(List<RankingDto> rankings) {
        return rankings.stream()
                .sorted((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()))
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<RankingRespDto> getTop3Rank(String compitition_name) {
        List<Ranking> top3Rankings = rankingRepository.findTop3ByCompetitionCodeOrderByRankAsc(compitition_name);
        return top3Rankings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    private RankingRespDto convertToDto(Ranking ranking) {
        RankingRespDto rankingDto = modelMapper.map(ranking, RankingRespDto.class);
        return rankingDto;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public void delete(String code, Integer id){
        RankingId rankingId = new RankingId(
                code,
                id
        );

        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(()-> new ResourceNotFoundException("Ranking not found with id "+id));
        rankingRepository.delete(ranking);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public RankingDto getOne(String code, Integer id){
        RankingId rankingId = new RankingId(
                code,
                id
        );
        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ranking not found with id "+id));
        return modelMapper.map(ranking, RankingDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<RankingDto> findAll() {
        return rankingRepository.findAll().stream().map(ranking -> modelMapper.map(ranking, RankingDto.class)).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public RankingDto update(String code, Integer id, RankingDto rankingDto) {
        RankingId rankingId = new RankingId(
                code,
                id
        );
        Ranking existingRanking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ranking Not found with this: " + id));
        return modelMapper.map(rankingRepository.save(existingRanking), RankingDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public Page<RankingDto> findWithPagination(Pageable pageable) {
        Page<Ranking> rankingPage = rankingRepository.findAll(pageable);
        return rankingPage.map(ranking -> modelMapper.map(ranking, RankingDto.class));
    }

}
