import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {NgbDate, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CalendarService} from '../../calendar/calendar.service';
import {Project} from '../../models/project.model';
import {Parameters} from '../../models/parameters.model';
import {MemberPosition} from '../../models/member-position.model';
import {ProjectPosition} from '../../models/project-position';
import {Member} from '../../models/member.model';
import {MembersTableService} from '../members-table.service';
import {Calendar} from '../../models/calendar.model';
import {ErrorMessages} from '../../utils/error.messages';
import {CommonServicesService} from '../../utils/common-services.service';
import {Constants} from '../../utils/utils.constants';
import {Subscription} from "rxjs";

@Component({
    selector: 'ngbd-add-member-position',
    templateUrl: './add-member-position.html',
    styleUrls: ['./add-member-position.css']
})
export class NgbdAddMemberPosition implements OnDestroy{
    @Input() member: Member;
    @Input() percentList: Parameters[];
    @Input() projectsList: Project[];
    @Input() positionsMappedByProjectName: {string: ProjectPosition[]};
    @Output() newAssignedPosition = new EventEmitter<MemberPosition>();
    memberPosition: MemberPosition;
    projectId: number;
    projectPosition: ProjectPosition;
    successMsg: { type: 'success', msg: string };
    errorsOccurred: string[] = [];
    currentPositionsSet: string[];
    positionsMappedByPositionName: { [key: string]: ProjectPosition[] };
    currentPositionsList: ProjectPosition[];
    subscription: Subscription;
    /** Date picker*/
    startDateModel: NgbDate;
    endDateModel: NgbDate;

    constructor(private modalService: NgbModal, private calendarService: CalendarService,
                private membersTableService: MembersTableService, public commonService: CommonServicesService,
                public errorMessagesConstants: ErrorMessages) {
    }

    onProjectChanged(event: any) {
        this.memberPosition.percentId = undefined;
        this.startDateModel = undefined;
        this.endDateModel = undefined;
        const {projectName} = this.projectsList.find(project => project.id == event.target.value);
        this.currentPositionsList = this.positionsMappedByProjectName[projectName];
        this.currentPositionsSet = this.getPositionsSet(this.currentPositionsList);
        this.positionsMappedByPositionName = this.getPositionsMappedByPositionName(this.currentPositionsList);
    }

    onProjectPositionChanged(event) {
        this.projectPosition = this.currentPositionsList.find(pp => pp.id == event.target.value);
        this.memberPosition.percentId = 10;
        const startDateId = this.projectPosition.startDateCalendarDTO.id;
        const endDateId = this.projectPosition.endDateCalendarDTO.id;
        this.startDateModel = this.calendarService.mapCalendarFKToDate(startDateId, true);
        this.endDateModel = this.calendarService.mapCalendarFKToDate(endDateId, false);
    }

    open(content: any) {
        this.memberPosition = new MemberPosition();
        this.memberPosition.projectPositionDTO = new ProjectPosition();
        this.projectId = null;
        this.startDateModel = null;
        this.endDateModel = null;
        this.modalService.open(content, {ariaLabelledBy: 'add-member-position-title'});
    }

    save() {
        this.errorsOccurred = [];
        const {description} = this.percentList.find(p => p.id == this.memberPosition.percentId);
        this.memberPosition.percentDescription = description;
        this.memberPosition.memberDTO = new Member();
        this.memberPosition.memberDTO.id = this.member.id;
        /** Date picker*/
        this.memberPosition.startDateCalendarDTO = new Calendar();
        this.memberPosition.endDateCalendarDTO = new Calendar();
        this.memberPosition.startDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.startDateModel);
        this.memberPosition.endDateCalendarDTO.id = this.calendarService.mapDateToCalendarFK(this.endDateModel);

        if (this.commonService.converToDate(this.endDateModel) < this.commonService.converToDate(this.startDateModel)) {
            this.registerError(this.errorMessagesConstants.END_DATE_ERROR_MESSAGE);
        }

        /** If start date of assign position is before start date of the project */
        if (this.commonService.mapNgDateToString(this.startDateModel) < this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.projectPosition.startDateCalendarDTO.id, true))) {
            this.registerError(this.errorMessagesConstants.PROJECT_POSITION_DATE_BEFORE_PROJECT);
        }

        /** If end date of assign position is after end date of the project */
        if (this.commonService.mapNgDateToString(this.endDateModel) > this.commonService.mapNgDateToString(this.calendarService.mapCalendarFKToDate(this.projectPosition.endDateCalendarDTO.id, false))) {
            this.registerError(this.errorMessagesConstants.PROJECT_POSITION_DATE_AFTER_PROJECT);
        }

        /** Set projectDTO on memberPosition*/
        this.memberPosition.projectPositionDTO.projectDTO = this.projectsList.find(project => this.projectId == project.id);

        if (!this.isAnyError()) {
            this.subscription = this.membersTableService.addMemberPosition(this.memberPosition)
                .subscribe(newAssignedPosition => {
                        this.close();
                        this.newAssignedPosition.emit(<MemberPosition>newAssignedPosition);
                        this.successMsg = {type: 'success', msg: `${Constants.POSITION_ASSIGNED}`};
                    }, err => this.registerError(err.statusText)
                );
        }
    }

    isAnyError = (): boolean => this.errorsOccurred.length > 0;

    registerError = (errorMsg: string) => {
        this.errorsOccurred = [...this.errorsOccurred, errorMsg];
    };

    close(): void {
        this.modalService.dismissAll();
    }

    private getPositionsSet = (positionsList: ProjectPosition[] = []): string[] => {
        const allProjectPositions = positionsList.map(pp => pp.positionDescription);
        return Array.from(new Set(allProjectPositions));
    };

    private getPositionsMappedByPositionName(positionsList: ProjectPosition[] = []): { [key: string]: ProjectPosition[] } {
        return positionsList.reduce((acc, curr, _, src) => {
            acc[curr.positionDescription] = src.filter(pp => pp.positionDescription == curr.positionDescription);
            return acc;
        }, {});
    }

    ngOnDestroy(): void {
        if (!!this.subscription) this.subscription.unsubscribe();
    }
}
