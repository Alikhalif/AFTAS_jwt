package com.youcode.myaftas.service.ImplService;

import com.youcode.myaftas.Exception.ResourceNotFoundException;
import com.youcode.myaftas.dto.MemberDto;
import com.youcode.myaftas.dto.authDTO.AuthenticationRequest;
import com.youcode.myaftas.dto.authDTO.AuthenticationResponse;
import com.youcode.myaftas.dto.authDTO.RegisterRequest;
import com.youcode.myaftas.dto.responseDTO.MemberRespDto;
import com.youcode.myaftas.entities.Member;
import com.youcode.myaftas.entities.Token;
import com.youcode.myaftas.entities.User;
import com.youcode.myaftas.enums.Roles;
import com.youcode.myaftas.repositories.MemberRepository;
import com.youcode.myaftas.repositories.TokenRepository;
import com.youcode.myaftas.repositories.UserRepository;
import com.youcode.myaftas.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements MemberService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        // check if user already exist. if exist than authenticate the user
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse("User already exist",null,null,null);
        }
        try {
            var user = User.builder()
                    .name(request.getName())
                    .familyName(request.getFamilyName())
                    .accessionDate(request.getAccessionDate())
                    .nationality(request.getNationality())
                    .identityNumber(request.getIdentityNumber())
                    .identityDocumentType(request.getIdentityDocumentType())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Roles.USER)
                    .build();


            user = repository.save(user);

            String jwt = jwtService.generateToken(user);

            Integer user_id = user.getId();
            String username = user.getUsername();
            String role = user.getRole().name();

            saveUserToken(jwt, user);

            return AuthenticationResponse.builder()
                    .token(jwt)
                    .user_id(user_id)
                    .username(username)
                    .role(role).build();

        } catch (DataIntegrityViolationException e) {
            return new AuthenticationResponse(e.getMessage(),null,null,null);
        }

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        Integer user_id = user.getId();
        String username = user.getUsername();
        String role = user.getRole().name();


        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return AuthenticationResponse.builder()
                .token(jwt)
                .user_id(user_id)
                .username(username)
                .role(role).build();

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }



    ////////////////////////
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public MemberRespDto create(MemberDto memberDto){
        User user = modelMapper.map(memberDto, Member.class);
        user = userRepository.save(user);
        return modelMapper.map(user, MemberRespDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public void delete(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Question not found with id "+id));
        userRepository.delete(user);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public MemberRespDto getOne(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id "+id));
        return modelMapper.map(user, MemberRespDto.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public List<MemberRespDto> getByName(String name){
        List<User> members = userRepository.findByName(name);
        if (members.isEmpty()) {
            throw new ResourceNotFoundException("Member not found with name " + name);
        }
        return members.stream()
                .map(member -> modelMapper.map(member, MemberRespDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public List<MemberRespDto> getByFamilyName(String fname){
        List<User> members = userRepository.findByFamilyName(fname);
        if (members.isEmpty()) {
            throw new ResourceNotFoundException("Member not found with family Name " + fname);
        }
        return members.stream()
                .map(member -> modelMapper.map(member, MemberRespDto.class))
                .collect(Collectors.toList());
    }

    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public List<MemberRespDto> getAll(){
        return Arrays.asList(modelMapper.map(userRepository.findAll(), MemberRespDto[].class));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public MemberRespDto update(Integer id, MemberDto memberDto){
        User member = userRepository.findById(id)
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

        member = userRepository.save(member);
        return modelMapper.map(member, MemberRespDto.class);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Override
    public Page<MemberRespDto> findWithPagination(Pageable pageable) {
        Page<User> membersPage = userRepository.findAll(pageable);
        return membersPage.map(member -> modelMapper.map(member, MemberRespDto.class));
    }
}
