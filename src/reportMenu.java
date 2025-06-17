import java.util.Scanner;

public class reportMenu {
    private Scanner scanner;

    public reportMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\nReport Menu:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show Recently Sent Messages");
            System.out.println("3) Show Full Sent Messages Report");
            System.out.println("4) Display Longest Sent Message");
            System.out.println("5) Search for Message by ID");
            System.out.println("6) Search for Messages by Recipient");
            System.out.println("7) Delete Message by Hash");
            System.out.println("8) Read Stored Messages From File");
            System.out.println("9) Quit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (option) {
                case 1:
                    Message.sendMessage();
                    break;
                case 2:
                    Message.showRecentlySent();
                    break;
                case 3:
                    Message.showFullReport();
                    break;
                case 4:
                    Message.displayLongestMessage();
                    break;
                case 5:
                    System.out.print("Enter Message ID: ");
                    Message.searchById(scanner.nextLine());
                    break;
                case 6:
                    System.out.print("Enter Recipient: ");
                    Message.searchByRecipient(scanner.nextLine());
                    break;
                case 7:
                    System.out.print("Enter Message Hash: ");
                    Message.deleteByHash(scanner.nextLine());
                    break;
                case 8:
                    Message.readStoredMessagesFromFile();
                    break;
                case 9:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}