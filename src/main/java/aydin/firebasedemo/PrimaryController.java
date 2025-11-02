package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML
    private TextField ageTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField; // Added phone field

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Button readButton;

    // Removed registerButton FXML property

    @FXML
    private Button switchSecondaryViewButton;

    @FXML
    private Button writeButton;

    private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;

    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    @FXML
    void initialize() {
        AccessDataView accessDataViewModel = new AccessDataView();
        nameTextField.textProperty().bindBidirectional(accessDataViewModel.personNameProperty());
        ageTextField.textProperty().bindBidirectional(accessDataViewModel.personAgeProperty());
        phoneTextField.textProperty().bindBidirectional(accessDataViewModel.personPhoneProperty());
        writeButton.disableProperty().bind(accessDataViewModel.isWritePossibleProperty().not());
    }

    @FXML
    void readButtonClicked(ActionEvent event) {
        readFirebase();
    }

    // Removed registerButtonClicked method

    @FXML
    void writeButtonClicked(ActionEvent event) {
        addData();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        DemoApp.setRoot("secondary");
    }

    public boolean readFirebase() {
        key = false;
        outputTextArea.clear(); // Clear previous results
        ApiFuture<QuerySnapshot> future = DemoApp.fstore.collection("Persons").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                System.out.println("Getting (reading) data from firebase database....");
                listOfUsers.clear();
                for (QueryDocumentSnapshot document : documents) {
                    String name = String.valueOf(document.getData().get("Name"));
                    String age = String.valueOf(document.getData().get("Age"));
                    String phone = document.getData().get("Phone") != null ? String.valueOf(document.getData().get("Phone")) : "N/A";

                    outputTextArea.setText(outputTextArea.getText() + "Name: " + name + ", Age: " + age + ", Phone: " + phone + " \n ");
                    System.out.println(document.getId() + " => " + name);

                    person = new Person(name, Integer.parseInt(age), phone);
                    listOfUsers.add(person);
                }
            } else {
                System.out.println("No data");
                outputTextArea.setText("No data found in database.");
            }
            key = true;

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    // Removed registerUser method

    public void addData() {
        DocumentReference docRef = DemoApp.fstore.collection("Persons").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameTextField.getText());
        data.put("Age", Integer.parseInt(ageTextField.getText()));
        data.put("Phone", phoneTextField.getText()); // Add phone to data map

        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);

        // Clear fields after writing
        nameTextField.clear();
        ageTextField.clear();
        phoneTextField.clear();
    }
}
