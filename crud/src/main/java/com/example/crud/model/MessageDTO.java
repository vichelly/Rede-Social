package com.example.crud.model;

public class MessageDTO {
    private String fromUserId;
    private String toUserId;
    private String content;
    private int logicalTimestamp;

    public MessageDTO() {}

    public MessageDTO(String fromUserId, String toUserId, String content) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
    }

    public String getFromUserId() { return fromUserId; }
    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }

    public String getToUserId() { return toUserId; }
    public void setToUserId(String toUserId) { this.toUserId = toUserId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLogicalTimestamp() { return logicalTimestamp; }
    public void setLogicalTimestamp(int logicalTimestamp) { this.logicalTimestamp = logicalTimestamp; }
}
