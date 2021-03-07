package com.health.application.views.main;

import com.health.application.data.entity.Admission;
import com.health.application.data.entity.Patient;
import com.health.application.data.entity.Ward;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class PatientForm extends FormLayout {
    private Patient patient;
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    DatePicker dob = new DatePicker("Date of Birth");
    IntegerField hospId = new IntegerField("Hosp No:");
    NumberField nhsId = new NumberField("NHS No:");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Patient> patientBinder = new BeanValidationBinder<>(Patient.class);


    public PatientForm() {
        addClassName("patient-form");
        patientBinder.bindInstanceFields(this);
        add(firstName, lastName, dob, hospId, nhsId,
                createButtonsLayout());
        bindFields();
    }

    private void bindFields() {

    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new PatientForm.DeleteEvent(this, patient)));
        close.addClickListener(event -> fireEvent(new PatientForm.CloseEvent(this)));


        patientBinder.addStatusChangeListener(e -> save.setEnabled(patientBinder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        patientBinder.readBean(patient);
    }

    private void validateAndSave() {
        try {
            patientBinder.writeBean(patient);
            fireEvent(new PatientForm.SaveEvent(this, patient));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }



    // Events
    public static abstract class PatientFormEvent extends ComponentEvent<PatientForm> {
        private Patient patient;

        protected PatientFormEvent(PatientForm source, Patient patient) {
            super(source, false);
            this.patient = patient;
        }

        public Patient getPatient() {
            return patient;
        }
    }

    public static class SaveEvent extends PatientForm.PatientFormEvent {
        SaveEvent(PatientForm source, Patient patient) {
            super(source, patient);
        }
    }

    public static class DeleteEvent extends PatientForm.PatientFormEvent {
        DeleteEvent(PatientForm source, Patient patient) {
            super(source, patient);
        }

    }

    public static class CloseEvent extends PatientForm.PatientFormEvent {
        CloseEvent(PatientForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
