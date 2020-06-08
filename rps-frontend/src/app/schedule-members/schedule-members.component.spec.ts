import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleMembersComponent } from './schedule-members.component';

describe('ScheduleMembersComponent', () => {
  let component: ScheduleMembersComponent;
  let fixture: ComponentFixture<ScheduleMembersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScheduleMembersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScheduleMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
