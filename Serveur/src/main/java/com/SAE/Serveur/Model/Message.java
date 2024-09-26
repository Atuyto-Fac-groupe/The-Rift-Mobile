package main.Model;

public class Message {

    private String message;
    private String from;
    private String to;
    private boolean see;

    public Message() {
        see = false;
    }

    public Message(String message, String from, String to) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.see = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
