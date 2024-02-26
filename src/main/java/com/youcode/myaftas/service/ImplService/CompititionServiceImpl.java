package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Exception.ResourceNotFoundException;
import com.youcode.myaftas.dto.CompetitionDto;
import com.youcode.myaftas.dto.responseDTO.CompetitionRespDto;
import com.youcode.myaftas.entities.Competition;
import com.youcode.myaftas.repositories.CompetitionRepository;
import com.youcode.myaftas.service.CompititionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CompititionServiceImpl implements CompititionService {
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public CompetitionRespDto create(CompetitionDto competitionDto){
        Competition competition = modelMapper.map(competitionDto, Competition.class);

        competition = competitionRepository.save(competition);
        return modelMapper.map(competition, CompetitionRespDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public void delete(String code){
        Competition competition = competitionRepository.findById(code)
                .orElseThrow(()-> new ResourceNotFoundException("Compitition not found with code : "+code));
        competitionRepository.delete(competition);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public CompetitionRespDto getOne(String code){
        Optional<Competition> competition = competitionRepository.findByCode(code);
        if (competition == null) {
            throw new ResourceNotFoundException("Competition not found with code " + code);
        }
        return modelMapper.map(competition, CompetitionRespDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<CompetitionRespDto> getAll(){
        return Arrays.asList(modelMapper.map(competitionRepository.findAll(), CompetitionRespDto[].class));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")

    @Override
    public CompetitionRespDto update(String code, CompetitionDto competitionDto){
        Competition competition = competitionRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Compitition not found with code : "+code));


        if(competitionDto.getDate() != null){
            competition.setDate(competitionDto.getDate());
        }
        if (competitionDto.getStartTime() != null){
            competition.setStartTime(competitionDto.getStartTime());
        }
        if(competitionDto.getEndTime() != null){
            competition.setEndTime(competitionDto.getEndTime());
        }
        if(competitionDto.getNumberOfParticipants() != null){
            competition.setNumberOfParticipants(competitionDto.getNumberOfParticipants());
        }
        if(competitionDto.getLocation().isEmpty() == false){
            competition.setLocation(competitionDto.getLocation());
        }
        if(competitionDto.getAmount() != null){
            competition.setAmount(competitionDto.getAmount());
        }

        competition = competitionRepository.save(competition);
        return modelMapper.map(competition, CompetitionRespDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public Page<CompetitionRespDto> findWithPagination(Pageable pageable) {
        Page<Competition> competitionsPage = competitionRepository.findAll(pageable);
        return competitionsPage.map(competition -> modelMapper.map(competition, CompetitionRespDto.class));
    }

}
