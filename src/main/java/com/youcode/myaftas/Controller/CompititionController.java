package com.youcode.myaftas.Controller;

import com.youcode.myaftas.dto.CompetitionDto;
import com.youcode.myaftas.dto.responseDTO.CompetitionRespDto;
import com.youcode.myaftas.service.CompititionService;
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
@RequestMapping("/api/compitition")
public class CompititionController {
    @Autowired
    private CompititionService compititionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCompitition(@Valid @RequestBody CompetitionDto competitionDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message",compititionService.create(competitionDto));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (Exception e){
            message.put("error", e);
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable String code){
        Map<String, Object> message = new HashMap<>();
        try{
            compititionService.delete(code);
            message.put("messge", "Compitition deleted successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Compitition Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<Map<String, Object>> getOneMember(@PathVariable String code){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", compititionService.getOne(code));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Compitition Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllMembers(){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", compititionService.getAll());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Compitition Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<Map<String, Object>> updateQuestion(@PathVariable String code, @Valid @RequestBody CompetitionDto competitionDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", compititionService.update(code, competitionDto));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Compitition Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<CompetitionRespDto>> getPaginatedCompetitions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(compititionService.findWithPagination(pageable).getContent());
    }

}
