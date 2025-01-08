package com.example.EECToronto.Message;
import java.util.*;

public class BulkMessages {
    private Long messageId;
    private List<String> phoneNumbers;
    private String message;
    private String author;
    private Long genderId;
    private Long receiverGroupId;

    public BulkMessages() {}
    public BulkMessages(List<String> phoneNumbers, String message, String author, Long genderId, Long receiverGroupId) {
        this.phoneNumbers = phoneNumbers;
        this.message = message;
        this.author = author;
        this.genderId = genderId;
        this.receiverGroupId = receiverGroupId;

    }
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public Long getGenderId() {
        return genderId;
    }
    public void setGenderId(Long genderId) {
        this.genderId = genderId;
    }
    public Long getReceiverGroupId() {
        return receiverGroupId;
    }
    public void setReceiverGroupId(Long receiverGroupId) {
        this.receiverGroupId = receiverGroupId;
    }
}
