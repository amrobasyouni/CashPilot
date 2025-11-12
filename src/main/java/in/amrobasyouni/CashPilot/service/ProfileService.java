package in.amrobasyouni.CashPilot.service;


import in.amrobasyouni.CashPilot.dto.ProfileDTO;
import in.amrobasyouni.CashPilot.entity.ProfileEntity;
import in.amrobasyouni.CashPilot.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private  final ProfileRepository profileRepository;

    public ProfileDTO registerProfile(ProfileDTO dto){
        ProfileEntity newProfile = toEntity(dto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        return toDto(newProfile);
    }

    public ProfileEntity toEntity(ProfileDTO dto){
        return ProfileEntity.builder()
                .Id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(dto.getPassword())
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
}
