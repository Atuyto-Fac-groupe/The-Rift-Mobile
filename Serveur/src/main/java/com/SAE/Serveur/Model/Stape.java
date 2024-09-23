package com.SAE.Serveur.Model;

import jakarta.persistence.*;

public class Stape {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String QuestType;

    @ManyToOne
    @JoinColumn(name = "id")
    private Progress progress;

    public Stape() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestType() {
        return QuestType;
    }

    public void setQuestType(String questType) {
        QuestType = questType;
    }
}
