package in.amrobasyouni.CashPilot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HomeController {
    @GetMapping
        public ResponseEntity<String> healthCheck(){

        return ResponseEntity.ok("app is running");
        }
    }
