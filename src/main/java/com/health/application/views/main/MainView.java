package com.health.application.views.main;

import com.health.application.data.entity.Admission;
import com.health.application.data.entity.Patient;
import com.health.application.data.entity.Ward;
import com.health.application.data.service.AdmissionService;
import com.health.application.data.service.PatientService;
import com.health.application.data.service.WardService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.Registration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@CssImport("./styles/shared-styles.css")
@CssImport(
        themeFor = "vaadin-grid",
        value = "./styles/dynamic-grid-row-background-color.css"
)
@PWA(name = "CRH Triage System", shortName = "Triage", enableInstallPrompt = false)
@Route("")
@PreserveOnRefresh
public class MainView extends VerticalLayout {

    private Grid<Admission> grid = new Grid<>(Admission.class);
    HorizontalLayout topLayout;
    private TextField filter = new TextField();
    private ComboBox<Ward> wardSelector = new ComboBox<>();
    private SplitLayout content;
    private Button addPtBtn;
    private AdmissionService admissionService;
    private PatientService patientService;
    private WardService wardService;
    private AdmissionForm admissionForm;
    private Button sortButton;
    private List<GridSortOrder<Admission>> sortList;

    public MainView(AdmissionService admissionService, PatientService patientService,
                    WardService wardService) {
        this.admissionService = admissionService;
        this.patientService = patientService;
        this.wardService = wardService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureButtons();
        configureTopLayout();

        VerticalLayout forms = new VerticalLayout();
        forms.addClassName("forms");

        forms.add(admissionForm);
        content = new SplitLayout(grid, forms);
        content.addClassName("content");
        content.setSizeFull();
        content.setSplitterPosition(70);

        add(topLayout, content);
        this.setMinWidth("1000px");
        updateList();
        closeEditor();
    }

    private void configureGrid() {

        //Setup Grid
        grid.addClassName("admission-grid");
        grid.setSizeFull();
        grid.removeAllColumns();

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
        }).setKey("wardName")
                .setHeader("Ward").setAutoWidth(true);

        grid.addColumn(admission -> {
            LocalDateTime from = LocalDateTime.of(admission.getDate(), admission.getTime());

            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(from, now);
            return duration.toHours();
        }).setHeader("Wait (hrs)").setKey("wait");

        grid.addColumn("news");

        grid.addColumn("presentingComplaint");

        grid.addColumn(admission -> {
            Admission.ClerkedBy clerkedBy = admission.getClerkedBy();
            return clerkedBy == null ? "-" : clerkedBy;
        })
                .setHeader("Clerked By").setAutoWidth(true);

        grid.addColumn(admission -> (admission.isPostTaken()) ? "Yes" : "No")
                .setHeader("Post Taken").setAutoWidth(true).setKey("post-taken");

        grid.addColumn("frailtyScore")
                .setHeader("CFS");

        grid.addColumn(admission -> (admission.isDischargeable()) ? "Yes" : "No")
                .setHeader("?Dsc");

        //Invisible Columns

        //Wait >= 8 hours
        grid.addColumn(admission -> {
            LocalDateTime from = LocalDateTime.of(admission.getDate(), admission.getTime());

            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(from, now);
            return (duration.toHours() >= 8) ? "Yes" : "No";
        }).setHeader("Long Wait").setKey("longWait").setVisible(false);

        //As set by nursing staff. Is Clerk-in urgent?
        grid.addColumn(Admission::isUrgentClerk)
                .setHeader("Urgent").setKey("urgent").setVisible(false);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editAdmission(event.getValue()));

        sortGridConfig();
    }

    private void configureForm() {
        //Setup Admission Form
        admissionForm = new AdmissionForm(wardService.findAll());

        admissionForm.addListener(AdmissionForm.SaveEvent.class, this::saveAdmission);
        admissionForm.addListener(AdmissionForm.DeleteEvent.class, this::deleteAdmission);
        admissionForm.addListener(AdmissionForm.CloseEvent.class, e -> closeEditor());

    }

    private void configureTopLayout() {
        filter.setPlaceholder("Search Name/Hosp no...");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());

        admissionForm.hospId.setPlaceholder("Search Hosp no...");
        admissionForm.hospId.setClearButtonVisible(true);
        admissionForm.hospId.setValueChangeMode(ValueChangeMode.LAZY);
        admissionForm.hospId.addValueChangeListener(e -> searchByHospId());

        wardSelector.setAllowCustomValue(false);
        wardSelector.setClearButtonVisible(true);

        wardSelector.setItems(wardService.findAll());
        wardSelector.setItemLabelGenerator(Ward::getWardName);
        wardSelector.setPlaceholder("Filter by Ward...");
        wardSelector.addValueChangeListener(e -> updateListByWard());

        //Header
        topLayout = new HorizontalLayout();
        topLayout.addClassName("header");
        topLayout.setPadding(false);
        topLayout.setWidthFull();

        wardSelector.getElement().getStyle().set("margin-right", "auto");
        addPtBtn.getElement().getStyle().set("margin-right", "50px");
        addPtBtn.getElement().getStyle().set("margin-left", "20px");
        addPtBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        H2 title = new H2("CRH Triage System");
        title.getElement().getStyle().set("margin-left", "50px");
        title.getElement().getStyle().set("margin-right", "20px");

        Anchor logout = new Anchor("logout", "Log out");

        sortButton = new Button("Sort by Urgency");
        sortButton.addClickListener(click -> sortGrid());

        topLayout.add(title, filter, wardSelector, sortButton, logout, addPtBtn);
        topLayout.setAlignItems(Alignment.CENTER);
    }

    private void configureButtons() {
        addPtBtn = new Button("Add Patient");
        addPtBtn.addClickListener(click -> addPatient());

    }

    private void sortGridConfig() {
        Grid.Column<Admission> postTaken = grid.getColumnByKey("post-taken");
        Grid.Column<Admission> urgent = grid.getColumnByKey("urgent");
        Grid.Column<Admission> longWait = grid.getColumnByKey("longWait");
        Grid.Column<Admission> news = grid.getColumnByKey("news");
        Grid.Column<Admission> wait = grid.getColumnByKey("wait");

        //        Grid.Column<Admission> ward = grid.getColumnByKey("wardName");

        sortList = new GridSortOrderBuilder<Admission>()
                .thenAsc(postTaken)
                .thenDesc(urgent)
                .thenDesc(longWait)
                .thenDesc(news)
                .thenDesc(wait)
                .build();

        sortGrid();

    }

    private void sortGrid () {
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.sort(sortList);
    }

    private void updateList() {
        grid.setItems(admissionService.findAll(filter.getValue()));
        setSeverityLabels();
        sortGrid();
    }

    private void updateListByWard() {
        grid.setItems(admissionService.findAllByWard(wardSelector.getValue()));
        setSeverityLabels();
        sortGrid();
    }

    private void setSeverityLabels() {
        grid.setClassNameGenerator(admission ->
                (admission.isUrgentClerk()) ? "high" : "low");
    }

    /**
     * If there is only one patient or patient admission, it attempts to autofind this patient
     *
     */
    private void searchByHospId () {
        if (admissionForm.hospId.getValue() == null
                || admissionForm.hospId.getValue() < 1000) return;

        List<Patient> pt = admissionService.findAllPts(admissionForm.hospId.getValue());
        if (pt == null || pt.size() == 0) {
            addPatient(admissionForm.hospId.getValue());
        } else if (pt.size() == 1) {
            List<Admission> pts = admissionService.findAll(admissionForm.hospId.getValue());
                if (pts.size() == 1) {
                    // Set admission
                    editAdmission(pts.get(0));
                } else {
                    //Set Patient. No admission found
                    Admission ad = new Admission();
                    ad = setDefaultAdmission(ad);

                    ad.setPatient(pt.get(0));
                    editAdmission(ad);
                }
        }
    }

    public void editAdmission(Admission admission) {
        if (admission == null) {
            closeEditor();
        } else {
            admissionForm.setAdmission(admission);
            admissionForm.setVisible(true);
            addClassName("editing");
            content.setSplitterPosition(70);
        }
    }

    private void closeEditor() {
        admissionForm.setAdmission(null);
        admissionForm.setVisible(false);
        removeClassName("editing");
        content.setSplitterPosition(100);
        sortGrid();
    }

    private void addPatient() {
        grid.asSingleSelect().clear();
        Admission ad = new Admission();
        ad = setDefaultAdmission(ad);

        ad.setPatient(new Patient());
        editAdmission(ad);
    }

    private void addPatient(int hospId) {
        grid.asSingleSelect().clear();
        Admission ad = new Admission();
        ad = setDefaultAdmission(ad);

        ad.setPatient(new Patient());
        ad.getPatient().setHospId(hospId);
        editAdmission(ad);
    }

    private Admission setDefaultAdmission(Admission ad) {
        ad.setDate(LocalDate.now());
        ad.setTime(LocalTime.now());
        ad.setWard(wardService.findEMU());
        ad.setClerkedBy(Admission.ClerkedBy.None);
        ad.setNews(0);
        ad.setFrailtyScore(1);
        return ad;
    }

    private void saveAdmission(AdmissionForm.SaveEvent event) {
        patientService.save(event.getAdmission().getPatient());
        admissionService.save(event.getAdmission());
        updateList();
        closeEditor();
    }

    private void deleteAdmission(AdmissionForm.DeleteEvent event) {
        admissionService.delete(event.getAdmission());
        updateList();
        closeEditor();
    }

}
