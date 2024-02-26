package com.youcode.myaftas.Controller;

import com.youcode.myaftas.dto.MemberDto;
import com.youcode.myaftas.dto.responseDTO.MemberRespDto;
import com.youcode.myaftas.service.ImplService.AuthenticationService;
import com.youcode.myaftas.service.MemberService;
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
//@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    private AuthenticationService memberService;


    @PostMapping("/api/member")
    public ResponseEntity<Map<String, Object>> createMember(@Valid @RequestBody MemberDto memberDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message",memberService.create(memberDto));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }catch (Exception e){
            message.put("error", "Member Not created");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/api/member/{id}")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            memberService.delete(id);
            message.put("messge", "Member deleted successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/member/num/{id}")
    public ResponseEntity<Map<String, Object>> getOneMember(@PathVariable Integer id){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", memberService.getOne(id));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/member/name/{name}")
    public ResponseEntity<Map<String, Object>> getMemberByName(@PathVariable String name){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", memberService.getByName(name));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/member/familyname/{fname}")
    public ResponseEntity<Map<String, Object>> getMemberByFamilyName(@PathVariable String fname){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", memberService.getByFamilyName(fname));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/member/all")
    public ResponseEntity<Map<String, Object>> getAllMembers(){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", memberService.getAll());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", "Members Not found");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/member/{id}")
    public ResponseEntity<Map<String, Object>> updateMember(@PathVariable Integer id, @Valid @RequestBody MemberDto memberDto){
        Map<String, Object> message = new HashMap<>();
        try{
            message.put("message", memberService.update(id, memberDto));
            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/api/member/paginated")
    public ResponseEntity<List<MemberRespDto>> getPaginatedMembers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(memberService.findWithPagination(pageable).getContent());
    }

}
