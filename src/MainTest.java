import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MainTest {


    @Test
    public void isCheckUserName() {
        assertTrue(Main.isCheckUserName("kyl_1"));//Correct
        assertFalse(Main.isCheckUserName("kyle!!!!!!!"));//no underscore
        assertFalse(Main.isCheckUserName("kyle"));
        assertFalse(Main.isCheckUserName("kyl_"));//too short
        assertFalse(Main.isCheckUserName("ky_"));

    }

    @Test
    public void isCheckPasswordComplexity() {
        assertTrue(Main.isCheckPasswordComplexity("Ch&&sec@ke99"));
        assertFalse(Main.isCheckPasswordComplexity("password")); // no capital, no special char
        assertFalse(Main.isCheckPasswordComplexity("Passw0rd")); // no special character
        assertFalse(Main.isCheckPasswordComplexity("Pass!"));    // too short
    }

    @Test
    public void isCheckCellPhoneNumber() {
        assertTrue(Main.isCheckCellPhoneNumber("+27838968976"));
        assertFalse(Main.isCheckCellPhoneNumber("+2708966553"));    //is too short
        Assertions.assertFalse(Main.isCheckCellPhoneNumber("+2783896897699")); // too long
    }

    @Test
    public void testRegisterUser() {
        assertEquals("The two conditions have been met, and the user has been register successfully.",
                Main.registerUser("kyl_1", "Ch&&sec@ke99"));
        assertEquals("The username is incorrectly formatted.",
                Main.registerUser("invalid", "Ch&&sec@ke99"));
        assertEquals("The password does not meet the complexity requirements.",
                Main.registerUser("kyl_1", "invalidpassword"));
    }
    @Test
    public void testLoginUser() {

        Main.users.put("kyl_1", "Ch&&sec@ke99");

        assertTrue(Main.loginUser("kyl_1", "Ch&&sec@ke99"));
        assertFalse(Main.loginUser("kyl_1", "wrongpassword"));
        assertFalse(Main.loginUser("nonexistent_user", "Ch&&sec@ke99"));
    }

    @Test
    public void testReturnLoginStatus() {
        assertEquals("A successful login", Main.returnLoginStatus(true));
        assertEquals("A failed login", Main.returnLoginStatus(false));
    }

}


