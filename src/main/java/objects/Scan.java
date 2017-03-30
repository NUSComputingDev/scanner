package objects;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Scan {


    // Display controls
    private ObjectProperty<Color> profileLabelColour;
    private ObjectProperty<Color> highlightColour;
    private BooleanProperty isHighlighted;
    private BooleanProperty isSelected;
    private IntegerProperty indexNumber;


    // Properties
//    private StringProperty fullName;
    private StringProperty matricNumber;
    private StringProperty dateTimeStamp;
    private StringProperty detailsField;
    private StringProperty accessCode;


    // Optional
    private ObjectProperty<Image> profilePicture;
    private StringProperty faculty;


    public Scan(/*String fullName,*/ String matricNumber, String dateTimeStamp, String detailsField, String accessCode) {

//        this.fullName = new SimpleStringProperty(fullName);
        this.matricNumber = new SimpleStringProperty(matricNumber);
        this.dateTimeStamp = new SimpleStringProperty(dateTimeStamp);
        this.detailsField = new SimpleStringProperty(detailsField);
        this.accessCode = new SimpleStringProperty(accessCode);

    }

    public Scan(String matricNumber, String accessCode) {
        this.matricNumber = new SimpleStringProperty(matricNumber);
        this.accessCode = new SimpleStringProperty(accessCode);
    }


//    public StringProperty getFullNameProperty() {
//        return this.fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName.set(fullName);
//    }
//
//    public String getFullName() {
//        return this.fullName.get();
//    }


    public StringProperty getMatricNumberProperty() {
        return this.matricNumber;
    }

    public void setMatricNumber(String matricNumber) {
        this.matricNumber.set(matricNumber);
    }

    public String getMatricNumber() {
        return this.matricNumber.get();
    }


    public StringProperty getDateTimeStampProperty() {
        return this.dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp.set(dateTimeStamp);
    }

    public String getDateTimeStamp() {
        return this.dateTimeStamp.get();
    }


    public StringProperty getDetailsFieldProperty() {
        return this.detailsField;
    }

    public void setDetailsField(String detailsField) {
        this.detailsField.set(detailsField);
    }

    public String getDetailsField() {
        return this.detailsField.get();
    }


    public StringProperty getAccessCodeProperty() {
        return this.accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode.set(accessCode);
    }

    public String getAccessCode() {
        return this.accessCode.get();
    }


    public ObjectProperty<Color> getProfileLabelColourProperty() {
        return this.profileLabelColour;
    }

    public void setProfileLabelColour(Color profileLabelColour) {
        this.profileLabelColour.set(profileLabelColour);
    }

    public Color getProfileLabelColor() {
        return this.profileLabelColour.get();
    }


    public ObjectProperty<Color> getHighlightColourProperty() {
        return this.profileLabelColour;
    }

    public void setHighlightColour(Color highlightColour) {
        this.highlightColour.set(highlightColour);
    }

    public Color getHighLightColor() {
        return this.highlightColour.get();
    }


    public BooleanProperty getIsHighlightedProperty() {
        return this.isHighlighted;
    }

    public void setIsHighlighted(Boolean isHighlighted) {
        this.isHighlighted.set(isHighlighted);
    }

    public Boolean getIsHighlighted() {
        return this.isHighlighted.get();
    }


    public BooleanProperty getIsSelectedProperty() {
        return this.isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected.set(isSelected);
    }

    public Boolean getIsSelected() {
        return this.isSelected.get();
    }


    public IntegerProperty getIndexNumberProperty() {
        return this.indexNumber;
    }

    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber.set(indexNumber);
    }

    public Integer getIndexNumber() {
        return this.indexNumber.get();
    }


}