import {Component, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {NgbDate, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CalendarService} from "../../calendar/calendar.service";
import {Project} from "../../models/project.model";
import {ProjectService} from "../project.service";
import {Parameters} from "../../models/parameters.model";
import {Calendar} from "../../models/calendar.model";
import {ErrorMessages} from "../../utils/error.messages";
import {CommonServicesService} from "../../utils/common-services.service";
import {Constants} from "../../utils/utils.constants";

@Component({
    selector: 'ngbd-add-project',
    templateUrl: './add-project.html',
    styleUrls: ['./add-project.css']
})

export class NgbdAddProject implements OnInit {

    @Input() statusList: Parameters[];
    @Input() percentList: Parameters[];
    project: Project = new Project();
    errorMessage: any;
    dismissible = true;
    errorIsOcured: boolean;
    projectName: string;
    alerts: any[];
    defaultAlerts: any[];
    errorMessagesArray: string[] = [];
    /** Models for the date picker widgets*/
    startDateModel: NgbDate;
    endDateModel: NgbDate;
    @Output() projectToBeSent = new EventEmitter<Project>();

    constructor(private modalService: NgbModal, private calendarService: CalendarService,
                private projectService: ProjectService,
                public  commonService: CommonServicesService,
                public errorMessages: ErrorMessages) {
    }

    ngOnInit() {
        this.errorIsOcured=false;
    }

    open(content: any) {
        this.errorIsOcured=false;
        this.project = new Project();
        this.startDateModel = null;
        this.endDateModel = null;
        this.modalService.open(content, {ariaLabelledBy: 'add-project-title'}).result.then(() => {
        }).catch(() => {
        });
    }

    save(e) {
        this.defaultAlerts = [
            {
                type: 'success',
                msg: `${Constants.PROJECT_CREATED}`
            }
        ];
        this.alerts = this.defaultAlerts;
        this.errorIsOcured=false;
        this.errorMessagesArray = [];
        let p = document.getElementById("projectCertitude");
        this.project.percentDescription = p[e.target.projectCertitude.selectedIndex].firstChild.textContent;

        let s = document.getElementById("projectStatus");
        this.project.statusDescription = s[e.target.projectStatus.selectedIndex].firstChild.textContent;

        this.project.startDateCalendarDTO = new Calendar();
        this.project.endDateCalendarDTO = new Calendar();
        this.project.startDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.startDateModel);
        this.project.startDateCalendarDTO.bop = this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.project.startDateCalendarDTO.id, true));

        this.project.endDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.endDateModel);
        this.project.endDateCalendarDTO.eop = this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.project.endDateCalendarDTO.id, false));

        if (this.commonService.mapNgDateToString(this.endDateModel) < this.commonService.mapNgDateToString(this.startDateModel)) {
            this.errorIsOcured = true;
            this.errorMessagesArray.push(this.errorMessages.END_DATE_ERROR_MESSAGE);
        }

        if (!this.errorIsOcured) {
            this.projectService.addProject(this.project).subscribe(
                isSuccess => {
                    this.close();
                    this.renderProjectList();
                },

                isError => {
                    this.errorIsOcured = true;
                }
            );
        }
    }

    close() {
        this.modalService.dismissAll();
    }

    renderProjectList() {
        this.projectToBeSent.emit(this.project);
    }


}
