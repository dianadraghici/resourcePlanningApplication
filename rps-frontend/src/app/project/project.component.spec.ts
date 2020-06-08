import {ComponentFixture, TestBed } from '@angular/core/testing';
import { ProjectComponent } from './project.component';
import {Component, Input} from "@angular/core";
import {Project} from "../models/project.model";
import {TranslatePipe} from "../utils/translate.pipe";
import {ErrorMessages} from "../utils/error.messages";
import {CommonServicesService} from "../utils/common-services.service";
import {ProjectService} from "./project.service";
import {CalendarService} from "../calendar/calendar.service";
import {ParametersService} from "../parameters/parameters.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbDate, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {MatSelectModule} from "@angular/material";
import {AngularFontAwesomeModule} from "angular-font-awesome";
import {of} from "rxjs";
import {ProjectExport} from "../models/projectExportCSV.model";
import {and} from "@angular/router/src/utils/collection";

describe('ProjectComponent', () => {

  @Component({ selector: 'app-menu', template: '' })
  class MenuComponentStub {}

  @Component({
    selector: 'ngbd-add-project',
    template: ''
  })
  class NgbdAddProjectStub{
    @Input() statusList
    @Input() percentList
  }

  @Component({
    selector: 'ngbd-project-positions',
    template: ''
  })
  class NgbdProjectPositionsStub {
    @Input() project
  }

  @Component({
    selector: 'ngbd-add-position-project',
    template: ''
  })
  class NgbdAddProjectPositionStub{
    @Input() project;
    @Input() percentList;
    @Input() positionList;
  }

  const CALENDAR: any[] = [
    {id: "FY19-P01-W01", fiscalYear: 2019, period: 1, week: 1, bop: "2018-09-23", eop: "2018-09-29", quarter: "Q1"},
    {id: "FY19-P03-W10", fiscalYear: 2019, period: 3, week: 10, bop: "2018-11-25", eop: "2018-12-01",quarter: "Q1"},
    {id: "FY19-P02-W07", fiscalYear: 2019, period: 2, week: 7, bop: "2018-11-04", eop: "2018-11-10",quarter: "Q1"}];

  const PROJECTS: Project[] = [
    {id: 1, projectCode: "INTERN-19-001", projectName: "PSA Project Intern", statusId: 2, percentId: 10, startDateCalendarDTO: CALENDAR[0],endDateCalendarDTO: CALENDAR[1], statusDescription: "VALID", percentDescription: "100", startDateModel: new NgbDate(2018, 9, 23), endDateModel: new NgbDate(2018, 9, 29), bopString: "bop", eopString: "eop"},
    {id: 2, projectCode: "EU431-98584", projectName: "BNF - TMA JAVA JEE", statusId: 2, percentId: 10,startDateCalendarDTO: CALENDAR[0],endDateCalendarDTO: CALENDAR[2], statusDescription: "In Negotiation", percentDescription: "60", startDateModel: new NgbDate(2019, 1, 23), endDateModel: new NgbDate(2019, 6, 28), bopString: "bop", eopString: "eop"}];

  let projectService ;
  let commonService ;
  let calendarService ;
  let paramService;

  let fixture: ComponentFixture<ProjectComponent>;
  let component: ProjectComponent;


  beforeEach(() => {
    projectService = jasmine.createSpyObj(['getProjectsPage', 'getProjects', 'editProject']);
    commonService = jasmine.createSpyObj(['enableExportFor', 'applyPageDataSource','mapNgDateToString']);
    calendarService = jasmine.createSpyObj(['mapCalendarFKToDate', 'mapDateToCalendarFK']);
    paramService = jasmine.createSpyObj(['getStatusParameters', 'getPercentParameters', 'getPositionParameters']);

    TestBed.configureTestingModule({
      declarations: [ProjectComponent,TranslatePipe, MenuComponentStub, NgbdAddProjectStub, NgbdProjectPositionsStub, NgbdAddProjectPositionStub],
      providers:[ ErrorMessages,
        {provide: ProjectService, useValue: projectService},
        {provide: CommonServicesService, useValue: commonService},
        {provide: CalendarService, useValue: calendarService},
        {provide: ParametersService, useValue: paramService}
      ],
      imports: [HttpClientTestingModule, FormsModule,ReactiveFormsModule,NgbModule,MatSelectModule, AngularFontAwesomeModule],
    });
  });

  it('should create an instance of ProjectComponent', () => {
    //arrange
    fixture = TestBed.createComponent(ProjectComponent);

    //act
    component = fixture.componentInstance;

    //assert
    expect(component).toBeTruthy();
  });

  it('should set active page for main pagination', () => {
    //arrange
    fixture = TestBed.createComponent(ProjectComponent);
    component = fixture.componentInstance;
    projectService.getProjectsPage.and.returnValue(of( {first: [...PROJECTS], second: 25}));
    projectService.getProjects.and.returnValue(of(PROJECTS));
    paramService.getStatusParameters.and.returnValue(of(true));
    paramService.getPercentParameters.and.returnValue(of(true));
    paramService.getPositionParameters.and.returnValue(of(true));

    //act
    component.ngOnInit();
    component.setActivePageForMainPagination(1);

    //assert
    expect(component.mainPagination.currentPage).toBe(1);
    expect(component.projects).toEqual([...PROJECTS]);
  });

  it('the project export list should be the given project list', () => {
    //arrange
     component = TestBed.createComponent(ProjectComponent).componentInstance;
     projectService.getProjects.and.returnValue(of(PROJECTS));

    //act
    component.generateExcelWithTabelHeaders(PROJECTS);
    const projectForExport = component.projectExportList;

    //assert
    expect(projectForExport.length).toEqual(2);
    expect(projectForExport[0].statusDescription).toEqual(PROJECTS[0].statusDescription);
  });

  it('should set the active page for search and render an array of filtered Projects', () => {
    //arrange
    component = TestBed.createComponent(ProjectComponent).componentInstance;
    commonService.applyPageDataSource.and.returnValue(PROJECTS);

    //act
    component.setActivePageForSearchPagination(1);

    //assert
    expect(component.searchPagination.currentPage).toBe(1);
    expect(component.projectsFiltered).toEqual([...PROJECTS]);
  });

  it('should update the fullProjectsList with the updatedProject', () => {
    //arrange
    component = TestBed.createComponent(ProjectComponent).componentInstance;
    (component as any).fullProjectsList = PROJECTS;

    spyOn((component as any), 'canUpdateProjectAt').and.returnValue(true);
    spyOn((component as any), 'isPercentAndStatusConditionValidated').and.returnValue(true);
    calendarService.mapDateToCalendarFK.and.returnValue(true);
    commonService.mapNgDateToString.and.returnValue(true);
    projectService.editProject.and.returnValue(of(JSON.stringify({...PROJECTS[0], statusDescription: 'UpdatedStatusDescription'})));

    // //act
   const lett  = component.editOnClick({...PROJECTS[0]}, 0);

    // //assert
    expect((component as any).fullProjectsList[0]).toEqual({...PROJECTS[0]});
    expect((component as any).fullProjectsList[1]).toEqual({...PROJECTS[1]});
    expect((component as any).fullProjectsList.length).toEqual(PROJECTS.length);

  });

});
