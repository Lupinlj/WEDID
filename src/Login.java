import java.util.Scanner;

public class Login {
    public static void printAMessage(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to QuickChat");
            System.out.println("Please choose an option:");
            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("Enter Message ID (10 alphanumeric characters): ");
                String id = scanner.nextLine();
                if (!Message.checkMessageID(id)) {
                    System.out.println("Invalid Message ID");


                    continue;
                }
                System.out.println("Enter recipient cell number (+27 followed by 9 digits):");
                String cellNumber = scanner.nextLine();
                if (!Message.checkRecipientCellNumber(cellNumber)) {
                    System.out.println("Invalid cell number");
                    continue;
                }
                System.out.println("Enter your message:");
                String content = scanner.nextLine();
                String formattedMessage = String.format("ID:%s | To:%s | Message:%s", id, cellNumber, content);

                System.out.println("Message sent successfully");


            } else if (choice == 2) {
                System.out.println("Coming Soon. ");

            } else if (choice == 3) {
                System.out.println("Exiting QuickChat. Bye!!");
                break;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
    }
}