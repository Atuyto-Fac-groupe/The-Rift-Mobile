package com.SAE.Serveur.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idProgress;

    private int maxStape;

    @OneToMany(mappedBy = "progress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stape> stapes;

    public Progress() {}

    public long getId() {
        return idProgress;
    }

    public void setId(long id) {
        this.idProgress = id;
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
