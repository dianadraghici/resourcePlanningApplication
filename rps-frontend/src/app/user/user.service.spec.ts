import { UserService } from './user.service';
import {TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Constants} from "../utils/utils.constants";

describe('UserService', () => {
    let httpClientMock;

    const DUMMY_USERS: any[] = [
        {id: 1, email: 'mail1@test.com', password: '123' },
        {id: 2, email: 'mail2@test.com', password: '123' },
        {id: 3, email: 'mail3@test.com', password: '123' },
    ];

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [UserService],
            imports: [HttpClientTestingModule]
        });
        httpClientMock = TestBed.get(HttpTestingController);
    });

    it('should create an instance of UserService', () => {
        const service =  TestBed.get(UserService);

        expect(service).toBeTruthy();
    });

    it('should return a list of users', () => {
        const service =  TestBed.get(UserService);

        service.getUsers().subscribe( userList => {
            expect(userList).toEqual(DUMMY_USERS);
        });

        const result = httpClientMock.expectOne(Constants.USER_CONTROLLER);
        expect(result.request.method).toBe('GET');
        expect(result.request['withCredentials']).toBeTruthy();

        result.flush(DUMMY_USERS);
        httpClientMock.verify();
    });

});

