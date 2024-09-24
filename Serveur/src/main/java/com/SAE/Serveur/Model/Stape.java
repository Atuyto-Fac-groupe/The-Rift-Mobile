package com.SAE.Serveur.Model;

import jakarta.persistence.*;

@Entity(name = "stape")
public class Stape {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idStape;

    private String QuestType;

    @ManyToOne
    @JoinColumn(name = "idProgress")
    private Progress progress;

    public Stape() {}

    public int getId() {
        return idStape;
    }

    public void setId(int id) {
        this.idStape = id;
    }

    public String getQuestType() {
        return QuestType;
    }

    public void setQuestType(String questType) {
        QuestType = questType;
    }
}
