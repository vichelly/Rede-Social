package com.example.crud.model;

public class PostDTO {
    private String userId;
    private String content;
    private int logicalTimestamp; // opcional no envio, será preenchido no serviço

    public PostDTO() {}

    public PostDTO(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLogicalTimestamp() { return logicalTimestamp; }
    public void setLogicalTimestamp(int logicalTimestamp) { this.logicalTimestamp = logicalTimestamp; }
}
