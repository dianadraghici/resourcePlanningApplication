import {Component, OnInit, Output, EventEmitter, Input, OnChanges} from '@angular/core';
import {NgbDate, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CalendarService} from "../../calendar/calendar.service";
import {Project} from "../../models/project.model";
import {Parameters} from "../../models/parameters.model";
import {ProjectService} from "../project.service";
import {ProjectPositionService} from "../position/view/project-position.service";
import {Calendar} from "../../models/calendar.model";
import {ProjectPosition} from "../../models/project-position";
import {ErrorMessages} from "../../utils/error.messages";
import {CommonServicesService} from "../../utils/common-services.service";
import {Constants} from "../../utils/utils.constants";

@Component({
    selector: 'ngbd-add-position-project',
    templateUrl: './add-position-project.html',
    styleUrls: ['./add-position-project.css']
})

export class NgbdAddProjectPosition implements OnInit, OnChanges {

    @Input() project: Project;
    @Input() positionList: Parameters[];
    @Input() percentList: Parameters[];
    projectPosition: ProjectPosition = new ProjectPosition();
    errorMessage: any;
    errorMessagesArray: string[] = [];
    errorIsOcured: boolean;
    alerts: any[];
    dismissible = true;
    defaultAlerts: any[];
    //Models for the date picker widgets
    startDateModel: NgbDate;
    endDateModel: NgbDate;

    @Output() projectPositionToBeSent = new EventEmitter<ProjectPosition>();

    constructor(private modalService: NgbModal, private calendarService: CalendarService,
                private projectService: ProjectService, private projectPositionService: ProjectPositionService,
                public errorMessagesConstants: ErrorMessages, public commonService: CommonServicesService) {

    }

    ngOnInit() {
        this.errorIsOcured=false;
    }

    ngOnChanges() {
        this.ngOnInit();
    }

    open(content: any) {
        this.errorIsOcured=false;
        this.projectPosition = new ProjectPosition();
        this.projectPosition.numberPositions = 1;
        this.projectPosition.percentId = 10;
        this.startDateModel = this.calendarService.mapCalendarFKToDate(this.project.startDateCalendarDTO.id, true);
        this.endDateModel = this.calendarService.mapCalendarFKToDate(this.project.endDateCalendarDTO.id, false);
        this.modalService.open(content, {ariaLabelledBy: 'add-project-position-title'}).result.then(() => {
        }).catch(() => {
        });
    }

    save(e) {
        this.errorIsOcured=false;
        this.errorMessagesArray = [];
        this.projectPosition.numberPositions = e.target.NumberOfPositions.value;
        this.projectPosition.projectDTO = new Project();
        this.projectPosition.projectDTO.id = this.project.id;
        this.projectPosition.startDateCalendarDTO = new Calendar();
        this.projectPosition.endDateCalendarDTO = new Calendar();
        this.projectPosition.startDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.startDateModel);
        this.projectPosition.endDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.endDateModel);


        if (this.commonService.converToDate(this.endDateModel) < this.commonService.converToDate(this.startDateModel)) {
            this.errorMessagesArray.push(this.errorMessagesConstants.END_DATE_ERROR_MESSAGE);
            this.errorIsOcured = true;
        }

        /** If start date does not exist in database => error*/
        if(this.projectPosition.startDateCalendarDTO.id == null){
            this.errorMessagesArray.push(this.errorMessagesConstants.NULL_START_DATE_ID);
            this.errorIsOcured = true;
        }

        /** If end date does not exist in database  => error*/
        if(this.projectPosition.endDateCalendarDTO.id == null){
            this.errorMessagesArray.push(this.errorMessagesConstants.NULL_END_DATE_ID);
            this.errorIsOcured = true;
        }

        /** If start date of assign position is before start date of the project */
        if (this.commonService.mapNgDateToString(this.startDateModel) < this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.project.startDateCalendarDTO.id, true)) ) {
            this.errorMessagesArray.push(this.errorMessagesConstants.PROJECT_POSITION_DATE_BEFORE_PROJECT);
            this.errorIsOcured = true;
        }

        /** If end date of assign position is after end date of the project */
        if (this.commonService.mapNgDateToString(this.endDateModel) > this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.project.endDateCalendarDTO.id, false))) {
            this.errorMessagesArray.push(this.errorMessagesConstants.PROJECT_POSITION_DATE_AFTER_PROJECT);
            this.errorIsOcured = true;
        }

        this.projectPosition.positionDescription = this.positionList.filter((param:Parameters) =>
            this.projectPosition.positionId == param.id)[0].description;
        this.projectPosition.percentDescription = this.percentList.filter((param:Parameters) =>
            this.projectPosition.percentId == param.id)[0].description;

        if (!this.errorIsOcured) {
            this.projectPositionService.addProjectPosition(this.projectPosition).subscribe(
                isSuccess => {

                    this.close();
                    this.renderProjectPositionList();
                    this.defaultAlerts = [
                        {
                            type: 'success',
                            msg: `${Constants.ADD_POSITION_TO_PROJECT}`
                        }
                    ];
                    this.alerts = this.defaultAlerts;
                },
                isError => {
                    this.errorIsOcured = true;
                    this.errorMessage = 'SERVER ERRORS';
                }
            )
        }
    }

    close() {
        this.modalService.dismissAll();
    }

    renderProjectPositionList() {
        this.projectPositionToBeSent.emit(this.projectPosition);
    }
}
