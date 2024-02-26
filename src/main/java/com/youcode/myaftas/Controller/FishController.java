package com.youcode.myaftas.Controller;

import com.youcode.myaftas.dto.CompetitionDto;
import com.youcode.myaftas.dto.FishDto;
import com.youcode.myaftas.dto.LevelDto;
import com.youcode.myaftas.dto.responseDTO.FishRespDto;
import com.youcode.myaftas.dto.responseDTO.LevelRespDto;
import com.youcode.myaftas.entities.Fish;
import com.youcode.myaftas.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fish")
public class FishController {
    @Autowired
    private FishService fishService;


    @PostMapping
    public ResponseEntity<Map<String, Object>> createFish(@Valid @RequestBody FishDto fishDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message",fishService.create(fishDto));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (Exception e){
            message.put("error", "Fish not created ! ");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, Object>> deleteFish(@PathVariable String name){
        Map<String, Object> message = new HashMap<>();
        try{
            fishService.delete(name);
            message.put("messge", "Fish deleted successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Fish Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getOneFish(@PathVariable String name){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", fishService.getOne(name));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Fish Not Found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/search")
    public ResponseEntity<List<Fish>> searchFishByName(@RequestParam String name) {
        List<Fish> members = fishService.searchFishsByName(name);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFish(){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", fishService.findAll());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Fish Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{name}")
    public ResponseEntity<Map<String, Object>> updateFish(@PathVariable String name, @Valid @RequestBody FishDto fishDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", fishService.update(name, fishDto));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Fish Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @GetMapping("/paginated")
    public ResponseEntity<List<FishRespDto>> getPaginatedFish(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fishService.findWithPagination(pageable).getContent());
    }


}
