package com.SAE.Serveur.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int maxStape;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stape> stapes;

    public Progress() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMaxStape() {
        return maxStape;
    }

    public void setMaxStape(int maxStape) {
        this.maxStape = maxStape;
    }

    public List<Stape> getStapes() {
        return stapes;
    }

    public void setStapes(List<Stape> stapes) {
        this.stapes = stapes;
    }
}
