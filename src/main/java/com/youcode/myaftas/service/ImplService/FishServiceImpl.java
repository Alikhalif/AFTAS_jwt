package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Exception.ResourceNotFoundException;
import com.youcode.myaftas.dto.FishDto;
import com.youcode.myaftas.dto.responseDTO.FishRespDto;
import com.youcode.myaftas.dto.responseDTO.LevelRespDto;
import com.youcode.myaftas.entities.*;
import com.youcode.myaftas.repositories.FishRepository;
import com.youcode.myaftas.repositories.LevelRepository;
import com.youcode.myaftas.service.FishService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FishServiceImpl implements FishService {
    @Autowired
    private FishRepository fishRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FishRespDto create(FishDto fishDto){
        Fish fish = modelMapper.map(fishDto, Fish.class);

        Level level = levelRepository.findById(fishDto.getLevel_id())
                .orElseThrow(() -> new ResourceNotFoundException("not found level with id : "+fishDto.getLevel_id()));
        fish.setLevel(level);

        fish = fishRepository.save(fish);
        return modelMapper.map(fish, FishRespDto.class);
    }

    @Override
    public void delete(String name){
        Fish fish = fishRepository.findById(name)
                .orElseThrow(()-> new ResourceNotFoundException("Fish not found with name "+name));
        fishRepository.delete(fish);
    }

    @Override
    public FishRespDto getOne(String name){
        Optional<Fish> fish = fishRepository.findByName(name);
        if (fish == null) {
            throw new ResourceNotFoundException("Fish not found with name " + name);
        }
        return modelMapper.map(fish, FishRespDto.class);
    }

    @Override
    public List<Fish> searchFishsByName(String name) {
        return fishRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<FishRespDto> findAll() {
        return fishRepository.findAll().stream().map(fish -> modelMapper.map(fish, FishRespDto.class)).collect(Collectors.toList());
    }

    @Override
    public FishRespDto update(String name, FishDto fishDto) {
        Fish fish = fishRepository.findById(name)
                .orElseThrow(() -> new ResourceNotFoundException("Fish Not found with this: " + name));
        return modelMapper.map(fishRepository.save(fish), FishRespDto.class);
    }


    @Override
    public Page<FishRespDto> findWithPagination(Pageable pageable) {
        Page<Fish> fishPage = fishRepository.findAll(pageable);
        return fishPage.map(fish -> modelMapper.map(fish, FishRespDto.class));
    }

}
