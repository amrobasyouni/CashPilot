package in.amrobasyouni.CashPilot.service;


import in.amrobasyouni.CashPilot.dto.AuthDTO;
import in.amrobasyouni.CashPilot.dto.ProfileDTO;
import in.amrobasyouni.CashPilot.entity.ProfileEntity;
import in.amrobasyouni.CashPilot.repository.ProfileRepository;
import in.amrobasyouni.CashPilot.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;

    public ProfileDTO registerProfile(ProfileDTO dto){
        ProfileEntity newProfile = toEntity(dto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        String activationLink = "http://localhost:8080/api/v1.0/activate?token="+newProfile.getActivationToken();
        String subject = "Activate your CashPilot Account";
        String body = "Click to the link to activate your account: " + activationLink;
        emailService.sendEmail(newProfile.getEmail(),subject,body);
        return toDto(newProfile);
    }

    public ProfileEntity toEntity(ProfileDTO dto){
        return ProfileEntity.builder()
                .Id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .profileImageUrl(dto.getProfileImageUrl())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public ProfileDTO toDto(ProfileEntity entity){
        return ProfileDTO.builder()
                .Id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .profileImageUrl(entity.getProfileImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken){
        return profileRepository.findByActivationToken(activationToken)
                .map(profile ->{
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
                }

    public boolean isAccountActive(String email){
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       return profileRepository.findByEmail(authentication.getName())
               .orElseThrow(()->new UsernameNotFoundException("Profile Not Found With Email: "+authentication.getName()));
    }

    public ProfileDTO getPublicProfile(String email){
        ProfileEntity currentUser = null;
       if(email == null){
          currentUser = getCurrentProfile();
       }else{
          currentUser =  profileRepository.findByEmail(email)
                   .orElseThrow(()->new UsernameNotFoundException("Profile not found for the email: "+email));
    }
       return toDto(currentUser);
    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
        //Generate te JWT token
            UserDetails user = appUserDetailsService.loadUserByUsername(authDTO.getEmail());
           String token = jwtUtil.generateToken(user);
            return Map.of("token",token,"User",getPublicProfile(authDTO.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid Email or Password");
        }
    }
}
