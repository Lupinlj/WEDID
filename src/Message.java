import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Message {

    public static MessageEntry[] sentMessages;

    // MessageEntry class definition
    public static class MessageEntry {
        private String messageId;
        private String recipient;
        private String message;
        private String hash;
        private String flag;

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

    // These lists will now store MessageEntry objects.
    // ArrayList is like a dynamic list that can grow or shrink as needed.
    public static List<MessageEntry> allMessages = new ArrayList<>(); // A central list for ALL messages (sent, stored, disregarded)
    public static List<String> sentMessagesList = new ArrayList<String>(); // For messages explicitly sent
    public static List<MessageEntry> disregardedMessagesList = new ArrayList<>(); // For messages the user discarded
    public static List<MessageEntry> storedMessagesList = new ArrayList<>(); // For messages stored for later

    public static MessageEntry[] sentMessage = new MessageEntry[100];
    public static MessageEntry[] disregardedMessages = new MessageEntry[100];
    public static MessageEntry[] storedMessages = new MessageEntry[100];
    public static String[] messageHashes = new String[100];
    public static String[] messageIds = new String[100];

    public static int sentCount = 0;
    public static int disregardedCount = 0;
    public static int storedCount = 0;
    public static int hashCount = 0;
    public static int idCount = 0;

    // These HashMaps are like dictionaries. You give them a "key" (like an ID or hash)
    // and they quickly give you back the full MessageEntry object.
    public static HashMap<String, MessageEntry> messageIdMap = new HashMap<>(); // Maps Message ID to MessageEntry object
    public static HashMap<String, MessageEntry> messageHashToEntryMap = new HashMap<>(); // Maps Message Hash to MessageEntry object

    public static int totalMessages = 0; // This counts all messages in the system
    private static int messageLimit = 0;
    private static int messagesSentCounterForHash = 0;

    public static HashMap<String, String> messageMap = new HashMap<>();
    private static int messagesSent = 0;

    public static void message(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to QuickChat");

        // Ask user for message limit first
        System.out.println("How many messages do you want to send?");
        messageLimit = scanner.nextInt();
        scanner.nextLine();

        // Main QuickChat menu loop
        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Report Menu");
            System.out.println("4. Quit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                if (messagesSent < messageLimit) {
                    System.out.println("Please enter a message.");
                    sendMessage();

                    if (messagesSent >= messageLimit){
                        System.out.println("Message limit reached! ");
                    }
                } else {
                    System.out.println("You've reached your message limit of "+ messageLimit + " messages");
                }
            } else if (choice == 2) {
                showRecentlySent();
            } else if (choice == 3) {
                reportMenu rm = new reportMenu(scanner);
                rm.run();
            } else if (choice == 4) {
                System.out.println("Exiting QuickChat. Bye!!");
                break;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
            }
        }
        scanner.close();
    }

    public static void sendMessage() {
        Scanner scanner = new Scanner(System.in);

        // Generate random 10-digit message ID
        String messageId = generateRandomMessageID();
        System.out.println("Generated Message ID: " + messageId);

        // Validate message ID
        if (!checkMessageID(messageId)) {
            System.out.println("Invalid Message ID");
            return;
        }

        // Get recipient cell number
        System.out.println("Enter recipient cell number (+27 followed by 9 digits):");
        String cellNumber = scanner.nextLine();

        if (!checkRecipientCellNumber(cellNumber)) {
            System.out.println("Invalid cell number");
            return;
        }

        // Get message content
        System.out.println("Enter your message:");
        String messageContent = scanner.nextLine();

        // Check message length
        if (messageContent.length() > 250) {
            System.out.println("Please enter a message of less than 250 characters.");
            return;
        }

        // Create message hash
        String messageHash = createMessageHash(messageId, messageContent);

        // Create full message format
        String fullMessage = String.format("MessageID: %s, Message Hash: %s, Recipient: %s, Message: %s",
                messageId, messageHash, cellNumber, messageContent);

        // Ask user what to do with the message
        String action = SentMessage();

        // Display full message details
        System.out.println("\nMessage Details:");
        System.out.println("MessageID: " + messageId);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + cellNumber);
        System.out.println("Message: " + messageContent);

        // Handle the action
        switch (action) {
            case "Send":
                sentMessagesList.add(fullMessage);
                messageMap.put(messageId, fullMessage);

                MessageEntry sentEntry = new MessageEntry(messageId, cellNumber, messageContent, messageHash, "Sent");
                sentMessage[sentCount++] = sentEntry;
                messageHashes[hashCount++] = messageHash;
                messageIds[idCount++] = messageId;

                // Add to HashMaps for quick lookup
                messageIdMap.put(messageId, sentEntry);
                messageHashToEntryMap.put(messageHash, sentEntry);

                messagesSent++;  // Increment sent count
                totalMessages++; // Increment total messages

                System.out.println("Message sent successfully!");

                String printMessage= "MessageID: " + messageId
                        + "\nMessage Hash: " + messageHash
                        + "\nRecipient: " + cellNumber
                        + "\nMessage: " + messageContent;
                JOptionPane.showMessageDialog(null, printMessage, "Message Sent Successfully", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Disregard":
                MessageEntry disregardedEntry = new MessageEntry(messageId, cellNumber, messageContent, messageHash, "Disregarded");
                disregardedMessages[disregardedCount++] = disregardedEntry;
                System.out.println("Message discarded.");
                break;

            case "Store":
                MessageEntry storedEntry = new MessageEntry(messageId, cellNumber, messageContent, messageHash, "Stored");
                storedMessages[storedCount++] = storedEntry;
                storedMessagesList.add(storedEntry);

                String jsonMessage = storeMessage(fullMessage);
                System.out.println("Message stored: " + jsonMessage);

                // Write to JSON file
                writeStoredMessagesToFile();
                break;

            default:
                System.out.println("Invalid action.");
        }

        System.out.println("Total messages sent: " + messagesSent);
    }

    public static String generateRandomMessageID() {
        Random random = new Random();
        StringBuilder messageId = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characters.length());
            messageId.append(characters.charAt(randomIndex));
        }

        return messageId.toString();
    }

    public static boolean checkMessageID(String messageId) {
        if (messageId == null) return false;
        if (messageId.length() != 10) return false;
        return messageId.matches("[a-zA-Z0-9]{10}");
    }

    public static boolean checkRecipientCellNumber(String cellNumber) {
        if (cellNumber == null) return false;
        if (cellNumber.length() != 12) return false; // +27 + 9 digits = 12 total
        String regex = "^\\+27\\d{9}$";
        return cellNumber.matches(regex);
    }

    public static String createMessageHash(String messageId, String messageContent) {
        // Get first two characters of message ID
        String firstTwo = messageId.substring(0, 2);
        String messageNum = String.format("%02d", messagesSent + 1);
        String[] words = messageContent.trim().split("\\s+");
        String lastWord = words.length > 0
                ? words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase()
                : "";

        // Format: 00:01:TONIGHT
        return String.format("%s:%s:%s", firstTwo, messageNum, lastWord);
    }

    public static String SentMessage() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose what to do with this message:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                return "Send";
            case 2:
                return "Disregard";
            case 3:
                return "Store";
            default:
                return "Invalid";
        }
    }

    public static String printMessages() {
        if (sentMessagesList.isEmpty()) {
            return "No messages to display.";
        }

        StringBuilder result = new StringBuilder("Recently Sent Messages:\n");
        for (int i = 0; i < sentMessagesList.size(); i++) {
            result.append((i + 1)).append(". ").append(sentMessagesList.get(i)).append("\n");
        }
        return result.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public static String storeMessage(String message) {
        // Simple JSON format for message storage
        return String.format("{ \"timestamp\": \"%d\", \"message\": \"%s\" }",
                System.currentTimeMillis(), message.replace("\"", "\\\""));
    }

    public static String checkMessageLength(String msg) {
        if (msg.length() > 250) {
            int excess = msg.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        } else {
            return "Message ready to send.";
        }
    }

    public static String checkRecipientCell(String cell) {
        if (cell.startsWith("+27") && cell.length() == 12 && cell.substring(3).matches("\\d{9}")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an International code. Please correct the number and try again.";
        }
    }

    public static String createMessage(String id, String message) {
        String firstTwo = id.substring(0, 2);
        String messageNum = String.valueOf(totalMessages + 1);
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : firstWord;
        return String.format("%s:%s:%s%s", firstTwo, messageNum, firstWord, lastWord);
    }

    public static String messageSentAction(String action) {
        switch (action) {
            case "Send":
                return "Message successfully sent.";
            case "Disregard":
                return "Press 1 to delete message.";
            case "Store":
                return "Message successfully stored.";
            default:
                return "";
        }
    }

    // Additional method for backwards compatibility
    public static String searchMessageOptions() {
        return SentMessage();
    }

    public static void resetMessagesSent() {
        messagesSent = 0;
    }

     // Displays the longest message from sent messages
     // Required for "Display details for the longest message" rubric item

    public static void displayLongestMessage() {
        if (sentCount == 0) {
            System.out.println("No sent messages found!");
            return;
        }

        MessageEntry longestEntry = sentMessage[0];
        for (int i = 1; i < sentCount; i++) {
            if (sentMessage[i].getMessage().length() > longestEntry.getMessage().length()) {
                longestEntry = sentMessage[i];
            }
        }

        System.out.println("\n=== LONGEST MESSAGE DETAILS ===");
        System.out.println("Message ID: " + longestEntry.getMessageId());
        System.out.println("Recipient: " + longestEntry.getRecipient());
        System.out.println("Message Hash: " + longestEntry.getHash());
        System.out.println("Message Length: " + longestEntry.getMessage().length() + " characters");
        System.out.println("Message Content: " + longestEntry.getMessage());
        System.out.println("===============================");
    }


     // Searches for messages by recipient across all message types
     // Search Array for messages sent to recipient

    public static void searchByRecipient(String recipient) {
        System.out.println("\n=== MESSAGES FOR RECIPIENT: " + recipient + " ===");
        boolean found = false;
        int messageCount = 0;

        // Search sent messages
        System.out.println("\nSENT MESSAGES:");
        for (int i = 0; i < sentCount; i++) {
            if (sentMessage[i].getRecipient().equals(recipient)) {
                messageCount++;
                System.out.println("  " + messageCount + ". [SENT] ID: " + sentMessage[i].getMessageId()
                        + " | Hash: " + sentMessage[i].getHash() + " | Message: " + sentMessage[i].getMessage());
                found = true;
            }
        }

        // Search stored messages
        System.out.println("\nSTORED MESSAGES:");
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getRecipient().equals(recipient)) {
                messageCount++;
                System.out.println("  " + messageCount + ". [STORED] ID: " + storedMessages[i].getMessageId()
                        + " | Hash: " + storedMessages[i].getHash() + " | Message: " + storedMessages[i].getMessage());
                found = true;
            }
        }

        // Search disregarded messages
        System.out.println("\nDISREGARDED MESSAGES:");
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i].getRecipient().equals(recipient)) {
                messageCount++;
                System.out.println("  " + messageCount + ". [DISREGARDED] ID: " + disregardedMessages[i].getMessageId()
                        + " | Hash: " + disregardedMessages[i].getHash() + " | Message: " + disregardedMessages[i].getMessage());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for recipient: " + recipient);
        } else {
            System.out.println("\nTotal messages found: " + messageCount);
        }
        System.out.println("==============================================");
    }


     // Deletes a message by its hash from all arrays
     // Delete Message using message Hash

    public static void deleteByHash(String hash) {
        boolean found = false;

        // Remove from sent messages
        for (int i = 0; i < sentCount; i++) {
            if (sentMessage[i].getHash().equals(hash)) {
                System.out.println("Deleting SENT message: " + sentMessage[i].getMessage());

                // Shift array elements left to remove the item
                for (int j = i; j < sentCount - 1; j++) {
                    sentMessage[j] = sentMessage[j + 1];
                }
                sentCount--;
                found = true;
                break;
            }
        }

        // Remove from message hashes array
        for (int i = 0; i < hashCount; i++) {
            if (messageHashes[i].equals(hash)) {
                // Shift array elements left to remove the hash
                for (int j = i; j < hashCount - 1; j++) {
                    messageHashes[j] = messageHashes[j + 1];
                }
                hashCount--;
                break;
            }
        }

        // Remove from stored messages
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getHash().equals(hash)) {
                System.out.println("Deleting STORED message: " + storedMessages[i].getMessage());

                // Shift array elements left
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                }
                storedCount--;
                found = true;
                break;
            }
        }

        // Remove from disregarded messages
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i].getHash().equals(hash)) {
                System.out.println("Deleting DISREGARDED message: " + disregardedMessages[i].getMessage());

                // Shift array elements left
                for (int j = i; j < disregardedCount - 1; j++) {
                    disregardedMessages[j] = disregardedMessages[j + 1];
                }
                disregardedCount--;
                found = true;
                break;
            }
        }

        // Remove from HashMaps
        messageHashToEntryMap.remove(hash);

        if (found) {
            System.out.println("Message with hash '" + hash + "' successfully deleted.");
        } else {
            System.out.println("No message found with hash: " + hash);
        }
    }


     // Searches for a message by ID
     // Enhanced version for better functionality

    public static void searchById(String id) {
        System.out.println("\n=== SEARCHING FOR MESSAGE ID: " + id + " ===");
        boolean found = false;

        // Search sent messages
        for (int i = 0; i < sentCount; i++) {
            if (sentMessage[i].getMessageId().equals(id)) {
                System.out.println("FOUND in SENT messages:");
                System.out.println("  Message: " + sentMessage[i].getMessage());
                System.out.println("  Recipient: " + sentMessage[i].getRecipient());
                System.out.println("  Hash: " + sentMessage[i].getHash());
                found = true;
                break;
            }
        }

        // Search stored messages if not found in sent
        if (!found) {
            for (int i = 0; i < storedCount; i++) {
                if (storedMessages[i].getMessageId().equals(id)) {
                    System.out.println("FOUND in STORED messages:");
                    System.out.println("  Message: " + storedMessages[i].getMessage());
                    System.out.println("  Recipient: " + storedMessages[i].getRecipient());
                    System.out.println("  Hash: " + storedMessages[i].getHash());
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("Message with ID '" + id + "' not found!");
        }
        System.out.println("========================================");
    }


     // Writes stored messages to JSON file
     // Required for JSON file operations

    public static void writeStoredMessagesToFile() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < storedCount; i++) {
                JSONObject messageObj = new JSONObject();
                messageObj.put("MessageID", storedMessages[i].getMessageId());
                messageObj.put("Message Hash", storedMessages[i].getHash());
                messageObj.put("Recipient", storedMessages[i].getRecipient());
                messageObj.put("Message", storedMessages[i].getMessage());
                messageObj.put("timestamp", System.currentTimeMillis());

                jsonArray.put(messageObj);
            }

            // Write to file
            Files.write(Paths.get("stored_messages.json"), jsonArray.toString(2).getBytes());
            System.out.println("Stored messages successfully written to file: stored_messages.json");

        } catch (Exception e) {
            System.out.println("Error writing stored messages to file: " + e.getMessage());
            e.printStackTrace();
        }
    }


     // Reads stored messages from JSON file

    public static void readStoredMessagesFromFile() {
        try {
            // Check if file exists
            if (!Files.exists(Paths.get("stored_messages.json"))) {
                System.out.println("No stored messages file found. Creating sample file...");
                createSampleStoredMessagesFile();
                return;
            }

            // Read file content
            String content = new String(Files.readAllBytes(Paths.get("stored_messages.json")));
            JSONArray jsonArray = new JSONArray(content);

            // Clear existing stored messages
            storedCount = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String msgId = obj.optString("MessageID", "UNKNOWN");
                String hash = obj.optString("Message Hash", "UNKNOWN");
                String recipient = obj.optString("Recipient", "UNKNOWN");
                String messageContent = obj.optString("Message", "UNKNOWN");

                // Add to stored messages array
                MessageEntry entry = new MessageEntry(msgId, recipient, messageContent, hash, "Stored");
                if (storedCount < storedMessages.length) {
                    storedMessages[storedCount++] = entry;
                    storedMessagesList.add(entry);
                }
            }

            System.out.println("Stored messages loaded successfully! Loaded " + storedCount + " messages.");

            // Display loaded messages
            if (storedCount > 0) {
                System.out.println("\nLoaded Messages:");
                for (int i = 0; i < storedCount; i++) {
                    System.out.println("  " + (i+1) + ". ID: " + storedMessages[i].getMessageId() +
                            " | To: " + storedMessages[i].getRecipient() +
                            " | Message: " + storedMessages[i].getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading stored messages: " + e.getMessage());
            e.printStackTrace();
        }
    }


     //Creates a sample JSON file for testing

    private static void createSampleStoredMessagesFile() {
        try {
            JSONArray jsonArray = new JSONArray();

            // Sample message 1
            JSONObject msg1 = new JSONObject();
            msg1.put("MessageID", "SAMPLE001");
            msg1.put("Message Hash", "SA:01:HELLO");
            msg1.put("Recipient", "+27823456789");
            msg1.put("Message", "Hello, this is a stored message");
            msg1.put("timestamp", System.currentTimeMillis());
            jsonArray.put(msg1);

            // Sample message 2
            JSONObject msg2 = new JSONObject();
            msg2.put("MessageID", "SAMPLE002");
            msg2.put("Message Hash", "SA:02:LATER");
            msg2.put("Recipient", "+27834567890");
            msg2.put("Message", "This message will be sent later");
            msg2.put("timestamp", System.currentTimeMillis());
            jsonArray.put(msg2);

            Files.write(Paths.get("stored_messages.json"), jsonArray.toString(2).getBytes());
            System.out.println("Sample stored messages file created: stored_messages.json");

        } catch (Exception e) {
            System.out.println("Error creating sample file: " + e.getMessage());
        }
    }


     // Shows full report of all sent messages
     // Display Message Report

    public static void showFullReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("               FULL SENT MESSAGES REPORT");
        System.out.println("=".repeat(60));

        if (sentCount == 0) {
            System.out.println("No sent messages to display.");
            System.out.println("=".repeat(60));
            return;
        }

        System.out.printf("%-12s %-15s %-15s %-30s%n", "MESSAGE ID", "RECIPIENT", "MESSAGE HASH", "MESSAGE");
        System.out.println("-".repeat(60));

        for (int i = 0; i < sentCount; i++) {
            MessageEntry msg = sentMessage[i];
            String truncatedMessage = msg.getMessage().length() > 28 ?
                    msg.getMessage().substring(0, 25) + "..." :
                    msg.getMessage();
            System.out.printf("%-12s %-15s %-15s %-30s%n",
                    msg.getMessageId(),
                    msg.getRecipient(),
                    msg.getHash(),
                    truncatedMessage);
        }

        System.out.println("-".repeat(60));
        System.out.println("SUMMARY:");
        System.out.println("  Total Messages Sent: " + sentCount);
        System.out.println("  Total Messages Stored: " + storedCount);
        System.out.println("  Total Messages Disregarded: " + disregardedCount);
        System.out.println("  Overall Total Messages: " + (sentCount + storedCount + disregardedCount));
        System.out.println("=".repeat(60));
    }


     // Shows recently sent messages (last 5)
     // Enhanced version with better formatting

    public static void showRecentlySent() {
        System.out.println("\n=== RECENTLY SENT MESSAGES ===");

        if (sentCount == 0) {
            System.out.println("No messages have been sent yet.");
            return;
        }

        int startIndex = Math.max(0, sentCount - 5);
        System.out.println("Showing last " + (sentCount - startIndex) + " sent messages:");

        for (int i = startIndex; i < sentCount; i++) {
            System.out.println((i - startIndex + 1) + ". To: " + sentMessage[i].getRecipient() +
                    " | ID: " + sentMessage[i].getMessageId() +
                    " | Message: " + sentMessage[i].getMessage());
        }
        System.out.println("===============================");
    }

    // Helper method to extract values from stored message strings
    private static String extractValue(String text, String prefix) {
        int startIndex = text.indexOf(prefix);
        if (startIndex == -1) return "";

        startIndex += prefix.length();
        int endIndex = text.indexOf(",", startIndex);

        if (endIndex == -1) {
            endIndex = text.length();
        }

        return text.substring(startIndex, endIndex).trim();
    }
}