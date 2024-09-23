package com.SAE.Serveur.Controler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeepAlive {

    @GetMapping("/api/keepAlive")
    public ResponseEntity<?> keepAlive(){
        return ResponseEntity.ok().build();
    }
}
