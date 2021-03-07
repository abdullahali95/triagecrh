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
import java.util.function.DoubleToIntFunction;

public class AdmissionForm extends FormLayout {

    private Admission admission;

    DatePicker dateAdmission = new DatePicker("Date of Admission");
    TimePicker timeAdmission = new TimePicker("Time of Admission");
    ComboBox<Ward> ward = new ComboBox<>("Ward");
    TextField pc = new TextField("Presenting Complaint");
    IntegerField news = new IntegerField("NEWS Score");

    Checkbox clerked = new Checkbox("Clerked");
    Checkbox postTaken = new Checkbox("Post-Taken");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Admission> admissionBinder = new BeanValidationBinder<>(Admission.class);


    public AdmissionForm(List<Ward> wards) {
        addClassName("admission-form");

        admissionBinder.bindInstanceFields(this);
        ward.setItems(wards);
        ward.setItemLabelGenerator(Ward::getWardName);
        add(dateAdmission, timeAdmission, ward, pc, news, clerked, postTaken,
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
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, admission)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));


        admissionBinder.addStatusChangeListener(e -> save.setEnabled(admissionBinder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setAdmission(Admission admission) {
        this.admission = admission;
        admissionBinder.readBean(admission);
    }

    private void validateAndSave() {
        try {
            admissionBinder.writeBean(admission);
            fireEvent(new SaveEvent(this, admission));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    // Events
    public static abstract class AdmissionFormEvent extends ComponentEvent<AdmissionForm> {
        private Admission admission;

        protected AdmissionFormEvent(AdmissionForm source, Admission admission) {
            super(source, false);
            this.admission = admission;
        }

        public Admission getAdmission() {
            return admission;
        }
    }

    public static class SaveEvent extends AdmissionFormEvent {
        SaveEvent(AdmissionForm source, Admission admission) {
            super(source, admission);
        }
    }

    public static class DeleteEvent extends AdmissionFormEvent {
        DeleteEvent(AdmissionForm source, Admission admission) {
            super(source, admission);
        }

    }

    public static class CloseEvent extends AdmissionFormEvent {
        CloseEvent(AdmissionForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
