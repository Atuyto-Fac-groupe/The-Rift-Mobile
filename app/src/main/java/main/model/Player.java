package main.model;


import java.util.ArrayList;
import java.util.List;

public class Player {


    private long idPlayer;
    private String name;
    private TypePlayer type;

    private double x;
    private double y;

    private List<Message> messages;

//    private Progress progress;

    public Player() {
        this.messages = new ArrayList<Message>();
    }

    public int getNotSee(){
        int cpt = 0;
        for (Message m : this.messages) {
            if (!m.isSee()){
                cpt++;
            }
        }
        return cpt;
    }
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void add(Message message) {
        messages.add(message);
    }

    public long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
