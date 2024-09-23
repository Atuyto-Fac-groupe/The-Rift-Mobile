package com.SAE.Serveur.Model;

import jakarta.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private TypePlayer type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "id")
    private Progress progress;

    public Player() {}


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
