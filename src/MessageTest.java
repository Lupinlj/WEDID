import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageLength() {
        String shortMsg = "This is a short message.";
        String longMsg = new String(new char[261]).replace("\0", "a"); // 261 characters
        assertEquals("Message ready to send.", Message.checkMessageLength(shortMsg));
        assertEquals("Message exceeds 250 characters by 11, please reduce size.", Message.checkMessageLength(longMsg));
    }

    @Test
    public void testCheckRecipientCell() {
        String validCell = "+27839892765";
        String invalidCell = "0839892765";
        assertEquals("Cell phone number successfully captured.", Message.checkRecipientCell(validCell));
        assertEquals("Cell phone number is incorrectly formatted or does not contain an International code. Please correct the number and try again.", Message.checkRecipientCell(invalidCell));
    }

    @Test
    public void testCreateMessageHash() {
        // Assuming messagesSent is reset to 0 before each test
        Message.resetMessagesSent();
        String messageId = "00ABCDEFG";
        String messageContent = "Hi Mike, can you join us for dinner tonight?";
        String expectedHash = "00:01:TONIGHT";
        assertEquals(expectedHash, Message.createMessageHash(messageId, messageContent));
    }

    @Test
    public void testGenerateRandomMessageID() {
        String id = Message.generateRandomMessageID();
        assertTrue(id.length() == 10);
        assertTrue(id.matches("[a-zA-Z0-9]{10}"));
    }

    @Test
    public void testMessageSentAction() {
        assertEquals("Message successfully sent.", Message.messageSentAction("Send"));
        assertEquals("Press 1 to delete message.", Message.messageSentAction("Disregard"));
        assertEquals("Message successfully stored.", Message.messageSentAction("Store"));
    }
}