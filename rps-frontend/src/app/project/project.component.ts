import {ChangeDetectorRef, Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ProjectService} from "./project.service";
import {Project} from "../models/project.model";
import {Angular5Csv} from "angular5-csv/dist/Angular5-csv";
import {CommonServicesService} from "../utils/common-services.service";
import {Constants} from "../utils/utils.constants";
import {ProjectExport} from "../models/projectExportCSV.model";
import {CalendarService} from "../calendar/calendar.service";
import {ProjectPositionService} from "./position/view/project-position.service";
import {ParametersService} from "../parameters/parameters.service";
import {Parameters} from "../models/parameters.model";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";
import {ErrorMessages} from "../utils/error.messages";
import {TranslatePipe} from "../utils/translate.pipe";
import {PaginationProperties} from "../models/pagination-properties.model";
import {Calendar} from "../models/calendar.model";


@Component({
    selector: 'app-project',
    templateUrl: './project.component.html',
    styleUrls: ['./project.component.css'],
    providers: [
        TranslatePipe
    ],
})
export class ProjectComponent implements OnInit {


    @ViewChildren('pages') pages: QueryList<any>;

    projectExportList: ProjectExport[] = [];
    projectExport: ProjectExport;
    projects: Project[];
    projectsFiltered: Project[];
    flagAscendingCode: string;
    flagAscendingName: string;
    searchText: string;
    percentSymbol: string = "%";
    options = {
        fieldSeparator: ',',
        quoteStrings: '"',
        decimalseparator: '.',
        showLabels: true,
        showTitle: true,
        useBom: true,
        headers: ['Code', 'Project Name', 'Status', 'Certitude', 'Start Date', 'End Date'],
        title: 'Projects'
    };
    optionsForExport = {
        fieldSeparator: ',',
        quoteStrings: '"',
        decimalseparator: '.',
        showLabels: true,
        showTitle: true,
        useBom: true,
        headers: ['', '', '', '', '', ''],
        title: ''
    };
    errorMessage: any;
    errorHasOccurred: boolean;

    /** Models for the date picker widgets*/
    startDateModel: NgbDate;
    endDateModel: NgbDate;
    /** Save status and percent from Parameters*/
    statusList: Parameters[];
    percentList: Parameters[];
    positionList: Parameters[];
    projectForm: FormGroup = new FormGroup({});
    validationError = {img: {path: "assets/img/validationError.png", height: 15}};
    private fullProjectsList: Project[] = [];
    private projectsSorted: Project[] = [];
    mainPagination: PaginationProperties<Project> = new PaginationProperties(true, Constants.MAX_ITEMS_PER_PAGE_FOR_MAIN_PAGINATION);
    searchPagination: PaginationProperties<Project> = new PaginationProperties(false, Constants.MAX_ITEMS_PER_PAGE_FOR_SEARCH_PAGINATION);

    constructor(private projectService: ProjectService,
                public commonService: CommonServicesService,
                private calendarService: CalendarService,
                private projectPositionService: ProjectPositionService,
                public errorMessages: ErrorMessages,
                public changeDetectorRef: ChangeDetectorRef,
                private parameterService: ParametersService,
                private translatePipe: TranslatePipe) {

    }

    ngOnInit() {
        this.setActivePageForMainPagination(this.mainPagination.currentPage);
        this.applySortingRules();
        this.flagAscendingCode = 'unsorted';
        this.flagAscendingName = 'unsorted';
        this.projectService.getProjects().subscribe(projects => this.fullProjectsList = projects.map(project => this.completeProjectWithDateModel(project)));
        this.parameterService.getStatusParameters().subscribe(statusList => this.statusList = statusList);
        this.parameterService.getPercentParameters().subscribe(percentList => this.percentList = percentList);
        this.parameterService.getPositionParameters().subscribe(positionList => this.positionList = positionList);
    }

    /** Save data for export from json(projects) into a temporary array*/
    generateExcelWithTabelHeaders(projects: Project[]) {
        /** Empty array of projects*/
        this.projectExportList.splice(0, this.projectExportList.length);
        for (let i in projects) {
            /** Initialize projectExport with null values  on every loop  to avoid overriding*/
            this.projectExport = new ProjectExport(null, null, null, null, null, null);
            this.projectExport.projectCode = projects[i].projectCode;
            this.projectExport.projectName = projects[i].projectName;
            this.projectExport.statusDescription = projects[i].statusDescription;
            this.projectExport.percentDescription = projects[i].percentDescription + "" + this.percentSymbol;
            this.projectExport.startDateCalendarDTO = projects[i].startDateCalendarDTO.bop;
            this.projectExport.endDateCalendarDTO = projects[i].endDateCalendarDTO.eop;
            /** Add in projectExportList all members from  the table*/
            this.projectExportList.push(this.projectExport);
        }
        return this.projectExportList;
    }

    private getProjectsToBeExported() {
        if (!this.searchText) {
            this.applySortingRules();
            this.projectExportList = this.generateExcelWithTabelHeaders(this.fullProjectsList);
        }
        this.commonService.enableExportFor(this.projectExportList);
        return this.projectExportList;
    }

    search() {
        if (!this.searchText) {
            this.refreshDataTable();
        }
        if (this.searchText) {
            this.searchPagination.data.source = this.commonService.filterIt(this.fullProjectsList, this.searchText);
            this.projectsFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.searchPagination.data.source);
            this.projectExportList = this.generateExcelWithTabelHeaders(this.searchPagination.data.source);
            this.mainPagination.display = false;
            this.searchPagination.display = true;
        }
    }

    generateCsv() {
        /** Translate the title and headers of columns in Excel*/
        this.optionsForExport.title = this.translatePipe.transform(this.options.title);
        for (let option in this.options.headers) {
            this.optionsForExport.headers[option] = this.translatePipe.transform(this.options.headers[option]);
        }
        /** Create the Excel*/
        new Angular5Csv(this.getProjectsToBeExported().filter((p, i) => this.commonService.objetsExported[i]),
            this.translatePipe.transform('Projects_List'), this.optionsForExport);
    }

    receiveMessage(project) {
        this.fullProjectsList = [...this.fullProjectsList, this.completeProjectWithDateModel(project)];
        if (!this.searchText) {
            this.refreshDataTable();
        } else {
            this.search();
        }
    }

    receiveMessagePosition($event) {
        this.refreshDataTable();
    }

    sortByColProjectCode() {
        if (!!this.searchText) {
            return this.searchNavigationSortByProjectCode();
        }
        if (this.flagAscendingCode === 'sorted-asc') {
            localStorage.setItem(Constants.SORT_ORDER_PROJECT, Constants.SORT_ORDER_PROJECT_DESCENDING_CODE);
            this.fullProjectsList.sort((a, b) => b.projectCode.localeCompare(a.projectCode));
            this.flagAscendingCode = 'sorted-desc';
        } else if(this.flagAscendingCode === 'sorted-desc') {
            localStorage.setItem(Constants.SORT_ORDER_PROJECT, Constants.SORT_ORDER_PROJECT_UNSORTED_CODE);
            this.fullProjectsList.sort((a, b) => b.projectCode.localeCompare(a.projectCode));
            this.flagAscendingCode = 'unsorted';
        } else {
            localStorage.setItem(Constants.SORT_ORDER_PROJECT, Constants.SORT_ORDER_PROJECT_ASCENDING_CODE);
            this.flagAscendingCode = 'sorted-asc';
        }
        this.flagAscendingName = 'unsorted';
        this.refreshDataTable();
    }

    sortByColProjectName() {
        if (!!this.searchText) {
            return this.searchNavigationSortByProjectName();
        }
        if (this.flagAscendingName === 'sorted-asc') {
            localStorage.setItem(Constants.SORT_ORDER_PROJECT, Constants.SORT_ORDER_PROJECT_DESCENDING_NAME);
            this.fullProjectsList.sort((a, b) => a.projectName.localeCompare(b.projectName));
            this.flagAscendingName = 'sorted-desc';
        } else if(this.flagAscendingName === 'sorted-desc') {
            localStorage.setItem(Constants.SORT_ORDER_PROJECT, Constants.SORT_ORDER_PROJECT_UNSORTED_NAME);
            this.fullProjectsList.sort((a, b) => b.projectName.localeCompare(a.projectName));
            this.flagAscendingName = 'unsorted';
        }  else {
            localStorage.setItem(Constants.SORT_ORDER_PROJECT, Constants.SORT_ORDER_PROJECT_ASCENDING_NAME);
            this.flagAscendingName = 'sorted-asc';
        }
        this.flagAscendingCode = 'unsorted';
        this.refreshDataTable();
    }

    editOnClick(project: Project, index: number) {
        if (this.canUpdateProjectAt(index) && this.isPercentAndStatusConditionValidated(index)) {
            this.errorHasOccurred = false;

            /** Set startDate FK and endDate FK for the current project*/

            project.startDateCalendarDTO = new Calendar();
            project.endDateCalendarDTO = new Calendar();

            project.startDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(project.startDateModel);
            project.startDateCalendarDTO.bop = this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(project.startDateCalendarDTO.id, true));
            project.endDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(project.endDateModel);
            project.endDateCalendarDTO.eop = this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(project.endDateCalendarDTO.id, false));
            this.projectService.editProject(project).subscribe(
                updatedProjectAsString => {
                    /** Updating this.fullProjectsList with the updatedProject*/
                    const updatedProject: Project = JSON.parse(updatedProjectAsString);
                    const projectIndex: number = this.fullProjectsList.findIndex(project => project.id === updatedProject.id);
                    this.fullProjectsList[projectIndex] = updatedProject;
                },
                isError => {
                    this.errorHasOccurred = true;
                    this.errorMessage = isError;
                }
            )
        }
    }

    private completeProjectWithDateModel(project: Project): Project {
        project.startDateModel = this.calendarService.mapCalendarFKToDate(project.startDateCalendarDTO.id, true);
        project.endDateModel = this.calendarService.mapCalendarFKToDate(project.endDateCalendarDTO.id, false);
        return project;
    }

    private projectFormValidator(index: number, action: string = 'addControl') {//////////////////////////////////////////////
        this.projectForm[action]('project_code_' + index, new FormControl('', [Validators.required, Validators.pattern('^[\\w.\'-]+$'), Validators.minLength(1)]));
        this.projectForm[action]('project_name_' + index, new FormControl('', [Validators.required, Validators.pattern('^[\\wA-zÀ-ú /,.\'-]+$'), Validators.minLength(1)]));
        this.projectForm[action]('project_percentId_' + index, new FormControl('', [Validators.required]));
        this.projectForm[action]('project_statusId_' + index, new FormControl('', [Validators.required]));
        this.projectForm[action]('project_startDateId_' + index, new FormControl('', [Validators.required]));
        this.projectForm[action]('project_endDateId_' + index, new FormControl('', [Validators.required]));
    }

    private canUpdateProjectAt(index: number): Boolean {
        return this.projectCodeControl(index).valid && this.projectNameControl(index).valid
            && this.projectStatusIdControl(index).valid && this.projectPercentIdControl(index).valid;
    }

    private isPercentAndStatusConditionValidated(index: number): Boolean {
        const percentIdFC = this.projectPercentIdControl(index);
        const statusIDFC = this.projectStatusIdControl(index);
        return ( (percentIdFC.valid && statusIDFC.valid) &&
                    ((percentIdFC.value == 0 && statusIDFC.value == 0) ||   // for Certitude = 0% -> Status = 'ABANDONED'
                    (percentIdFC.value == 10 && (statusIDFC.value == 3 || statusIDFC.value == 2)) ||    // for Certitude = 100% -> Status = 'TERMINATED' || 'VALID'
                    (percentIdFC.value >= 1 && percentIdFC.value <= 9) && (statusIDFC.value == 1)) );     // for  10% <= Certitude <= 90 -> Status = 'In Negotiation'
    }

    private isStartDateIdNull(index : number): Boolean{
        if(this.calendarService.mapDateToCalendarFK(this.projectStartDateIdControl(index).value) == null){
            return true;
        }
    }

    private isEndDateIdNull(index : number): Boolean{
        if(this.calendarService.mapDateToCalendarFK(this.projectEndDateIdControl(index).value) == null){
            return true;
        }
    }

    private isStarDateGraterThanEndDate(index : number): Boolean{
        if(this.commonService.converToDate(this.projectStartDateIdControl(index).value) > this.commonService.converToDate(this.projectEndDateIdControl(index).value))
        {
            return true;
        }
    }

    projectCodeControl(index: number): AbstractControl {
        return this.projectForm.controls['project_code_' + index];
    }

    projectNameControl(index: number) {
        return this.projectForm.controls['project_name_' + index];
    }

    projectPercentIdControl(index: number) {
        return this.projectForm.controls['project_percentId_' + index];
    }

    projectStatusIdControl(index: number) {
        return this.projectForm.controls['project_statusId_' + index];
    }

    projectStartDateIdControl(index: number) {
        return  this.projectForm.controls['project_startDateId_' + index];
    }

    projectEndDateIdControl(index: number) {
        return  this.projectForm.controls['project_endDateId_' + index];
    }


    /**
     * The pageChange event handler for the main pagination.
     * @param page - the current page number on the pagination.
     * */
    setActivePageForMainPagination(page: number) {
        this.mainPagination.currentPage = page;
        this.projectService.getProjectsPage(this.mainPagination.currentPage, this.mainPagination.maxPerPage)
            .subscribe(pageResponse => {
                this.projects = pageResponse.first;
                this.mainPagination.data.size = pageResponse.second;
                /*map to startDateModel and endDateModel the date received form the projects*/
                this.projects.map(el => this.completeProjectWithDateModel(el));
                /*Register on init the projectFormValidator for projects's attributes*/
                this.projects.forEach((project, index) => this.projectFormValidator(index));
                this.projectsFiltered = this.projects;
                this.commonService.enableExportFor(this.projectsFiltered);
            });
    }

    /**
     * The pageChange event handler for the search pagination.
     * @param page - the current page number on the pagination.
     * */
    setActivePageForSearchPagination(page: number) {
        this.searchPagination.currentPage = page;
        this.projectsFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.searchPagination.data.source);
    }

    /** The sort rules to be apply on this component on initialization. */
    private applySortingRules() {
        switch (localStorage.getItem(Constants.SORT_ORDER_PROJECT)) {
            case Constants.SORT_ORDER_PROJECT_ASCENDING_CODE:
                this.flagAscendingCode = 'sorted-asc';
                this.sortByColProjectCode();
                break;
            case Constants.SORT_ORDER_PROJECT_DESCENDING_CODE:
                this.flagAscendingCode = 'sorted-desc';
                this.sortByColProjectCode();
                break;
            case Constants.SORT_ORDER_PROJECT_UNSORTED_CODE:
                this.flagAscendingCode = 'unsorted';
                this.sortByColProjectCode();
                break;
            case Constants.SORT_ORDER_PROJECT_ASCENDING_NAME:
                this.flagAscendingName = 'sorted-asc';
                this.sortByColProjectName();
                break;
            case Constants.SORT_ORDER_PROJECT_DESCENDING_NAME:
                this.flagAscendingName = 'sorted-desc';
                this.sortByColProjectName();
                break;
            case Constants.SORT_ORDER_PROJECT_UNSORTED_NAME:
                this.flagAscendingName = 'unsorted';
                this.sortByColProjectName();
                break;
            case undefined:
                this.flagAscendingCode = 'unsorted';
                this.sortByColProjectCode();
                this.flagAscendingName = 'unsorted';
                this.sortByColProjectName();
                break;
        }
    }

    /** Refresh the data table for the current page. *////////////////////////////////////////////////////////////////////
    private refreshDataTable() {
        this.projectService.getProjectsPage(this.mainPagination.currentPage, this.mainPagination.maxPerPage).subscribe(pageResponse => {
            this.projects = pageResponse.first;
            this.projects.forEach((project, index) => this.projectFormValidator(index));
            this.mainPagination.data.size = pageResponse.second;
            this.projects.map(el => this.completeProjectWithDateModel(el));
            this.projectsFiltered = this.projects;
            this.mainPagination.display = true;
            this.searchPagination.display = false;
        });

    }

    /** The Sort to be apply for the ProjectCode column when the user is in search mode. */
    private searchNavigationSortByProjectCode() {
        if (this.flagAscendingCode === 'sorted-asc') {
            this.projectsSorted = this.searchPagination.data.source.sort((a, b) => a.projectCode.localeCompare(b.projectCode));
            this.projectExportList.sort((a, b) => a.projectCode.localeCompare(b.projectCode));
            this.flagAscendingCode = 'sorted-desc';
        } else if (this.flagAscendingCode === 'sorted-desc') {
            this.projectsSorted = this.searchPagination.data.source.sort((a, b) => b.projectCode.localeCompare(a.projectCode));
            this.projectExportList.sort((a, b) => b.projectCode.localeCompare(a.projectCode));
            this.flagAscendingCode = 'unsorted';
        } else {
            this.flagAscendingCode = 'sorted-asc';
        }
        this.projectsFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.projectsSorted);
        return;
    }

    /** The Sort to be apply for the ProjectName column when the user is in search mode. */
    private searchNavigationSortByProjectName() {
        if (this.flagAscendingName === 'sorted-asc') {
            this.projectsSorted = this.searchPagination.data.source.sort((a, b) => a.projectName.localeCompare(b.projectName));
            this.projectExportList.sort((a, b) => a.projectName.localeCompare(b.projectName));
            this.flagAscendingName = 'sorted-desc';
        } else if (this.flagAscendingName === 'sorted-desc') {
            this.projectsSorted = this.searchPagination.data.source.sort((a, b) => b.projectName.localeCompare(a.projectName));
            this.projectExportList.sort((a, b) => b.projectName.localeCompare(a.projectName));
            this.flagAscendingName = 'unsorted';
        } else {
            this.flagAscendingName = 'sorted-asc';
        }
        this.projectsFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.projectsSorted);
        return;
    }

}