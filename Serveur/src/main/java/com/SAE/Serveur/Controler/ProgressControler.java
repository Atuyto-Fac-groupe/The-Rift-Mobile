package com.SAE.Serveur.Controler;

import com.SAE.Serveur.Controler.Repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.SAE.Serveur.Model.Progress;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Progress")
public class ProgressControler {

    @Autowired
    private ProgressRepository progressRepository;

    @GetMapping("/getProgress/{id}")
    public ResponseEntity<Progress> getProgress(@PathVariable long id) {
        Optional<Progress> progress =progressRepository.findById(id);
        return progress.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getAllProgress")
    public ResponseEntity<List<Progress>> getAllProgress() {
        List<Progress> progress = progressRepository.findAll();
        return ResponseEntity.ok().body(progress);
    }

    @GetMapping("/deleteProgress/{id}")
    public ResponseEntity<Progress> deleteProgress(@PathVariable long id) {
        Optional<Progress> progress = progressRepository.findById(id);
        if (progress.isPresent()) {
            progressRepository.delete(progress.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update/Progress")
    public ResponseEntity<Progress> updateProgress(@RequestBody Progress progress) {
        progressRepository.save(progress);
        return ResponseEntity.ok(progress);
    }

}
