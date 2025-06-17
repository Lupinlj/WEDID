// MessageEntry.java
// This class is a blueprint for a single message.
// It holds all the details about one message in separate, organized variables.
import java.util.Objects; // Needed for the equals and hashCode methods later

public class MessageEntry {
    // These are the "compartments" in your message box.
    // 'private' means only methods inside this class can directly change them.
    private String messageId;
    private String messageHash;
    private String recipient;
    private String messageContent;
    private MessageStatus status; // This uses your new MessageStatus enum!

    // This is the constructor. It's like the factory that builds a new MessageEntry box.
    // When you create a new message, you give it all the pieces of information here.
    public MessageEntry(String messageId, String messageHash, String recipient, String messageContent, MessageStatus status) {
        this.messageId = messageId; // 'this.messageId' refers to the variable in this class
        this.messageHash = messageHash;
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.status = status;
    }

    // These are "getter" methods. They let other parts of your code
    // safely get (read) the information stored inside this MessageEntry object.
    public String getMessageId() {
        return messageId;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public MessageStatus getStatus() {
        return status;
    }

    // This special method helps you print the MessageEntry object nicely.
    // When you do System.out.println(myMessageEntryObject), this method is used.
    @Override
    public String toString() {
        return String.format("ID: %s, Hash: %s, Recipient: %s, Content: %s, Status: %s",
                messageId, messageHash, recipient, messageContent, status);
    }

    // These methods are important if you want to compare MessageEntry objects
    // or store them in lists/maps that need to know if two objects are "equal".
    // For a first-year, just know that they help Java understand when two MessageEntry objects
    // are considered the same, even if they are different objects in memory, as long as their content is the same.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null | getClass() != o.getClass()) return false;
        MessageEntry that = (MessageEntry) o;
        return messageId.equals(that.messageId) &&
                messageHash.equals(that.messageHash) &&
                recipient.equals(that.recipient) &&
                messageContent.equals(that.messageContent) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, messageHash, recipient, messageContent, status);
    }
}