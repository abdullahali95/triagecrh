//package com.health.application.views.triagecrh;
//
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.HasStyle;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.checkbox.Checkbox;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.component.timepicker.TimePicker;
//
//public class AdmissionEditor extends FormLayout {
//
//    private Component[] fields;
//
//    public AdmissionEditor(DatePicker date, TimePicker time, Checkbox clerked, Checkbox postTaken,
//                                TextField presentingComplaint, TextField news,
//                           Button save, Button cancel, Button delete) {
//        date = new DatePicker("Date");
//        time = new TimePicker("Time");
//        clerked = new Checkbox("Clerked");
//        clerked.getStyle().set("padding-top", "var(--lumo-space-m)");
//        postTaken = new Checkbox("Post Taken");
//        postTaken.getStyle().set("padding-top", "var(--lumo-space-m)");
//        presentingComplaint = new TextField("Presenting Complaint");
//        news = new TextField("News");
//
//        fields = new Component[]{date, time, clerked, postTaken, presentingComplaint,
//                news};
//        this.add(fields);
//        for (Component field : fields) {
//            ((HasStyle) field).addClassName("full-width");
//        }
//        createButtonLayout(save, cancel, delete);
//
//    }
//
//    private void createButtonLayout(Button save, Button cancel, Button delete) {
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.setId("button-layout");
//        buttonLayout.setWidthFull();
//        buttonLayout.setSpacing(true);
//
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
//        buttonLayout.add(save, cancel);
//        this.add(buttonLayout);
//    }
//
//
//}
