package com.youcode.myaftas.Controller;

import com.youcode.myaftas.dto.HuntingDto;
import com.youcode.myaftas.dto.LevelDto;
import com.youcode.myaftas.dto.responseDTO.LevelRespDto;
import com.youcode.myaftas.service.HuntingService;
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
@RequestMapping("/api/hunting")
public class HuntingController {
    @Autowired
    private HuntingService huntingService;


    @PostMapping
    public ResponseEntity<Map<String, Object>> createHunting(@Valid @RequestBody List<HuntingDto> huntingDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("messge", huntingService.create(huntingDto));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (Exception e){
            message.put("error", "Hunting Not created");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteHunting(@PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            huntingService.delete(id);
            message.put("messge", "Hunting deleted successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Hunting Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOneHunting(@PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", huntingService.getOne(id));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Hunting Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllHunting(){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", huntingService.findAll());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Hunting Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateHunting(@PathVariable Integer id, @Valid @RequestBody HuntingDto huntingDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", huntingService.update(id, huntingDto));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Hunting Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<HuntingDto>> getPaginatedLevel(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(huntingService.findWithPagination(pageable).getContent());
    }

}
