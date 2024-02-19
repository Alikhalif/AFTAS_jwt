package com.youcode.myaftas.service;

import com.youcode.myaftas.dto.MemberDto;
import com.youcode.myaftas.dto.responseDTO.MemberRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    MemberRespDto create(MemberDto memberDto);
    void delete(Integer id);
    MemberRespDto getOne(Integer id);
    List<MemberRespDto> getByName(String name);
    List<MemberRespDto> getByFamilyName(String fname);
    List<MemberRespDto> getAll();
    MemberRespDto update(Integer id, MemberDto memberDto);
    Page<MemberRespDto> findWithPagination(Pageable pageable);

}
