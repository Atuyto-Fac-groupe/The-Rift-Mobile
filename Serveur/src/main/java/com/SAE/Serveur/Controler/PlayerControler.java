package com.SAE.Serveur.Controler;

import com.SAE.Serveur.Controler.Repository.PlayerRepository;
import com.SAE.Serveur.Model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Player")
public class PlayerControler {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/getPlayer/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable long id) {
        Optional<Player> player = playerRepository.findById(id);
        return player.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/getAllPlayer")
    public ResponseEntity<List<Player>> getAllPlayer() {
        List<Player> players = playerRepository.findAll();
        return ResponseEntity.ok().body(players);
    }

    @GetMapping("/deletePlayer/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable long id) {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            playerRepository.delete(player.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/updatePlayer")
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) {
        playerRepository.save(player);
        return ResponseEntity.ok(player);
    }


}
