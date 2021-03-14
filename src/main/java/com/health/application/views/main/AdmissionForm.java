package com.health.application.views.main;

import com.health.application.data.entity.Admission;
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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class AdmissionForm extends FormLayout {

    private Admission admission;
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    DatePicker dob = new DatePicker("Date of Birth");
    public IntegerField hospId = new IntegerField("Hosp No:");
    NumberField nhsId = new NumberField("NHS No:");

    DatePicker dateAdmission = new DatePicker("Date of Admission");
    TimePicker timeAdmission = new TimePicker("Time of Admission");
    ComboBox<Ward> ward = new ComboBox<>("Ward");
    IntegerField frailtyScore = new IntegerField("Frailty Score");
    TextField pc = new TextField("Presenting Complaint");
    IntegerField news = new IntegerField("NEWS Score");

    ComboBox<Admission.ClerkedBy> clerkedBy = new ComboBox<>("Clerked By");
    Checkbox postTaken = new Checkbox("Post-Taken");
    Checkbox urgentClerk = new Checkbox("Clerk Urgently (Nurses only)");
    Checkbox dischargeable = new Checkbox("?Discharge");


    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Admission> admissionBinder = new BeanValidationBinder<>(Admission.class);



    public AdmissionForm(List<Ward> wards) {
        addClassName("admission-form");

        bindFields();
        ward.setItems(wards);
        ward.setItemLabelGenerator(Ward::getWardName);
        admissionBinder.bindInstanceFields(this);

        H2 title = new H2("Add/Edit Patient");
        title.setId("form-h2");
        add(title);
        add(hospId, firstName, lastName, dob, nhsId,dateAdmission,
                timeAdmission, ward, news, frailtyScore, pc, urgentClerk,
                dischargeable, clerkedBy, postTaken,
        createButtonsLayout());
        bindFields();
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

    private void bindFields() {
        news.setWidth("5em");

        admissionBinder
                .forField(clerkedBy)
                .bind(Admission::getClerkedBy, Admission::setClerkedBy);
        clerkedBy.setItems(Admission.ClerkedBy.values());


        admissionBinder.forField(dateAdmission)
                .withValidator(new DateRangeValidator(
                        "Please enter a valid Date of Admission",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.now()))
                .bind(Admission::getDate, Admission::setDate);
            dateAdmission.setLocale(Locale.UK);


        admissionBinder
                .forField(pc)
                .withValidator(string -> string.length() < 40, "Too long")
                .bind(Admission::getPresentingComplaint, Admission::setPresentingComplaint);
        admissionBinder
                .forField(timeAdmission)
                .asRequired("Please enter a valid time of admission")
                .bind(Admission::getTime, Admission::setTime);

        news.setValue(0);
        admissionBinder
                .forField(news)
                .withValidator(news ->
                        (news == null || (news >=0 && news < 21)),
                        "Please enter a valid NEWS score")
                .bind(Admission::getNews, Admission::setNews);

        admissionBinder.forField(nhsId)
                .bind(
                admission -> {
                    if (admission.getPatient() != null) {
                        return admission.getPatient().getNhsId();
                    }
                    return null;
                },
                (admission, nhsId) -> {
                    if (admission.getPatient() != null) {
                        admission.getPatient().setNhsId(nhsId);
                    }
                });

        admissionBinder.forField(hospId)
                .withValidator(
                        hospId -> (hospId == null || (hospId > 10000 && hospId < 9999999)),
                        "Please enter a valid Hospital ID"
                )
                .asRequired()
                .bind(
                admission -> {
                    if (admission.getPatient() != null) {
                        return admission.getPatient().getHospId();
                    }
                    return null;
                },
                (admission, hospId) -> {
                    if (admission.getPatient() != null) {
                        admission.getPatient().setHospId(hospId);
                    }
                });
        hospId.setMin(10000);

        admissionBinder.forField(dob)
                .withValidator(new DateRangeValidator(
                        "Please enter a valid Date of Birth",
                        LocalDate.of(1900, 1, 1),
                        LocalDate.now()))
                .bind(
                admission -> {
                    if (admission.getPatient() != null) {
                        return admission.getPatient().getDob();
                    }
                    return null;
                },
                (admission, dob) -> {
                    if (admission.getPatient() != null) {
                        admission.getPatient().setDob(dob);
                    }
                });
        dob.setLocale(Locale.UK);

        admissionBinder.forField(lastName).bind(
                admission -> {
                    if (admission.getPatient() != null) {
                        return admission.getPatient().getLastName();
                    }
                    return "";
                },
                (admission, lastName) -> {
                    if (admission.getPatient() != null) {
                        admission.getPatient().setLastName(lastName);
                    }
                });

        admissionBinder.forField(firstName).bind(
                admission -> {
                    if (admission.getPatient() != null) {
                        return admission.getPatient().getFirstName();
                    }
                    return "";
                },
                (admission, firstName) -> {
                    if (admission.getPatient() != null) {
                        admission.getPatient().setFirstName(firstName);
                    }
                });
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
