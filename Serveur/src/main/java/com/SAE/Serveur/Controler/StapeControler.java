package com.SAE.Serveur.Controler;

import com.SAE.Serveur.Controler.Repository.StapeRepository;
import com.SAE.Serveur.Model.Stape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Stape")
public class StapeControler {

    @Autowired
    private StapeRepository stapeRepository;

    @GetMapping("/getStape/{id}")
    public ResponseEntity<Stape> getStape(@PathVariable long id) {
        Optional<Stape> stape = stapeRepository.findById(id);

        return stape.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getAllStape")
    public ResponseEntity<List<Stape>> getAllStape() {
        List<Stape> stape = stapeRepository.findAll();
        return ResponseEntity.ok(stape);
    }

    @GetMapping("/deleteStape/{id}")
    public ResponseEntity<Stape> deleteStape(@PathVariable long id) {
        Optional<Stape> stape = stapeRepository.findById(id);
        if (stape.isPresent()) {
            stapeRepository.delete(stape.get());
            // Retourne l'objet après suppression
            return ResponseEntity.ok(stape.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/updateStape")
    public ResponseEntity<Stape> updateStape(@RequestBody Stape stape) {
        stapeRepository.save(stape);
        return ResponseEntity.ok(stape);
    }
}
