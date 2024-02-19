package com.youcode.myaftas.Controller;

import com.youcode.myaftas.dto.LevelDto;
import com.youcode.myaftas.dto.responseDTO.LevelRespDto;
import com.youcode.myaftas.service.LevelService;
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

@Controller
@RequestMapping("/api/level")
public class LevelController {
    @Autowired
    private LevelService levelService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createLevel(@Valid @RequestBody LevelDto levelDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message",levelService.create(levelDto));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (Exception e){
            message.put("error", "Level Not created");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteLevel(@PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            levelService.delete(id);
            message.put("messge", "Level deleted successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Level Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOneLevel(@PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", levelService.getOne(id));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Level Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllLevels(){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", levelService.findAll());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Level Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateQuestion(@PathVariable Integer id, @Valid @RequestBody LevelDto levelDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", levelService.update(id, levelDto));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Member Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<LevelRespDto>> getPaginatedLevel(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(levelService.findWithPagination(pageable).getContent());
    }
}
