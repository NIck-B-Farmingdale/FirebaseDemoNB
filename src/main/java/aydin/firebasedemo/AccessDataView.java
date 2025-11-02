package aydin.firebasedemo;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AccessDataView {

    private final StringProperty personName = new SimpleStringProperty();
    private final StringProperty personAge = new SimpleStringProperty(); // Added age
    private final StringProperty personPhone = new SimpleStringProperty(); // Added phone
    private final ReadOnlyBooleanWrapper writePossible = new ReadOnlyBooleanWrapper();

    public AccessDataView() {
        // Write button is possible only if all three fields are not empty
        writePossible.bind(
                personName.isNotEmpty()
                        .and(personAge.isNotEmpty())
                        .and(personPhone.isNotEmpty())
        );
    }

    public StringProperty personNameProperty() {
        return personName;
    }

    public StringProperty personAgeProperty() {
        return personAge;
    }

    public StringProperty personPhoneProperty() {
        return personPhone;
    }

    public ReadOnlyBooleanProperty isWritePossibleProperty() {
        return writePossible.getReadOnlyProperty();
    }
}
