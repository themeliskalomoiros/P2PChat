package gr.kalymnos.skemelio.p2pchat.mvc_model;

public class Message {

    private String message, sender;

    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
