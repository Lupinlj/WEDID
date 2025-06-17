import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageAdvancedTest {

    @BeforeEach
    public void setup() {
        // Reset arrays before each test
        Message.sentMessages = new MessageEntry[100];
        Message.storedMessages = new MessageEntry[100];
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
        // Message 1: Sent
        Message.sentMessages[Message.sentCount++] = new MessageEntry(
                "ID001", "+27834557896", "Did you get the cake?", "HASH1", "Sent"
        );
        Message.messageHashes[Message.hashCount++] = "HASH1";
        Message.messageIds[Message.idCount++] = "ID001";

        // Message 2: Stored
        Message.storedMessages[Message.storedCount++] = new MessageEntry(
                "ID002", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "HASH2", "Stored"
        );

        // Message 4: Sent
        Message.sentMessages[Message.sentCount++] = new MessageEntry(
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
        String longest = "";
        for (int i = 0; i < Message.sentCount; i++) {
            if (Message.sentMessages[i].getMessage().length() > longest.length()) {
                longest = Message.sentMessages[i].getMessage();
            }
        }
        assertEquals("It is dinner time!", longest);
    }

    @Test
    public void testSearchById() {
        String message = "";
        for (int i = 0; i < Message.idCount; i++) {
            if (Message.messageIds[i].equals("ID004")) {
                for (MessageEntry msg : Message.sentMessages) {
                    if (msg != null && msg.getMessageId().equals("ID004")) {
                        message = msg.getMessage();
                    }
                }
            }
        }
        assertEquals("It is dinner time!", message);
    }

    @Test
    public void testSearchByRecipient() {
        int count = 0;
        for (int i = 0; i < Message.sentCount; i++) {
            if (Message.sentMessages[i].getRecipient().equals("+27838884567")) {
                count++;
            }
        }
        for (int i = 0; i < Message.storedCount; i++) {
            if (Message.storedMessages[i].getRecipient().equals("+27838884567")) {
                count++;
            }
        }
        assertEquals(1, count); // Only 1 in test data
    }

    @Test
    public void testDeleteByHash() {
        int initialCount = Message.hashCount;
        Menu.deleteByHash("HASH1");
        assertEquals(initialCount - 1, Message.hashCount);
    }
}