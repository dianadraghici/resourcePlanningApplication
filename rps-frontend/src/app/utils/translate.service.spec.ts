import { TranslateService } from './translate.service';
import {of} from "rxjs";

describe('TranslateService', () => {
  let mockHttpClient;

  beforeEach(() => {
    mockHttpClient = jasmine.createSpyObj(['get']);
  });

  it('should create an instance of TranslateService', () => {
    //Arrange
    const service = new TranslateService(mockHttpClient);
    //Assert
    expect(service).toBeTruthy();
  });


  it('should data property be an empty object', () => {
    //Arrange
    const service = new TranslateService(mockHttpClient);
    //Assert
    expect(service.data).toEqual({});
  });

  it('should not found a translation and return \'Welcome\'', () => {
    //Arrange
    const service = new TranslateService(mockHttpClient);
    mockHttpClient.get.and.returnValue(of({'Welcome':'Welcome'}));

    //Act
    service.use('en');

    //Assert
    expect(service.data['Welcome']).toBe('Welcome');
  });

  it('should translate \'Welcome\' to \'Bienvenue\'', () => {
    //Arrange
    const service = new TranslateService(mockHttpClient);
    mockHttpClient.get.and.returnValue(of({'Welcome':'Bienvenue'}));

    //Act
    service.use('fr');

    //Assert
    expect(service.data['Welcome']).toBe('Bienvenue');
  });

});
