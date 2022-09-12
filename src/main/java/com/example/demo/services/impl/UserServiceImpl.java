package com.example.demo.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.demo.utils.Utils.loadProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final GroupPostRepository groupPostRepository;
    private final ReactionRepository reactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;
    private final PostServiceImpl postServiceImpl;
    private final SessionServiceImpl sessionService;

    public static String generateToken(Map<String, Object> payload, org.springframework.security.core.userdetails.User user) {
        Properties prop = loadProperties("jwt.setting.properties");
        assert prop != null;
        String key = prop.getProperty("key");
        String accessExpired = prop.getProperty("access_expired");
        assert key != null;
        assert accessExpired != null;
        long expiredIn = Long.parseLong(accessExpired);
        Algorithm algorithm = Algorithm.HMAC256(key);

        return JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + expiredIn)).withClaim("user", payload).sign(algorithm);
    }

    @Override
    public AbstractResponse login(LoginRequestDto loginRequestDto) throws RuntimeException {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BadCredentialsException e) {
            return new AbstractResponse("FAILED", "INCORRECT_EMAIL_OR_PASSWORD", 400);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        User detectedUser = userRepository.findByEmail(email);

        if (detectedUser == null || !detectedUser.getIsActive()) {
            return new AbstractResponse("FAILED", "FORBIDDEN", 400);
        }

        if (detectedUser.getToken() != null) {
            return new AbstractResponse("FAILED", "ALREADY_LOGGED_IN", 400, detectedUser.getToken());
        }

        Map<String, Object> payload = new HashMap<>();

        payload.put("id", detectedUser.getId());
        payload.put("email", detectedUser.getEmail());
        payload.put("role", detectedUser.getRole().getRoleName());

        Properties prop = loadProperties("jwt.setting.properties");

        String token = generateToken(payload, new org.springframework.security.core.userdetails.User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));

        detectedUser.setToken(token);

        userRepository.save(detectedUser);

        return new AbstractResponse(new LoginDto("Bearer", token, prop.getProperty("access_expired")));
    }

    @Override
    public AbstractResponse logout(String token) {
        if (!token.startsWith("Bearer")) {
            return new AbstractResponse("FAILED", "MISSING_BEARER_PREFIX", 400);
        }
        token = token.split(" ")[1];
        User user = userRepository.findByToken(token);
        if (user == null) {
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        } else if (user.getToken() == null) {
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        user.setToken(null);
        userRepository.save(user);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse register(RegisterRequestDto registerRequestDto) {

        User foundUser = userRepository.findByEmail(registerRequestDto.getEmail());

        if (foundUser != null) {
            return new AbstractResponse("FAILED", "EMAIL_EXISTED", 400);
        }

        User user = new User();

        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(roleRepository.findRoleById(3));
        user = userRepository.save(user);

        UserProfile userProfile = new UserProfile(user, registerRequestDto.getFullName(), registerRequestDto.getAddress(), registerRequestDto.getAge(), registerRequestDto.getGender(), registerRequestDto.getPhone());

        userProfileRepository.save(userProfile);

        return new AbstractResponse();
    }

    @Override
    public AbstractResponse getUserProfile(Integer id) {
        if (sessionService.isTokenExpire()) {
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        User user = userRepository.findUserById(id);
        if(user == null){
            return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
        }
        UserProfile userProfile = userProfileRepository.findByUser(user);
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getId().longValue());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setFullName(userProfile.getFullName());
        userProfileDto.setAge(userProfile.getAge());
        userProfileDto.setGender(userProfile.getGender());
        userProfileDto.setPhone(userProfile.getPhone());
        List<GroupPost> groupPostList = groupPostRepository.findAllByCreatedBy(user.getEmail());
        List<PostSearchResultDto> postSearchResultDtoList;
        postSearchResultDtoList = postServiceImpl.convertPostToPostDto(groupPostList);
        userProfileDto.setPostSearchResultDtoList(postSearchResultDtoList);
        DoctorProfile doctorProfile = doctorProfileRepository.findByUser(user);
        if(doctorProfile == null){
            return new AbstractResponse(userProfileDto);
        } else {
            userProfileDto.setCertificate(doctorProfile.getCertificate());
            userProfileDto.setDegree(doctorProfile.getDegree());
            userProfileDto.setExpYear(doctorProfile.getExpYear());
            userProfileDto.setSpecialist(doctorProfile.getSpecialist());
            userProfileDto.setWorkingAt(doctorProfile.getWorkingAt());
            userProfileDto.setPrivateWeb(doctorProfile.getPrivateWeb());
            userProfileDto.setStartWorkAtTime(doctorProfile.getStartWorkAtTime());
            userProfileDto.setEndWorkAtTime(doctorProfile.getEndWorkAtTime());
            userProfileDto.setWorkAt(doctorProfile.getWorkAt());
        }
        return new AbstractResponse(userProfileDto);
    }
}
