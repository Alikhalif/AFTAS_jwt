package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Exception.ResourceNotFoundException;
import com.youcode.myaftas.dto.LevelDto;
import com.youcode.myaftas.dto.responseDTO.LevelRespDto;
import com.youcode.myaftas.entities.Level;
import com.youcode.myaftas.repositories.LevelRepository;
import com.youcode.myaftas.service.LevelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LevelServiceImpl implements LevelService {
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")

    @Override
    public LevelRespDto create(LevelDto levelDto){
        if(levelRepository.findMaxPoints() < levelDto.getPoint()){
            Level level = modelMapper.map(levelDto, Level.class);
            level = levelRepository.save(level);
            return modelMapper.map(level, LevelRespDto.class);
        }else
        {
            throw new ResourceNotFoundException("The checkpoint when entering imaging levels must be larger than before. ");
        }


    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public void delete(Integer id){
        Level level = levelRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Level not found with id "+id));
        levelRepository.delete(level);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public LevelRespDto getOne(Integer id){
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found with id "+id));
        return modelMapper.map(level, LevelRespDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public List<LevelRespDto> findAll() {
        return levelRepository.findAll().stream().map(level -> modelMapper.map(level, LevelRespDto.class)).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_JURY','ROLE_ADMIN')")
    @Override
    public LevelRespDto update(Integer id, LevelDto levelDto) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Level Not found with this: " + id));
        return modelMapper.map(levelRepository.save(level), LevelRespDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADHERENT','ROLE_JURY','ROLE_ADMIN')")
    @Override
    public Page<LevelRespDto> findWithPagination(Pageable pageable) {
        Page<Level> levelPage = levelRepository.findAll(pageable);
        return levelPage.map(level -> modelMapper.map(level, LevelRespDto.class));
    }
}
