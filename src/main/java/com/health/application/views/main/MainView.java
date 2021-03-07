package com.health.application.views.main;

import com.health.application.data.entity.Admission;
import com.health.application.data.entity.Patient;
import com.health.application.data.entity.Ward;
import com.health.application.data.service.AdmissionService;
import com.health.application.data.service.PatientService;
import com.health.application.data.service.WardService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/shared-styles.css")
@PWA(name = "Triage CRH", shortName = "Triage CRH", enableInstallPrompt = false)
@Route("")
public class MainView extends VerticalLayout {

    private Grid<Admission> grid = new Grid<>(Admission.class);
    private TextField filter = new TextField();
    private SplitLayout content;
    private Button addPtBtn;
    private AdmissionService admissionService;
    private PatientService patientService;
    private WardService wardService;
    private PatientForm ptForm;
    private AdmissionForm admissionForm;

    public MainView(AdmissionService admissionService, PatientService patientService,
                    WardService wardService) {
        this.admissionService = admissionService;
        this.patientService = patientService;
        this.wardService = wardService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureFilter();
        configureForm();
        configureButtons();

        VerticalLayout forms = new VerticalLayout();
        forms.addClassName("forms");

        forms.add(ptForm, admissionForm);
        content = new SplitLayout(grid, forms);
        content.addClassName("content");
        content.setSizeFull();
        content.setSplitterPosition(70);

        //Header
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.addClassName("header");
        topLayout.add(filter, addPtBtn);

        add(topLayout, content);
        updateList();


        closeEditor();
    }

    private void configureGrid() {

        //Setup Grid
        grid.addClassName("admission-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

        grid.addColumn("news");
        grid.addColumn(admission -> {
            Patient pt = admission.getPatient();
            return pt == null ? "-" : pt.getHospId();
        }).setHeader("Hosp No");

        grid.addColumn(admission -> {
            Patient pt = admission.getPatient();
            return pt == null ? "-" : pt.getFirstName() + " " + pt.getLastName();
        }).setHeader("Patient");

        grid.addColumn(admission -> {
            Ward ward = admission.getWard();
            return ward == null ? "-" : ward.getWardName();
        }).setKey("wardName").setHeader("Ward");

        TemplateRenderer<Admission> clerkedRenderer = TemplateRenderer.<Admission>of(
                "<iron-icon hidden='[[!item.clerked]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); " +
                        "height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon " +
                        "hidden='[[item.clerked]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); " +
                        "height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("clerked", Admission::isClerked);
        grid.addColumn(clerkedRenderer).setHeader("Clerked").setAutoWidth(true);
        TemplateRenderer<Admission> postTakenRenderer = TemplateRenderer.<Admission>of(
                "<iron-icon hidden='[[!item.postTaken]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.postTaken]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("postTaken", Admission::isPostTaken);
        grid.addColumn(postTakenRenderer).setHeader("Post Taken").setAutoWidth(true);
        grid.addColumn("presentingComplaint");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        sortGrid();

        grid.asSingleSelect().addValueChangeListener(event ->
                editAdmission(event.getValue()));
    }

    private void configureForm() {
        //Setup Admission Form
        ptForm = new PatientForm();
        admissionForm = new AdmissionForm(wardService.findAll());

        ptForm.addListener(PatientForm.SaveEvent.class, this::savePatient);
        ptForm.addListener(PatientForm.DeleteEvent.class, this::deletePatient);
        ptForm.addListener(PatientForm.CloseEvent.class, e -> closeEditor());

        admissionForm.addListener(AdmissionForm.SaveEvent.class, this::saveAdmission);
        admissionForm.addListener(AdmissionForm.DeleteEvent.class, this::deleteAdmission);
        admissionForm.addListener(AdmissionForm.CloseEvent.class, e -> closeEditor());

    }

    private void configureFilter() {
        filter.setPlaceholder("Search Name/Hosp no...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());
    }

    private void configureButtons() {
        addPtBtn = new Button("Add Patient");
        addPtBtn.addClickListener(click -> addPatient());

    }

    private void sortGrid() {
        List<GridSortOrder<Admission>> sortList = new ArrayList<GridSortOrder<Admission>>();

        GridSortOrder gsort2 = new GridSortOrder<Admission>(grid.getColumnByKey("wardName"),SortDirection.ASCENDING);
        GridSortOrder gsort = new GridSortOrder<Admission>(grid.getColumnByKey("news"),SortDirection.DESCENDING);

        sortList.add(gsort);
        sortList.add(gsort2);

        grid.sort(sortList);
    }

    private void updateList() {
        grid.setItems(admissionService.findAll(filter.getValue()));
    }

    public void editAdmission(Admission admission) {
        if (admission == null) {
            closeEditor();
        } else {

            ptForm.setPatient(admission.getPatient());
            ptForm.setVisible(true);

            admissionForm.setAdmission(admission);
            admissionForm.setVisible(true);
            addClassName("editing");
            content.setSplitterPosition(70);
        }
    }

    private void closeEditor() {
        ptForm.setPatient(null);
        ptForm.setVisible(false);

        admissionForm.setAdmission(null);
        admissionForm.setVisible(false);
        removeClassName("editing");
        content.setSplitterPosition(100);
    }

    private void addPatient() {
        grid.asSingleSelect().clear();
        Admission ad = new Admission();
        ad.setPatient(new Patient());
        editAdmission(new Admission());
    }

    private void saveAdmission(AdmissionForm.SaveEvent event) {
        admissionService.save(event.getAdmission());
        updateList();
        closeEditor();
    }

    private void savePatient(PatientForm.SaveEvent event) {
        patientService.save(event.getPatient());
        updateList();
        closeEditor();
    }

    private void deleteAdmission(AdmissionForm.DeleteEvent event) {
        admissionService.delete(event.getAdmission());
        updateList();
        closeEditor();
    }

    private void deletePatient(PatientForm.DeleteEvent event) {
        patientService.delete(event.getPatient());
        updateList();
        closeEditor();
    }

}
