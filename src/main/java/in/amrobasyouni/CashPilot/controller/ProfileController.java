package in.amrobasyouni.CashPilot.controller;

import in.amrobasyouni.CashPilot.dto.AuthDTO;
import in.amrobasyouni.CashPilot.dto.ProfileDTO;
import in.amrobasyouni.CashPilot.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO){
        ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token){
        boolean isActivated = profileService.activateProfile(token);
        if(isActivated){
            return ResponseEntity.ok("Profile activated successfully");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation Token not found or already used");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO){
        try {

            if(!profileService.isAccountActive(authDTO.getEmail())){

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "Message","Account is not activated!"
                ));
            }
            Map<String,Object> response = profileService.authenticateAndGenerateToken(authDTO);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message",e.getMessage()));

        }

    }
}
