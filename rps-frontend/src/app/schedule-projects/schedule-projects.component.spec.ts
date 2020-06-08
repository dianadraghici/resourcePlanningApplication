import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ScheduleProjectsComponent} from './schedule-projects.component';

describe('ScheduleProjectsComponent', () => {
    let component: ScheduleProjectsComponent;
    let fixture: ComponentFixture<ScheduleProjectsComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [ScheduleProjectsComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ScheduleProjectsComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    // it('should create', () => {
    //     expect(component).toBeTruthy();
    // });
});
