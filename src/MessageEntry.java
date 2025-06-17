public class MessageEntry {
    private String messageId;
    private String recipient;
    private String message;
    private String hash;
    private String flag; // Sent, Stored, Disregarded

    public MessageEntry(String messageId, String recipient, String message, String hash, String flag) {
        this.messageId = messageId;
        this.recipient = recipient;
        this.message = message;
        this.hash = hash;
        this.flag = flag;
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getHash() { return hash; }
    public String getFlag() { return flag; }
}