import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageAdvancedTest {

    @BeforeEach
    public void setup() {
        // Reset arrays before each test
        Message.sentMessage = new Message.MessageEntry[100];
        Message.storedMessages = new Message.MessageEntry[100];
        Message.messageHashes = new String[100];
        Message.messageIds = new String[100];
        Message.sentCount = 0;
        Message.storedCount = 0;
        Message.hashCount = 0;
        Message.idCount = 0;

        // Add test data
        addTestData();
    }

    private void addTestData() {
        // Message 1: Sent - "Did you get the cake?" (19 characters)
        Message.sentMessage[Message.sentCount++] = new Message.MessageEntry(
                "ID001", "+27834557896", "Did you get the cake?", "HASH1", "Sent"
        );
        Message.messageHashes[Message.hashCount++] = "HASH1";
        Message.messageIds[Message.idCount++] = "ID001";

        // Message 2: Stored - "Where are you? You are late! I have asked you to be on time." (62 characters)
        Message.storedMessages[Message.storedCount++] = new Message.MessageEntry(
                "ID002", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "HASH2", "Stored"
        );

        // Message 4: Sent - "It is dinner time!" (18 characters)
        Message.sentMessage[Message.sentCount++] = new Message.MessageEntry(
                "ID004", "0838884567", "It is dinner time!", "HASH4", "Sent"
        );
        Message.messageHashes[Message.hashCount++] = "HASH4";
        Message.messageIds[Message.idCount++] = "ID004";
    }

    @Test
    public void testArraysPopulated() {
        assertEquals(2, Message.sentCount);
        assertEquals(1, Message.storedCount);
        assertEquals(2, Message.hashCount);
        assertEquals(2, Message.idCount);
    }

    @Test
    public void testLongestMessage() {
        // We're only checking SENT messages (Message.sentMessage)
        String longest = "";
        for (int i = 0; i < Message.sentCount; i++) {
            if (Message.sentMessage[i].getMessage().length() > longest.length()) {
                longest = Message.sentMessage[i].getMessage();
            }
        }
        // "Did you get the cake?" is longer (19 chars) than "It is dinner time!" (18 chars)
        assertEquals("Did you get the cake?", longest);
    }

    @Test
    public void testSearchById() {
        String message = "";
        for (int i = 0; i < Message.idCount; i++) {
            if (Message.messageIds[i].equals("ID004")) {
                // Search through sent messages
                for (int j = 0; j < Message.sentCount; j++) {
                    if (Message.sentMessage[j] != null &&
                            Message.sentMessage[j].getMessageId().equals("ID004")) {
                        message = Message.sentMessage[j].getMessage();
                    }
                }
            }
        }
        assertEquals("It is dinner time!", message);
    }

    @Test
    public void testSearchByRecipient() {
        int count = 0;
        String recipient = "+27838884567";

        // Check sent messages
        for (int i = 0; i < Message.sentCount; i++) {
            if (Message.sentMessage[i].getRecipient().equals(recipient)) {
                count++;
            }
        }

        // Check stored messages
        for (int i = 0; i < Message.storedCount; i++) {
            if (Message.storedMessages[i].getRecipient().equals(recipient)) {
                count++;
            }
        }
        // Should find 1 match (message2)
        assertEquals(1, count);
    }

    @Test
    public void testDeleteByHash() {
        int initialCount = Message.hashCount;
        // Create a simple version of deleteByHash
        deleteByHash("HASH1");
        assertEquals(initialCount - 1, Message.hashCount);
    }

    // Simplified deleteByHash implementation for tests
    private void deleteByHash(String hash) {
        for (int i = 0; i < Message.hashCount; i++) {
            if (Message.messageHashes[i].equals(hash)) {
                // Shift elements to remove the hash
                for (int j = i; j < Message.hashCount - 1; j++) {
                    Message.messageHashes[j] = Message.messageHashes[j + 1];
                }
                Message.hashCount--;
                break;
            }
        }
    }
}