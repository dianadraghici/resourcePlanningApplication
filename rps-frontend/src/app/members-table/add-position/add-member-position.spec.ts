import {TestBed} from '@angular/core/testing';
import {FormsModule} from '@angular/forms';
import {NgbdAddMemberPosition} from './add-member-position';
import {CalendarService} from '../../calendar/calendar.service';
import {CommonServicesService} from '../../utils/common-services.service';
import {ErrorMessages} from '../../utils/error.messages';
import {NgbDate, NgbModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {MembersTableService} from '../members-table.service';
import {Component, Input, Pipe, PipeTransform} from '@angular/core';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {MemberPosition} from '../../models/member-position.model';
import {Project} from '../../models/project.model';
import {ProjectPosition} from '../../models/project-position';
import {Member} from "../../models/member.model";
import {of, throwError} from "rxjs";

describe('NgbdAddMemberPosition', () => {
    let component: NgbdAddMemberPosition;
    let mockNgbModal;
    let mockCalendarService;
    let mockMembersTableService;
    let mockCommonServicesService;
    const percentData = [{type: 'PERCENT', id: 3, description: '30'}];
    const projectsData = [{id: 3, projectName: 'iTools'}];
    const positionsMappedByProjectName = {
        'iTools': [{
            id: 16,
            percentId: 10,
            startDateCalendarDTO: {id: 'FY19-P01-W01'},
            endDateCalendarDTO: {id: 'FY19-P12-W52'},
            positionDescription: 'Project Manager',
            projectName: 'iTools'
        }]
    };

    @Pipe({name: 'translate'})
    class TranslatePipeStub implements PipeTransform {
        transform(key: string): string {
            return key;
        }
    }

    @Component({selector: 'alert', template: ''})
    class AlertComponentStub {
        @Input() type: string;
        @Input() dismissible: boolean;
        @Input() dismissOnTimeout: number | string;
    }

    beforeEach(() => {
        mockNgbModal = jasmine.createSpyObj(['open', 'dismissAll']);
        mockCalendarService = jasmine.createSpyObj(['mapCalendarFKToDate', 'mapDateToCalendarFK']);
        mockMembersTableService = jasmine.createSpyObj(['addMemberPosition']);
        mockCommonServicesService = jasmine.createSpyObj(['converToDate', 'mapNgDateToString']);

        TestBed.configureTestingModule({
            declarations: [NgbdAddMemberPosition, TranslatePipeStub, AlertComponentStub],
            providers: [
                ErrorMessages,
                {provide: NgbModal, useValue: mockNgbModal},
                {provide: CalendarService, useValue: mockCalendarService},
                {provide: MembersTableService, useValue: mockMembersTableService},
                {provide: CommonServicesService, useValue: mockCommonServicesService}
            ],
            imports: [FormsModule, NgbModule, AngularFontAwesomeModule]
        });
        component = TestBed.createComponent(NgbdAddMemberPosition).componentInstance;
    });

    it('should create an instance of NgbdAddMemberPosition', () => {
        expect(component).toBeTruthy();
    });

    it('should simulate the onProjectChanged method call', () => {
        //Arrange
        const projectId = projectsData[0].id;
        const projectName = projectsData[0].projectName;
        const firstProjectPositionFound = positionsMappedByProjectName[projectName][0];
        component.memberPosition = new MemberPosition();
        component.projectsList = projectsData as Project[];
        component.positionsMappedByProjectName = positionsMappedByProjectName as any;
        //Act
        component.onProjectChanged({target: {value: projectId}});
        //Assert
        expect(component.currentPositionsList).toEqual(component.positionsMappedByProjectName[projectName]);
        expect(component.currentPositionsSet[0]).toEqual(firstProjectPositionFound.positionDescription);
        expect(component.positionsMappedByPositionName[firstProjectPositionFound.positionDescription][0]).toEqual(firstProjectPositionFound)
    });

    it('should simulate the onProjectPositionChanged method call', () => {
        //Arrange
        const projectName = projectsData[0].projectName;
        const firstProjectPosition = positionsMappedByProjectName[projectName][0];
        const positionId = firstProjectPosition.id;
        const startDate = new NgbDate(2018, 9, 23);
        const endDate = new NgbDate(2019, 9, 21);
        mockCalendarService.mapCalendarFKToDate.and.returnValues(startDate, endDate);
        component.memberPosition = new MemberPosition();
        component.currentPositionsList = positionsMappedByProjectName[projectName];
        //Act
        component.onProjectPositionChanged({target: {value: positionId}});
        //Assert
        expect(component.projectPosition).toEqual(firstProjectPosition);
        expect(component.projectPosition.percentId).toBe(10);
        expect(component.startDateModel).toEqual(startDate);
        expect(component.endDateModel).toEqual(endDate);
    });

    it('should simulate the open method call', () => {
        //Arrange
        const htmlContent = '<div>should check open test</div>';
        const memberPosition = new MemberPosition();
        memberPosition.projectPositionDTO = new ProjectPosition();
        //Act
        component.open(htmlContent);
        //Assert
        expect(component.memberPosition).toEqual(memberPosition);
        expect(component.memberPosition.projectPositionDTO).toEqual(memberPosition.projectPositionDTO);
        expect(mockNgbModal.open).toHaveBeenCalledTimes(1);
        expect(mockNgbModal.open).toHaveBeenCalledWith(htmlContent, {ariaLabelledBy: 'add-member-position-title'});
    });

    it('should assign a new member position successfully', () => {
        //Arrange
        const currentMemberPosition = {percentId: percentData[0].id, projectPositionDTO: {}} as MemberPosition;
        mockMembersTableService.addMemberPosition.and.returnValue(of({id: 101, ...currentMemberPosition}));
        spyOn(component, 'close');
        spyOn(component.newAssignedPosition, 'emit');
        component.member = {id: 3} as Member;
        component.projectsList = projectsData as Project[];
        component.percentList = percentData;
        component.memberPosition = currentMemberPosition;
        component.projectPosition = {
            startDateCalendarDTO: {id: 'FY19-P01-W01'},
            endDateCalendarDTO: {id: 'FY19-P12-W52'}
        } as ProjectPosition;
        //Act
        component.save();
        //Assert
        expect(component.close).toHaveBeenCalledTimes(1);
        expect(component.newAssignedPosition.emit).toHaveBeenCalledTimes(1);
        expect(component.successMsg).toEqual({type: 'success', msg: 'Position is successfully assigned'});
    });

    it('should simulate an error happened when assigning a new member position', () => {
        //Arrange
        const currentMemberPosition = {percentId: percentData[0].id, projectPositionDTO: {}} as MemberPosition;
        mockMembersTableService.addMemberPosition.and.returnValue(throwError({statusText: 'SERVER ERROR'}));
        spyOn(component, 'registerError');
        component.member = {id: 3} as Member;
        component.projectsList = projectsData as Project[];
        component.percentList = percentData;
        component.memberPosition = currentMemberPosition;
        component.projectPosition = {
            startDateCalendarDTO: {id: 'FY19-P01-W01'},
            endDateCalendarDTO: {id: 'FY19-P12-W52'}
        } as ProjectPosition;
        //Act
        component.save();
        //Assert
        expect(component.registerError).toHaveBeenCalledTimes(1);
        expect(component.registerError).toHaveBeenCalledWith('SERVER ERROR');
    });

    it('should return false when isAnyError method is called', () => {
        const isError = component.isAnyError();
        expect(isError).toBeFalsy();
    });

    it('should return true when isAnyError method is called', () => {
        component.errorsOccurred.push('A error happened when processing');
        const isError = component.isAnyError();
        expect(isError).toBeTruthy();
    });

    it('should simulate a registration of a new error message', () => {
        const newErrorMsg = 'A error happened when processing';
        component.registerError(newErrorMsg);
        expect(component.errorsOccurred).toContain(newErrorMsg);
    });


    it('should call dismissAll on NgbModal when close() is called', () => {
        component.close();
        expect(mockNgbModal.dismissAll).toHaveBeenCalledTimes(1);
    });
});
