package com.SAE.Serveur.Controler.Repository;

import com.SAE.Serveur.Model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
