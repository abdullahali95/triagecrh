//package com.health.application.views.triagecrh;
//
//import java.util.Optional;
//
//import com.health.application.data.entity.Admission;
//import com.health.application.data.service.AdmissionService;
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.HasStyle;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.dependency.CssImport;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.grid.GridVariant;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.html.H1;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.FlexComponent;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.splitlayout.SplitLayout;
//import com.vaadin.flow.component.timepicker.TimePicker;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.data.binder.ValidationException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.vaadin.artur.helpers.CrudServiceDataProvider;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.router.PageTitle;
//import com.health.application.views.main.MainView;
//import com.vaadin.flow.router.RouteAlias;
//import com.vaadin.flow.data.renderer.TemplateRenderer;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.checkbox.Checkbox;
//import com.vaadin.flow.data.converter.StringToIntegerConverter;
//
//@CssImport("./views/triagecrh/triage-crh-view.css")
//@Route(value = "triage-crh", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)
//@PageTitle("Triage CRH")
//public class TriageCRHView extends Div {
//
//    private Grid<Admission> grid = new Grid<>(Admission.class, false);
//    VerticalLayout editorLayout;
//
//    private DatePicker date;
//    private TimePicker time;
//    private Checkbox clerked;
//    private Checkbox postTaken;
//    private TextField presentingComplaint;
//    private TextField news;
//
//    private Button save = new Button("Save");
//    private Button cancel = new Button("Cancel");
//    private Button delete = new Button("Delete");
//
//    private BeanValidationBinder<Admission> binder;
//
//    private Admission admission;
//
//
//    public TriageCRHView(@Autowired AdmissionService admissionService) {
//        addClassName("triage-crh-view");
//        // Create UI
//        SplitLayout splitLayout = new SplitLayout();
//        splitLayout.setSizeFull();
//
//        createEditorLayout(splitLayout);
//        createGridLayout(splitLayout);
//
//        add(splitLayout);
//
//        // Configure Grid
////        grid.addColumn("adId").setAutoWidth(true);
//        grid.addColumn("date").setAutoWidth(true);
//        grid.addColumn("time").setAutoWidth(true);
//        grid.addColumn("patient").setAutoWidth(true);
//        grid.addColumn("ward").setAutoWidth(true);
//        TemplateRenderer<Admission> clerkedRenderer = TemplateRenderer.<Admission>of(
//                "<iron-icon hidden='[[!item.clerked]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.clerked]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
//                .withProperty("clerked", Admission::isClerked);
//        grid.addColumn(clerkedRenderer).setHeader("Clerked").setAutoWidth(true);
//
//        TemplateRenderer<Admission> postTakenRenderer = TemplateRenderer.<Admission>of(
//                "<iron-icon hidden='[[!item.postTaken]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.postTaken]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
//                .withProperty("postTaken", Admission::isPostTaken);
//        grid.addColumn(postTakenRenderer).setHeader("Post Taken").setAutoWidth(true);
//
//        grid.addColumn("presentingComplaint").setAutoWidth(true);
//        grid.addColumn("news").setAutoWidth(true);
//        grid.setDataProvider(new CrudServiceDataProvider<Admission, Integer>(admissionService));
//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
//        grid.setHeightFull();
//
//        // when a row is selected or deselected, populate form
//        grid.asSingleSelect().addValueChangeListener(event -> {
//            if (event.getValue() != null) {
//                Optional<Admission> admissionFromBackend = admissionService.get(event.getValue().getId());
//                // when a row is selected but the data is no longer available, refresh grid
//                if (admissionFromBackend.isPresent()) {
//                    populateForm(admissionFromBackend.get());
//                    splitLayout.getSecondaryComponent().setVisible(true);
//                } else {
//                    refreshGrid();
//                    splitLayout.getSecondaryComponent().setVisible(true);
//                }
//            } else {
//                clearForm();
////                splitLayout.getSecondaryComponent().setVisible(false);
//                splitLayout.getSecondaryComponent().setVisible(true);
//
//            }
//        });
//
//        // Configure Form
//        binder = new BeanValidationBinder<>(Admission.class);
//
//        // Bind fields. This where you'd define e.g. validation rules
//        binder.forField(news).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("news");
//
//        binder.bindInstanceFields(this);
//
//        cancel.addClickListener(e -> {
//            clearForm();
//            refreshGrid();
//        });
//
//        save.addClickListener(e -> {
//            try {
//                if (this.admission == null) {
//                    this.admission = new Admission();
//                }
//                binder.writeBean(this.admission);
//
//                admissionService.update(this.admission);
//                clearForm();
//                refreshGrid();
//                Notification.show("Admission details stored.");
//            } catch (ValidationException validationException) {
//                Notification.show("An exception happened while trying to store the admission details.");
//            }
//        });
//
//        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
//
//    }
//
//    private void createEditorLayout(SplitLayout splitLayout) {
//        editorLayout = new VerticalLayout();
//        editorLayout.setId("editor-layout");
//        editorLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
//
//        Div editorDiv = new Div();
//        editorDiv.setId("editor");
//
//        H1 title = new H1("Add/Modify");
//        title.setId("title");
//        editorLayout.add(title, editorDiv);
//
//        AdmissionEditor admissionEditor = new AdmissionEditor
//                (date, time, clerked, postTaken, presentingComplaint, news,
//                        save, cancel, delete);
//
//        editorDiv.add(admissionEditor);
//
//        splitLayout.addToSecondary(editorLayout);
//        editorLayout.setWidth("30%");
////        splitLayout.getSecondaryComponent().setVisible(false);
//        splitLayout.getSecondaryComponent().setVisible(true);
//
//    }
//
//
//
//    private void createGridLayout(SplitLayout splitLayout) {
//        Div wrapper = new Div();
//        wrapper.setId("grid-wrapper");
//        wrapper.setWidthFull();
//        splitLayout.addToPrimary(wrapper);
//        wrapper.add(grid);
//    }
//
//    private void refreshGrid() {
//        grid.select(null);
//        grid.getDataProvider().refreshAll();
//    }
//
//    private void clearForm() {
//        populateForm(null);
//    }
//
//    private void populateForm(Admission value) {
//        this.admission = value;
//        binder.readBean(this.admission);
//
//    }
//
//}
