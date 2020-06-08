import {Component, Input, EventEmitter, OnInit, Output, OnDestroy} from "@angular/core";
import {CalendarService} from "../../../calendar/calendar.service";
import {MembersTableService} from "../../members-table.service";
import {NgbDate, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Parameters} from "../../../models/parameters.model";
import {MemberPosition} from "../../../models/member-position.model";
import {ErrorMessages} from "../../../utils/error.messages";
import {CommonServicesService} from "../../../utils/common-services.service";
import {Subscription} from "rxjs";


@Component({
    selector: 'ngbd-member-positions-detail',
    templateUrl: './member-positions-detail.html',
    styleUrls: ['./member-positions-detail.css']
})
export class NgbdMemberPositionDetail implements OnInit, OnDestroy {

    @Input() memberPosition: MemberPosition;
    @Input() isDeactivatedMembersTableInactive: boolean;
    @Input() percentList: Parameters[];
    @Output() memberPositionToDelete = new EventEmitter<MemberPosition>();
    editMode: boolean = false;
    startDateModel: NgbDate;
    endDateModel: NgbDate;
    initialStartDateModel: NgbDate;
    initialEndDateModel: NgbDate;
    errorsOccurred: string[] = [];
    editSubscription: Subscription;

    constructor(private memberTableService: MembersTableService,
                private calendarService: CalendarService,
                public errorMessagesConstants: ErrorMessages,
                private modalService: NgbModal,
                private commonService: CommonServicesService) {
    }

    ngOnInit() {
        const startDateId = this.memberPosition.startDateCalendarDTO.id;
        const endDateId = this.memberPosition.endDateCalendarDTO.id;
        this.startDateModel = this.initialStartDateModel = this.calendarService.mapCalendarFKToDate(startDateId, true);
        this.endDateModel = this.initialEndDateModel = this.calendarService.mapCalendarFKToDate(endDateId, false);
    }

    save() {
        this.errorsOccurred = [];

        const {description} = this.percentList.find(({id}) => this.memberPosition.percentId == id);
        this.memberPosition.percentDescription = description;

        this.memberPosition.startDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.startDateModel);
        this.memberPosition.endDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.endDateModel);

        if (this.commonService.converToDate(this.endDateModel) < this.commonService.converToDate(this.startDateModel)) {
            this.registerError(this.errorMessagesConstants.END_DATE_ERROR_MESSAGE);
        }

        const startDate = this.calendarService.mapCalendarFKToDate(this.memberPosition.projectPositionDTO.startDateCalendarDTO.id, true);
        if (this.commonService.mapNgDateToString(this.startDateModel) < this.commonService.mapNgDateToString(startDate)) {
            this.registerError(this.errorMessagesConstants.PROJECT_POSITION_DATE_BEFORE_PROJECT);
        }

        /** If end date of assign position is after end date of the project */
        const endDate = this.calendarService.mapCalendarFKToDate(this.memberPosition.projectPositionDTO.endDateCalendarDTO.id, false);
        if (this.commonService.mapNgDateToString(this.endDateModel) > this.commonService.mapNgDateToString(endDate)) {
            this.registerError(this.errorMessagesConstants.PROJECT_POSITION_DATE_AFTER_PROJECT);
        }

        if (this.isAnyError()) {
            this.memberPosition.startDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.initialStartDateModel);
            this.memberPosition.endDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.initialEndDateModel);
        } else {
            this.editSubscription = this.memberTableService.editMemberPositions(this.memberPosition)
                .subscribe(() => {},err => this.registerError(err.statusText));
        }
    }

    /** open modal for confirmation on position deletion*/
    openModal(content) {
        this.modalService.open(content, {centered: true});
    }

    isAnyError = (): boolean => this.errorsOccurred.length > 0;

    registerError = (errorMsg: string) => {
        this.errorsOccurred = [...this.errorsOccurred, errorMsg];
    };

    /** this method removes this.projectPosition member position*/
    delete() {
        this.memberPositionToDelete.emit(this.memberPosition);
    }

    ngOnDestroy(): void {
        if (this.editSubscription) this.editSubscription.unsubscribe();
    }
}
