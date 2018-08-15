package sg.edu.rp.webservices.dmsdchatapp;

import android.text.format.DateFormat;



public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String id;
    public ChatMessage(){

    }

    public ChatMessage(String messageText, long messageTime, String messageUser) {
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.messageUser = messageUser;
        DateFormat.format("dd-MM-yyyy (HH:mm:ss)", messageTime);
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
