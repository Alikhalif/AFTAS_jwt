package com.youcode.myaftas.Controller;

import com.youcode.myaftas.Utils.RankingId;
import com.youcode.myaftas.dto.MemberDto;
import com.youcode.myaftas.dto.RankingDto;
import com.youcode.myaftas.service.RankingService;
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
//@RequestMapping("/api/ranking")
public class RankingController {
    @Autowired
    private RankingService rankingService;


    @PostMapping("/api/ranking")
    public ResponseEntity<Map<String, Object>> createRanking(@Valid @RequestBody RankingDto rankingDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message",rankingService.create(rankingDto));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (Exception e){
            message.put("error", "ranking not created");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/api/ranking/calculate/{compName}")
    public ResponseEntity<Map<String, Object>> calcule(@PathVariable String compName){
        Map<String, Object> message = new HashMap<>();
        try{
            rankingService.calculate(compName);
            //message.put("message", "Ranking successfuly");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/ranking/top3/{compName}")
    public ResponseEntity<Map<String, Object>> getTop3Rank(@PathVariable String compName){
        Map<String, Object> message = new HashMap<>();
        try{

            message.put("message", rankingService.getTop3Rank(compName));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Ranking Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/api/ranking/{code}/{id}")
    public ResponseEntity<Map<String, Object>> deleteRanking(@PathVariable String code, @PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            rankingService.delete(code, id);
            message.put("messge", "Ranking deleted successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Ranking Not deleted");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/ranking/{code}/{id}")
    public ResponseEntity<Map<String, Object>> getOneRanking(@PathVariable String code, @PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", rankingService.getOne(code, id));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Ranking Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/ranking/all")
    public ResponseEntity<Map<String, Object>> getAllRankings(){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", rankingService.findAll());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Ranking Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/ranking/{code}/{id}")
    public ResponseEntity<Map<String, Object>> updateRanking(@PathVariable String code, @PathVariable Integer id, @Valid @RequestBody RankingDto rankingDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", rankingService.update(code, id, rankingDto));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Ranking Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/api/ranking/paginated")
    public ResponseEntity<List<RankingDto>> getPaginatedRankings(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(rankingService.findWithPagination(pageable).getContent());
    }
}
