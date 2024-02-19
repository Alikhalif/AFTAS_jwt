package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Exception.ResourceNotFoundException;
import com.youcode.myaftas.dto.MemberDto;
import com.youcode.myaftas.dto.responseDTO.MemberRespDto;
import com.youcode.myaftas.entities.Member;
import com.youcode.myaftas.repositories.MemberRepository;
import com.youcode.myaftas.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MemberRespDto create(MemberDto memberDto){
        Member member = modelMapper.map(memberDto, Member.class);
        member = memberRepository.save(member);
        return modelMapper.map(member, MemberRespDto.class);
    }

    @Override
    public void delete(Integer id){
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Question not found with id "+id));
        memberRepository.delete(member);
    }

    @Override
    public MemberRespDto getOne(Integer id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id "+id));
        return modelMapper.map(member, MemberRespDto.class);
    }

    @Override
    public List<MemberRespDto> getByName(String name){
        List<Member> members = memberRepository.findByName(name);
        if (members.isEmpty()) {
            throw new ResourceNotFoundException("Member not found with name " + name);
        }
        return members.stream()
                .map(member -> modelMapper.map(member, MemberRespDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberRespDto> getByFamilyName(String fname){
        List<Member> members = memberRepository.findByFamilyName(fname);
        if (members.isEmpty()) {
            throw new ResourceNotFoundException("Member not found with family Name " + fname);
        }
        return members.stream()
                .map(member -> modelMapper.map(member, MemberRespDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberRespDto> getAll(){
        return Arrays.asList(modelMapper.map(memberRepository.findAll(), MemberRespDto[].class));
    }

    @Override
    public MemberRespDto update(Integer id, MemberDto memberDto){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id "+id));

        if(memberDto.getName().isEmpty() == false){
            member.setName(memberDto.getName());
        }
        if(memberDto.getFamilyName().isEmpty() == false){
            member.setFamilyName(memberDto.getFamilyName());
        }
        if(memberDto.getAccessionDate() != null){
            member.setAccessionDate(memberDto.getAccessionDate());
        }
        if (memberDto.getNationality().isEmpty() == false){
            member.setNationality(memberDto.getNationality());
        }
        if(memberDto.getIdentityDocument() != null){
            member.setIdentityDocumentType(memberDto.getIdentityDocument());
        }
        if(memberDto.getIdentityNumber().isEmpty() == false){
            member.setIdentityNumber(memberDto.getIdentityNumber());
        }

        member = memberRepository.save(member);
        return modelMapper.map(member, MemberRespDto.class);
    }



    @Override
    public Page<MemberRespDto> findWithPagination(Pageable pageable) {
        Page<Member> membersPage = memberRepository.findAll(pageable);
        return membersPage.map(member -> modelMapper.map(member, MemberRespDto.class));
    }
}
