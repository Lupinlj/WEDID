import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

public class Message {
    public static HashMap<String, String> messageMap = new HashMap<>();
    public static List<String> sentMessagesList = new ArrayList<>();
    public static int totalMessages = 0;
    private static int messageLimit = 0;
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
            System.out.println("3. Quit");


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
                    System.out.println("You've reached your message limit of "+ messageLimit + "message");

                }
            } else if (choice == 2) {
                System.out.println("Coming Soon.");
            } else if (choice == 3) {
                System.out.println("Exiting QuickChat. Bye!!");
                break;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");

            }
        }
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

        // Increment message count

        totalMessages = messagesSent;

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
                System.out.println("Message sent successfully!");

                String printMessage= "MessageID: " + messageId
                        + "\nMessage Hash: " + messageHash
                        + "\nRecipient: " + cellNumber
                        + "\nMessage: " + messageContent;
                JOptionPane.showMessageDialog(null, printMessage, "Message Sent Successfully", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Disregard":
                System.out.println("Message discarded.");
                messagesSent--; // Don't count discarded messages
                totalMessages--;
                break;
            case "Store":
                String jsonMessage = storeMessage(fullMessage);
                System.out.println("Message stored: " + jsonMessage);
                break;

            default:
                System.out.println("Invalid action.");


        }

        System.out.println("Total messages sent: " + totalMessages);
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
        String messageNum = String.format("%02d", messagesSent);
        String[] words = messageContent.trim().split("\\s+");
        String lastWord = words.length > 0
                ? words[words.length -  1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase()
                : "";

        // Format: 00:01:TONIGHT
        messagesSent++;
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

}