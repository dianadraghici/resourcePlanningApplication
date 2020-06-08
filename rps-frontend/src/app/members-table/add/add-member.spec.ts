import {ComponentFixture, TestBed} from "@angular/core/testing";
import {NgbdAddMember} from "./add-member";
import {Component, Directive, Input, Pipe, PipeTransform} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MembersTableService} from "../members-table.service";
import {ErrorMessages} from "../../utils/error.messages";
import {of, throwError} from "rxjs";
import {Constants} from "../../utils/utils.constants";
import {By} from "@angular/platform-browser";
import {Parameters} from "../../models/parameters.model";

describe('NgbdAddMember', () => {
    let fixture: ComponentFixture<NgbdAddMember>;
    let mockNgbModal;
    let mockMembersTableService;

    beforeEach(() => {
        mockNgbModal = jasmine.createSpyObj(['open','dismissAll']);
        mockMembersTableService = jasmine.createSpyObj(['addMember']);

        TestBed.configureTestingModule({
            declarations: [NgbdAddMember, TranslatePipeStub, TooltipDirectiveStub, AlertComponentStub],
            providers: [
                ErrorMessages,
                {provide: NgbModal, useValue: mockNgbModal},
                {provide: MembersTableService, useValue: mockMembersTableService}
            ],
            imports: [FormsModule]
        });

        fixture = TestBed.createComponent(NgbdAddMember);
        fixture.componentInstance.technologyList = dummyTechnologyList;
    });

    it('should create an instance of NgbdAddMember', () => {
        const component: NgbdAddMember = fixture.componentInstance;
        expect(component).toBeTruthy();
        expect(component.technologyList).toEqual(dummyTechnologyList);
        expect(component.dismissible).toBeTruthy();
    });

    it('should open the AddMemberModal when clicked', () => {
        //Arrange
        fixture.detectChanges();
        //Act
        fixture.debugElement
            .query(By.css('.addMember'))
            .triggerEventHandler('click', undefined);
        //Assert
        expect(mockNgbModal.open).toHaveBeenCalledTimes(1);
    });

    it('should successfully save a new member', () => {
        //Arrange
        fixture.detectChanges();
        fixture.debugElement.query(By.css('.addMember')).nativeElement.click();
        const component: NgbdAddMember = fixture.componentInstance;
        component.member.technologyId = dummyTechnologyList[0].id;
        const addedMemberToBeReturnFromBackend = {id:25, technologyDescription: dummyTechnologyList[0].description, ...component.member};
        mockMembersTableService.addMember.and.returnValue(of(addedMemberToBeReturnFromBackend));
        spyOn(component.memberToBeSent, 'emit');
        //Act
        component.save();
        //Assert
        expect(mockMembersTableService.addMember).toHaveBeenCalledTimes(1);
        expect(mockMembersTableService.addMember).toHaveBeenCalledWith(component.member);
        expect(component.successMsg).toEqual({type: 'success', msg: `${Constants.MEMBER_SAVED}`});
        expect(mockNgbModal.dismissAll).toHaveBeenCalled();
        expect(component.memberToBeSent.emit).toHaveBeenCalledWith(addedMemberToBeReturnFromBackend);
    });

    it('should simulate an error while save() is called', () => {
        //Arrange
        fixture.detectChanges();
        fixture.debugElement.query(By.css('.addMember')).nativeElement.click();
        const component: NgbdAddMember = fixture.componentInstance;
        component.member.technologyId = dummyTechnologyList[1].id;
        mockMembersTableService.addMember.and.returnValue(throwError({error: {message: 'Oops, Server error'}}));
        //Act
        component.save();
        //Assert
        expect(mockMembersTableService.addMember).toHaveBeenCalledTimes(1);
        expect(mockMembersTableService.addMember).toHaveBeenCalledWith(component.member);
        expect(component.errorMsg).toEqual({type: 'danger', msg: `${component.errorMessages.MEMBER_SAVED_ERROR}`});
    });

    it('should simulate a ConstraintViolation error while save() is called', () => {
        //Arrange
        fixture.detectChanges();
        fixture.debugElement.query(By.css('.addMember')).nativeElement.click();
        const component: NgbdAddMember = fixture.componentInstance;
        component.member.technologyId = dummyTechnologyList[1].id;
        const backendError = {error: {message: 'Oops, ConstraintViolationException detected by the server'}};
        mockMembersTableService.addMember.and.returnValue(throwError(backendError));
        //Act
        component.save();
        //Assert
        expect(mockMembersTableService.addMember).toHaveBeenCalledTimes(1);
        expect(mockMembersTableService.addMember).toHaveBeenCalledWith(component.member);
        expect(component.errorMsg).toEqual({type: 'danger', msg: `${component.errorMessages.MEMBER_STAFF_ALREADY_REGISTERED}`});
    });

    it('should call dismissAll on NgbModal when close() is called', () => {
        //Arrange
        const component: NgbdAddMember = fixture.componentInstance;
        //Act
        component.close();
        //Assert
        expect(mockNgbModal.dismissAll).toHaveBeenCalledTimes(1);
    });
});

const dummyTechnologyList: Parameters[] = [
    {
        id: 15,
        type: 'TECH',
        description: 'DBA'
    },
    {
        id: 33,
        type: 'TECH',
        description: 'JAVA'
    }
];

@Pipe({name: 'translate'})
class TranslatePipeStub implements PipeTransform {
    transform(key: string): string {
        return key;
    }
}

@Directive({selector: '[ngbTooltip]'})
class TooltipDirectiveStub {
    @Input() ngbTooltip: string;
}

@Component({selector: 'alert', template: ''})
class AlertComponentStub {
    @Input() type: string;
    @Input() dismissible: boolean;
    @Input() dismissOnTimeout: number | string;
}
