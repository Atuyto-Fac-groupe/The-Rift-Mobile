package com.SAE.Serveur.Model;

import jakarta.persistence.*;

@Entity(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idPlayer;
    private String name;
    private TypePlayer type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "ProgressPlayer")
    private Progress progress;

    public Player() {}


    public void setId(Long id) {
        this.idPlayer = id;
    }

    public Long getId() {
        return idPlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypePlayer getType() {
        return type;
    }

    public void setType(TypePlayer type) {
        this.type = type;
    }
}
