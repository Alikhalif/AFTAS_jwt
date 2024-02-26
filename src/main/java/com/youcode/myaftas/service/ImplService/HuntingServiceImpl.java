package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Exception.ResourceNotFoundException;
import com.youcode.myaftas.dto.FishDto;
import com.youcode.myaftas.dto.HuntingDto;
import com.youcode.myaftas.dto.LevelDto;
import com.youcode.myaftas.dto.RankingDto;
import com.youcode.myaftas.dto.responseDTO.FishRespDto;
import com.youcode.myaftas.dto.responseDTO.LevelRespDto;
import com.youcode.myaftas.entities.*;
import com.youcode.myaftas.repositories.*;
import com.youcode.myaftas.service.HuntingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HuntingServiceImpl implements HuntingService {
    @Autowired
    private HuntingRepository huntingRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FishRepository fishRepository;
    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<HuntingDto> create(List<HuntingDto> huntingDtos){
        return huntingDtos.stream().map(huntingDto -> {

            Competition competition1 = competitionRepository.getOne(huntingDto.getCompetition_id());

            if(competition1.getDate().equals(LocalDate.now())) {

                Hunting huntingExist = huntingRepository.findByCompetitionIdAndMemberIdAndFishId(
                        huntingDto.getCompetition_id(),
                        huntingDto.getMember_id(),
                        huntingDto.getFish_id()
                );

                if (huntingExist != null) {
                    Integer newNbrFish = huntingExist.getNomberOfFish() + huntingDto.getNomberOfFish();
                    huntingExist.setNomberOfFish(newNbrFish);
                    huntingExist = huntingRepository.save(huntingExist);
                    return modelMapper.map(huntingExist, HuntingDto.class);
                    //throw new ResourceNotFoundException("Hunting already exists, just increases the number of fish hunted.");
                } else {
                    Hunting hunting = modelMapper.map(huntingDto, Hunting.class);

                    Competition competition = competitionRepository.findById(huntingDto.getCompetition_id())
                            .orElseThrow(() -> new ResourceNotFoundException("not found compitition with id : " + huntingDto.getCompetition_id()));
                    hunting.setCompetition(competition);

                    Member member = memberRepository.findById(huntingDto.getMember_id())
                            .orElseThrow(() -> new ResourceNotFoundException("not found member with id : " + huntingDto.getMember_id()));
                    hunting.setMember(member);

                    Fish fish = fishRepository.findById(huntingDto.getFish_id())
                            .orElseThrow(() -> new ResourceNotFoundException("not found fich with id : " + huntingDto.getFish_id()));
                    hunting.setFish(fish);




                    return huntingRepository.save(hunting);
                }
            }else {
                throw new ResourceNotFoundException("The results of the hunts are recorded during the competition on the same day.");

            }
        }).map(hunting -> modelMapper.map(hunting, HuntingDto.class))
                .collect(Collectors.toList());


    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public void delete(Integer id){
        Hunting hunting = huntingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hunting not found with id "+id));
        huntingRepository.delete(hunting);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public HuntingDto getOne(Integer id){
        Hunting hunting = huntingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hunting not found with id "+id));
        return modelMapper.map(hunting, HuntingDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<HuntingDto> findAll() {
        return huntingRepository.findAll().stream().map(hunting -> modelMapper.map(hunting, HuntingDto.class)).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public HuntingDto update(Integer id, HuntingDto huntingDto) {
        Hunting hunting = huntingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hunting Not found with this: " + id));
        return modelMapper.map(huntingRepository.save(hunting), HuntingDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public Page<HuntingDto> findWithPagination(Pageable pageable) {
        Page<Hunting> huntingPage = huntingRepository.findAll(pageable);
        return huntingPage.map(hunting -> modelMapper.map(hunting, HuntingDto.class));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<HuntingDto> findHuntingByCompititionAndMember(String code, Integer id){
        return Arrays.asList(modelMapper.map(huntingRepository.findHuntingByCompetitionCodeAndMemberId(code,id), HuntingDto[].class));

    }

}
