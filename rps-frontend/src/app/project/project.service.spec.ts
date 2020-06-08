import { TestBed } from '@angular/core/testing';
import { ProjectService } from './project.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {Constants} from "../utils/utils.constants";
import {Project} from "../models/project.model";


describe('ProjectService', () => {
  let mockHttpClient : HttpTestingController;

  const CALENDAR: any[] = [
      {id: "FY19-P01-W01", fiscalYear: 2019, period: 1, week: 1, bop: "2018-09-23", eop: "2018-09-29", quarter: "Q1"},
      {id: "FY19-P03-W10", fiscalYear: 2019, period: 3, week: 10, bop: "2018-11-25", eop: "2018-12-01",quarter: "Q1"},
      {id: "FY19-P02-W07", fiscalYear: 2019, period: 2, week: 7, bop: "2018-11-04", eop: "2018-11-10",quarter: "Q1"}];

  const PROJECTS: Project[] = [
    {id: 1, projectCode: "INTERN-19-001", projectName: "PSA Project Intern", statusId: 2, percentId: 10, startDateCalendarDTO: CALENDAR[0],endDateCalendarDTO: CALENDAR[1], statusDescription: "VALID", percentDescription: "100", startDateModel: new NgbDate(2018, 9, 23), endDateModel: new NgbDate(2018, 9, 29), bopString: "bop", eopString: "eop"},
    {id: 2, projectCode: "EU431-98584", projectName: "BNF - TMA JAVA JEE", statusId: 2, percentId: 10,startDateCalendarDTO: CALENDAR[0],endDateCalendarDTO: CALENDAR[2], statusDescription: "In Negotiation", percentDescription: "60", startDateModel: new NgbDate(2019, 1, 23), endDateModel: new NgbDate(2019, 6, 28), bopString: "bop", eopString: "eop"}];

  const PROJECT_POSITIONS: any[] = [
      {id: 2, positionId: 2, numberPositions: 4, percentId: 50, projectName: "PSA Project Intern", projectDTO: PROJECTS[0], startDateCalendarDTO: CALENDAR[0], endDateCalendarDTO: CALENDAR[1], positionDescription: "Junior Java Developer", percentDescription: "50"},
      {id: 1, positionId: 1, numberPositions: 1, percentId: 10, projectName: "BNF - TMA JAVA JEE", projectDTO: PROJECTS[1], startDateCalendarDTO: CALENDAR[0], endDateCalendarDTO: CALENDAR[2], positionDescription: "Java Developer", percentDescription: "100"}];


  beforeEach(() => {TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    mockHttpClient = TestBed.get(HttpTestingController);
  });

  it('should create an instance of ProjectService and return a list of projects', () => {
    const service = TestBed.get(ProjectService);
    expect(service).toBeTruthy();

    service.getProjects().subscribe( list => {
      expect(list).toEqual(PROJECTS);
    });

    const result = mockHttpClient.expectOne(Constants.PROJECT_CONTROLLER);
    expect(result.request.method).toBe('GET');
    expect(result.request['withCredentials']).toBe(true);

    result.flush(PROJECTS);
    mockHttpClient.verify();
  });

  it('should add an project to the list  --- AddProject', () => {
    const service = TestBed.get(ProjectService);

    const newProject = {projectCode: "PISTE-19-01-001", projectName: "Thalès Digital Factory", statusId: 1,percentId: 10, startDateCalendarDTO: CALENDAR[0],endDateCalendarDTO: CALENDAR[1], statusDescription: "VALID", percentDescription: "100", startDateModel: new NgbDate(2020, 5, 5), endDateModel: new NgbDate(2021, 5, 29), bopString: "bop", eopString: "eop"};

    service.addProject(newProject).subscribe( project => {
        expect(project.id).toBeTruthy();
    });

    const result = mockHttpClient.expectOne(Constants.PROJECT_CONTROLLER);
      expect(result.request.method).toBe('POST');
      expect(result.request['withCredentials']).toBeTruthy();

    result.flush({id: 500, ...newProject});
    mockHttpClient.verify();
  });

  it('should edit a project list with a given project --- EditProject', () => {
      const service = TestBed.get(ProjectService);

      const updatedProject = {...PROJECTS[0]};
      updatedProject.endDateModel = new NgbDate(2019, 11, 30);
      updatedProject.projectName = "New project name";


     service.editProject(updatedProject).subscribe( project => {
       const result  = JSON.parse(project);
       result.startDateModel = new NgbDate(result.startDateModel.year, result.startDateModel.month, result.startDateModel.day);
       result.endDateModel = new NgbDate(result.endDateModel.year, result.endDateModel.month, result.endDateModel.day);

       expect(result).toEqual(updatedProject);
     });

     const result = mockHttpClient.expectOne(Constants.PROJECT_CONTROLLER + Constants.UPDATE_PROJECT_BY_ID + 1, null);
     expect(result.request.method).toBe('PUT');
     expect(result.request['withCredentials']).toBeTruthy();
     expect(result.request['responseType']).toBe('text');

     result.flush(updatedProject);
    mockHttpClient.verify();
  });
    it('should get a Project Position with a given project and use a given url ---GetProjectPositions', () => {
        const service = TestBed.get(ProjectService);

        const projectPositionFromBackend: any[] = [{"Junior Java Developer": [PROJECTS[0]] },{"Java Developer": [PROJECTS[1]] }];

        service.getProjectPositions(projectPositionFromBackend[0]).subscribe(result => {
            expect(result).toEqual(projectPositionFromBackend[0]);
        });

        const result = mockHttpClient.expectOne(Constants.PROJECT_POSITION_CONTROLLER + Constants.FIND_BY_ID_PROJECT_POSITION + projectPositionFromBackend[0].id );
        expect(result.request.method).toBe('GET');
        expect(result.request['withCredentials']).toBeTruthy();


        result.flush(projectPositionFromBackend[0]);
        mockHttpClient.verify();
    });

  it('should edit a project position form a list and use an given url ---- EditProjectPosition', () => {
      const service = TestBed.get(ProjectService);

      const projectPosition = {...PROJECT_POSITIONS[0]};
      projectPosition.projectDTO = PROJECTS[1];
      projectPosition.projectName = PROJECTS[1].projectName;
      projectPosition.numberPositions = 3;

      service.editProjectPosition(projectPosition).subscribe( pp => {
          const result  = JSON.parse(pp);
          result.projectDTO.startDateModel = new NgbDate(result.projectDTO.startDateModel.year, result.projectDTO.startDateModel.month, result.projectDTO.startDateModel.day);
          result.projectDTO.endDateModel = new NgbDate(result.projectDTO.endDateModel.year, result.projectDTO.endDateModel.month, result.projectDTO.endDateModel.day);
          expect(result).toEqual(projectPosition);
      });

      const result = mockHttpClient.expectOne(Constants.PROJECT_POSITION_CONTROLLER + Constants.UPDATE_PROJECT_POSITION);
      expect(result.request.method).toBe('PUT');
      expect(result.request['withCredentials']).toBeTruthy();
      expect(result.request['responseType']).toBe('text');

      result.flush(projectPosition);
      mockHttpClient.verify();
  });

  it( 'should return a page with a list of Projects', () => {
      const service = TestBed.get(ProjectService);

      service.getProjectsPage(0,50).subscribe( data => {
          expect(data).toBeDefined();
      });

      const result = mockHttpClient.match(req => req.url.match(`${Constants.PROJECT_CONTROLLER}${Constants.GET_PAGINATED_PROJECTS}` ) && req.method === 'GET' && req.withCredentials === true);
      const pageResponse = {first: [...PROJECTS], second: 25};

      result.forEach(tr => tr.flush(pageResponse));
      mockHttpClient.verify();
  });

    it( 'should ', () => {
        const service = TestBed.get(ProjectService);


        service.getProjectsPage(0, 1).subscribe(data => {
            expect(data).toBeDefined();
        });

        const result = mockHttpClient.match(req => req.url.match(`${Constants.PROJECT_CONTROLLER}${Constants.GET_PAGINATED_PROJECTS}` ) && req.method === 'GET' && req.withCredentials === true);
        const pageResponse = {};
        console.log(pageResponse)

        result.forEach(tr => tr.flush(PROJECTS));
        mockHttpClient.verify();
    });

});
