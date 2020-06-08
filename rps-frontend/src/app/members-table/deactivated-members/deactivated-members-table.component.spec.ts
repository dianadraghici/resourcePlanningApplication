import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {DeactivatedMembersTableComponent} from "./deactivated-members-table.component";


describe('DeactivatedMembersTableComponent', () => {
    let component: DeactivatedMembersTableComponent;
    let fixture: ComponentFixture<DeactivatedMembersTableComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [DeactivatedMembersTableComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(DeactivatedMembersTableComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    // it('should create', () => {
    //     expect(component).toBeTruthy();
    // });
});
