package in.amrobasyouni.CashPilot.dto;

import in.amrobasyouni.CashPilot.entity.ProfileEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;



import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {


    private Long id;
    private String name;
    private Long profileId;
    private String  icon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String type;
    private ProfileEntity profile;
}
