import javax.swing.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static Map<String, String> users = new HashMap<>();



    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        boolean isRegistered = false;
        boolean isLoginIn = false;

        String registeredUsername = "";
        String registeredPassword = "";



        while (true) {// Loop starts here
            if (!isRegistered) {

                System.out.println("Please choose the following:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                int selection = scanner.nextInt();
                scanner.nextLine();

                if (selection == 1) {
                    // Start registration process
                    String userName;
                    do {
                        System.out.println("Please enter username (Must contain an underscore and be more than five characters)");
                        userName = scanner.nextLine();

                        if (!isCheckUserName(userName)) {
                            System.out.println("Username is not correctly formatted. Please ensure it contains an underscore and is longer than 5 characters.");
                        } else {
                            System.out.println("Username successfully captured");
                            break;
                        }
                    } while (true);

                    String password;
                    do {
                        System.out.println("Please enter password (At least 8 characters, with a capital letter, number, and special character):");
                        password = scanner.nextLine();

                        if (!isCheckPasswordComplexity(password)) {
                            System.out.println("Password is not correctly formatted. Please check the requirements.");
                        } else {
                            System.out.println("Password successfully captured");
                            break;
                        }
                    } while (true);


                    String phoneNumber;
                    do {
                        System.out.println("Please enter your cell phone number with international country code:");
                        phoneNumber = scanner.nextLine();

                        if (!isCheckCellPhoneNumber(phoneNumber)) {
                            System.out.println("Cell phone number incorrectly formatted.");
                        } else {
                            System.out.println("Cell phone number successfully added.");
                            break;
                        }
                    } while (true);

                    //will store user input
                    users.put(userName, password);
                    registeredUsername = userName;
                    registeredPassword = password;
                    isRegistered = true;

                    //Registration Message
                    System.out.println("Registration completed! Returning to menu...\n");

                } else if (selection == 2) {
                    System.out.println("Please enter your username");
                    String userName = scanner.nextLine();
                    System.out.println("Please enter your password");
                    String password = scanner.nextLine();

                    if (loginUser(userName, password)) {
                        isLoginIn = true;
                        System.out.println("Login successful!");
                        System.out.println("Welcome" + userName + "!");
                    } else {
                        System.out.println("Invalid username or password");
                    }
                } else if (selection == 3) {
                    System.out.println("Exiting program. Bye!!");
                    scanner.close();
                    break;
                } else {
                    System.out.println("Invalid choice. Please select 1, 2, or 3. ");

                }
            } else {
                //User has completed registration

                while (true) {// Loop starts here
                    if (!isLoginIn)
                        System.out.println("Please choose the following:");
                    System.out.println("2. Login");
                    System.out.println("3. Exit");

                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 2) {
                        System.out.println("Login");
                        System.out.println("Please enter your username:");
                        String username = scanner.nextLine();

                        System.out.println("Please enter your password:");
                        String password = scanner.nextLine();

                        boolean loginSuccess = loginUser(username, password);
                        String loginMessage = returnLoginStatus(loginSuccess);
                        System.out.println(loginMessage);
                        if (loginSuccess) {
                            Message.message(args);

                            // Start HERE to add code after successful login
                            break;
                        } else {
                            System.out.println("Login failed. Please try again");
                        }
                    } else if (choice == 3) {
                        System.out.println("Exiting program. Bye!!");
                        break;
                    } else {
                        System.out.println("Invalid choice. Please select 2 or 3.");
                    }
                }

            }
        }
    }
    public static boolean isCheckUserName(String name) {
        // Change to >= 5 characters
        return name != null && name.length() > 5 && name.contains("_");

    }

    public static boolean isCheckPasswordComplexity(String password) {
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        if (password.length() < 8) return false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) hasUpper = true;
            if (Character.isDigit(ch)) hasDigit = true;
            if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
        }

        return hasUpper && hasDigit && hasSpecial;
    }

    public static boolean isCheckCellPhoneNumber(String phoneNumber) {
        String regex = "^\\+27\\d{9}$";
        return phoneNumber.matches(regex);
    }

    public static String registerUser(String username, String password) {
        if (!isCheckUserName(username)) {
            return "The username is incorrectly formatted.";
        }
        if (!isCheckPasswordComplexity(password)) {
            return "The password does not meet the complexity requirements.";
        }
        return "The two conditions have been met, and the user has been register successfully.";
    }

    public static boolean loginUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "A successful login";
        } else {
            return "A failed login";
        }


    }

}