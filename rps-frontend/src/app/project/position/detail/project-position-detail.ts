import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ProjectPosition} from "../../../models/project-position";
import {ParametersService} from "../../../parameters/parameters.service";
import {CalendarService} from "../../../calendar/calendar.service";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProjectService} from "../../project.service";
import {ProjectPositionService} from "../view/project-position.service";
import {Parameters} from "../../../models/parameters.model";
import {ErrorMessages} from "../../../utils/error.messages";
import {CommonServicesService} from "../../../utils/common-services.service";

@Component({
    selector: 'ngbd-project-position-detail',
    templateUrl: './project-position-detail.html',
    styleUrls: ['./project-position-detail.css']
})

export class NgbdProjectPositionDetail implements OnInit {

    @Input() projectPosition: ProjectPosition;
    percentList: Parameters[];
    errorHasOccurred: boolean = false;
    errorMessage: any;
    positionEditing: boolean = false;
    startDateModel: NgbDate;
    endDateModel: NgbDate;
    percentId: number;
    errorMessagesArray: string[] = [];
    errorIsOcured: boolean = false;
    @Output() projectPositionChange = new EventEmitter<ProjectPosition>();

    constructor(private projectService: ProjectService,
                private projectPositionService: ProjectPositionService,
                private parametersService: ParametersService,
                public errorMessagesConstants: ErrorMessages,
                private calendarService: CalendarService,
                private modalService: NgbModal,
                public commonService: CommonServicesService) {

    }

    ngOnInit() {
        this.startDateModel = this.calendarService.mapCalendarFKToDate(this.projectPosition.startDateCalendarDTO.id, true);
        this.endDateModel = this.calendarService.mapCalendarFKToDate(this.projectPosition.endDateCalendarDTO.id, false);
        this.parametersService.getPercentParameters()
            .subscribe(data => {
                this.percentList = data;
            });
    }

    save(e) {
        this.errorMessagesArray = [];
        this.projectPosition.percentDescription = this.percentList.filter(c => c.id == this.projectPosition.percentId)[0].description;
        this.errorIsOcured = false;
        this.projectPosition.startDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.startDateModel);
        this.projectPosition.endDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.endDateModel);

        if (this.commonService.converToDate(this.endDateModel) < this.commonService.converToDate(this.startDateModel)) {
            this.errorMessagesArray.push(this.errorMessagesConstants.END_DATE_ERROR_MESSAGE);
            this.errorIsOcured = true;
            this.positionEditing = false;
        }
        /** If start date does not exist in database => error*/
        if(this.projectPosition.startDateCalendarDTO.id == null){
            this.errorMessagesArray.push(this.errorMessagesConstants.NULL_START_DATE_ID);
            this.positionEditing = false;
            this.errorIsOcured = true;
        }

        /** If end date does not exist in database => error*/
        if(this.projectPosition.endDateCalendarDTO.id == null){
            this.errorMessagesArray.push(this.errorMessagesConstants.NULL_END_DATE_ID);
            this.errorIsOcured = true;
            this.positionEditing = false;
        }

        /** If start date of assign position is before start date of the project */
        if (this.commonService.mapNgDateToString(this.startDateModel) < this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.projectPosition.projectDTO.startDateCalendarDTO.id, true))) {
            this.errorMessagesArray.push(this.errorMessagesConstants.PROJECT_POSITION_DATE_BEFORE_PROJECT);
            this.errorIsOcured = true;
            this.positionEditing = false;
        }

        /** If end date of assign position is after end date of the project */
        if (this.commonService.mapNgDateToString(this.endDateModel) > this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.projectPosition.projectDTO.endDateCalendarDTO.id, false))) {
            this.errorMessagesArray.push(this.errorMessagesConstants.PROJECT_POSITION_DATE_AFTER_PROJECT);
            this.errorIsOcured = true;
            this.positionEditing = false;
        }

        if (!this.errorIsOcured) {

            this.projectService.editProjectPosition(this.projectPosition).subscribe(
                isSuccess => {
                    this.positionEditing = false;
                },
                isError => {
                    this.errorHasOccurred = true;
                }
            )
            this.errorIsOcured = false;
        }
    }

    /** open modal for confirmation on position deletion*/
    openModal(content) {
        this.modalService.open(content, {centered: true});
    }

    /** this method removes this.projectPosition project position*/
    delete() {
        this.projectPositionService.deleteProjectPosition(this.projectPosition).subscribe();
        this.projectPositionChange.emit(this.projectPosition);
    }
}
