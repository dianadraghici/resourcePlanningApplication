import {TranslatePipe} from './translate.pipe';

describe('TranslatePipe', () => {
    let mockTranslateService;

    beforeEach(() => {
        mockTranslateService = jasmine.createSpyObj(['data']);
    });

    it('should create an instance of TranslatePipe', () => {
        //Arrange
        const pipe = new TranslatePipe(mockTranslateService);

        //Assert
        expect(pipe).toBeTruthy();
    });

    it('should not found a translation and return Welcome', () => {
        //Arrange
        const pipe = new TranslatePipe(mockTranslateService);

        //Act
        const result = pipe.transform('Welcome');

        //Assert
        expect(result).toBe('Welcome');
    });

    it('should translate Welcome to Bienvenue', () => {
        //Arrange
        const pipe = new TranslatePipe(mockTranslateService);
        mockTranslateService.data = {'Welcome': 'Bienvenue'};

        //Act
        const result = pipe.transform('Welcome');

        //Assert
        expect(result).toBe('Bienvenue');
    });

});
