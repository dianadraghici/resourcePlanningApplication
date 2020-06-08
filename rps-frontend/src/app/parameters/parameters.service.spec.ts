import { TestBed } from '@angular/core/testing';
import { ParametersService } from './parameters.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Constants} from "../utils/utils.constants";

describe('ParametersService', () => {
  let httpClientMock;
  let service;

  const PARAMETER_LIST: any[] = [
    {id:3, type:"PERCENT", description: "30"},
    {id:1, type:"TECH", description: "DATA"},
    {id:3, type: "STATUS", description: "TERMINATED"},
    {id:1, type: "POSITION", description: "Java Developer"}];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    httpClientMock = TestBed.get(HttpTestingController);
    service = TestBed.get(ParametersService);
  });

  it('should create an instance of ParametersService', () => {
    const service = TestBed.get(ParametersService);

    expect(service).toBeTruthy();
  });

  it('should be called with the Status Parameter', () => {
    const expectedStatus = PARAMETER_LIST.filter( data => data.type == "STATUS");

    service.getStatusParameters().subscribe(data => {
      expect(data).toEqual(expectedStatus);
    });

    const result = httpClientMock.expectOne(Constants.PARAMETER_CONTROLLER + Constants.GET_STATUS_PARAMETERS);
    expect(result.request.method).toBe('GET');
    expect(result.request['withCredentials']).toBeTruthy();

    result.flush(expectedStatus);
    httpClientMock.verify();
  });

  it('should be called with the Percent Parameter', () => {
    const expectedPercent = PARAMETER_LIST.filter(data => data.type == "PERCENT");

    service.getPercentParameters().subscribe(data => {
      expect(data).toEqual(expectedPercent);
    });

    const result = httpClientMock.expectOne(Constants.PARAMETER_CONTROLLER + Constants.GET_PERCENT_PARAMETERS);
    expect(result.request.method).toBe('GET');
    expect(result.request['withCredentials']).toBeTruthy();

    result.flush(expectedPercent);
    httpClientMock.verify();
  });

  it('should be called with the Technology Parameter', () => {
    const expectedTechnology = PARAMETER_LIST.filter(data => data.type == "TECH");

    service.getTechnologyParameters().subscribe(data => {
      expect(data).toEqual(expectedTechnology);
    });

    const result = httpClientMock.expectOne(Constants.PARAMETER_CONTROLLER + Constants.GET_TECHNOLOGY_PARAMETERS);
    expect(result.request.method).toBe('GET');
    expect(result.request['withCredentials']).toBeTruthy();

    result.flush(expectedTechnology);
    httpClientMock.verify()
  });

  it('should be called with the Position Parameter', () => {
    const expectedPosition = PARAMETER_LIST.filter( data =>  data.type == "POSITION" );

    service.getPositionParameters().subscribe(data => {
      expect(data).toEqual(expectedPosition);
    });

    const result = httpClientMock.expectOne(Constants.PARAMETER_CONTROLLER + Constants.GET_POSITION_PARAMETERS);
    expect(result.request.method).toBe('GET');
    expect(result.request['withCredentials']).toBeTruthy();

    result.flush(expectedPosition);
    httpClientMock.verify()
  });

});
