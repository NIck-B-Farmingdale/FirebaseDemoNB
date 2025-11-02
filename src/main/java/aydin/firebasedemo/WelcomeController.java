package aydin.firebasedemo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class WelcomeController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button signInButton;

    @FXML
    private Label statusLabel;

    @FXML
    void registerButtonClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Email and password cannot be empty.");
            return;
        }

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(password)
                .setDisabled(false);

        try {
            UserRecord userRecord = DemoApp.fauth.createUser(request);
            System.out.println("Successfully created new user with Firebase Uid: " + userRecord.getUid());
            statusLabel.setText("Registration successful! You can now sign in.");
            emailField.clear();
            passwordField.clear();

        } catch (FirebaseAuthException ex) {
            System.out.println("Error creating a new user in firebase");
            ex.printStackTrace();
            statusLabel.setText("Registration failed: " + ex.getMessage());
        }
    }

    @FXML
    void signInButtonClicked(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText(); // Note: Admin SDK can't verify password directly

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Email and password cannot be empty.");
            return;
        }

        try {
            // With Admin SDK, we can only look up a user, not truly "sign in" or verify a password.
            // This lookup confirms the user exists.
            UserRecord userRecord = DemoApp.fauth.getUserByEmail(email);
            System.out.println("Successfully fetched user data: " + userRecord.getUid());

            // If user lookup is successful, proceed to the main app screen.
            // This does NOT verify the password.
            // A real client app would use a Client SDK or REST API for password verification.
            DemoApp.setRoot("primary");

        } catch (FirebaseAuthException ex) {
            System.out.println("Error fetching user data: " + ex.getMessage());
            statusLabel.setText("Sign-in failed: User not found or error.");
        } catch (IOException ex) {
            System.out.println("Failed to load primary view.");
            statusLabel.setText("Error loading application.");
        }
    }
}