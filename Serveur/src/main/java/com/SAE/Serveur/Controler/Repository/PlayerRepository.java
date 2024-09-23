package com.SAE.Serveur.Controler.Repository;

import com.SAE.Serveur.Model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {


}
