import {TestBed, inject} from '@angular/core/testing';
import {MembersTableService} from './members-table.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Member} from '../models/member.model';
import {Constants} from '../utils/utils.constants';
import {PageResponse} from '../models/page-response.model';

describe('MembersTableService', () => {
    let mockHttpClient;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [MembersTableService],
            imports: [HttpClientTestingModule]
        });

        mockHttpClient = TestBed.get(HttpTestingController);

        spyOn(localStorage, 'getItem').and.callFake(mockLocalStorage.getItem);
        spyOn(localStorage, 'setItem').and.callFake(mockLocalStorage.setItem);
        spyOn(localStorage, 'clear').and.callFake(mockLocalStorage.clear);
    });

    afterEach(() =>  mockHttpClient.verify());

    it('should create an instance of MembersTableService', inject([MembersTableService], (service: MembersTableService) => {
        expect(service).toBeTruthy();
    }));

    it(`should check if 'memberServiceUrl = Constants.MEMBER_CONTROLLER' and 'memberPositionServiceUrl = Constants.MEMBER_POSITION_CONTROLLER'`, () => {
        const {memberServiceUrl, memberPositionServiceUrl} = TestBed.get(MembersTableService);
        expect(memberServiceUrl).toBe(Constants.MEMBER_CONTROLLER);
        expect(memberPositionServiceUrl).toBe(Constants.MEMBER_POSITION_CONTROLLER);
    });

    it('should get all ACTIVE members calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const activeMembers = dummyMembers.filter(({flag}) => flag === 1);
        const activeMembersURL = Constants.MEMBER_CONTROLLER + Constants.GET_ACTIVE_MEMBERS;
        //Act
        service.getActiveMembers().subscribe(members => {
            expect(members.length).toBe(activeMembers.length);
            expect(members).toEqual(activeMembers)
        });
        //Assert
        const req = mockHttpClient.expectOne(activeMembersURL);
        expect(req.request.url).toBe(activeMembersURL);
        expect(req.request.method).toBe('GET');
        expect(req.request['withCredentials']).toBeTruthy();

        req.flush(activeMembers);
    }));

    it('should get the 1st page of max 2 elements of all ACTIVE members calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const activeMembers = dummyMembers.filter(({flag}) => flag === 1);
        const activeMembersPageURL = `${Constants.MEMBER_CONTROLLER}${Constants.GET_PAGINATED_ACTIVE_MEMBERS}`;
        const requestedPageNumber = 1;
        const requestedElementsSize = 2;
        const paginatedResponseFromBackend = {
            first: [...activeMembers].slice(0, requestedElementsSize),
            second: 3
        } as PageResponse<Member>;
        localStorage.setItem('sortOrderActiveMembers', 'ascendingStaffNumber');
        //Act
        service.getActiveMembersPage(requestedPageNumber, requestedElementsSize).subscribe((members: PageResponse<Member>) => {
            //Assert
            expect(members.first.length).toBe(requestedElementsSize);
            expect(members).toEqual(paginatedResponseFromBackend);
            expect(localStorage.getItem).toHaveBeenCalledWith(Constants.SORT_ORDER_ACTIVE_MEMBERS);
        });

        const matchingRequests = mockHttpClient.match(request =>
            request.url.match(activeMembersPageURL)
            && request.method === 'GET'
            && !!request['withCredentials']
            && +request.params.map.get('page') === (requestedPageNumber - 1) // -1 because backend api handles pages as 0 based.
            && +request.params.map.get('size') === requestedElementsSize
            && request.params.map.get('sortBy')[0] === 'staffNumber'
            && request.params.map.get('direction')[0] === 'ASC'
        );

        matchingRequests.forEach(r => r.flush(paginatedResponseFromBackend));
        localStorage.clear();
    }));

    it('should get the 1st page of max 5 elements of all INACTIVE members calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const inactiveMembers = dummyMembers.filter(({flag}) => flag === 0);
        const inactiveMembersPageURL = `${Constants.MEMBER_CONTROLLER}${Constants.GET_PAGINATED_INACTIVE_MEMBERS}`;
        const requestedPageNumber = 1;
        const requestedElementsSize = 5;
        const paginatedResponseFromBackend = {
            first: [...inactiveMembers].slice(0, requestedElementsSize),
            second: inactiveMembers.length
        } as PageResponse<Member>;
        localStorage.setItem('sortOrderInactiveMembers', 'descendingLastName');
        //Act
        service.getInactiveMembersPage(requestedPageNumber, requestedElementsSize).subscribe((members: PageResponse<Member>) => {
            //Assert
            expect(members.first.length).toBeLessThanOrEqual(requestedElementsSize);
            expect(members).toEqual(paginatedResponseFromBackend);
            expect(localStorage.getItem).toHaveBeenCalledWith(Constants.SORT_ORDER_INACTIVE_MEMBERS);
        });

        const matchingRequests = mockHttpClient.match(request =>
            request.url.match(inactiveMembersPageURL)
            && request.method === 'GET'
            && !!request['withCredentials']
            && +request.params.map.get('page') === (requestedPageNumber - 1) // -1 because backend api handles pages as 0 based.
            && +request.params.map.get('size') === requestedElementsSize
            && request.params.map.get('sortBy')[0] === 'lastName'
            && request.params.map.get('direction')[0] === 'DESC'
        );

        matchingRequests.forEach(r => r.flush(paginatedResponseFromBackend));
        localStorage.clear();
    }));

    it('should get all INACTIVE members calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const inactiveMembers = dummyMembers.filter(({flag}) => flag === 0);
        const inactiveMembersURL = Constants.MEMBER_CONTROLLER + Constants.GET_INACTIVE_MEMBERS;
        //Act
        service.getInactiveMembers().subscribe(members => {
            expect(members.length).toBe(inactiveMembers.length);
            expect(members).toEqual(inactiveMembers)
        });
        //Assert
        const req = mockHttpClient.expectOne(inactiveMembersURL);
        expect(req.request.url).toBe(inactiveMembersURL);
        expect(req.request.method).toBe('GET');
        expect(req.request['withCredentials']).toBeTruthy();

        req.flush(inactiveMembers);
    }));

    it('should DEACTIVATE the given member providing his id', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const [, memberToBeDeactivate,] = dummyMembers;
        const deactivateMemberURL = Constants.MEMBER_CONTROLLER + Constants.DEACTIVATE_MEMBER;
        //Act
        service.deactivateMember(memberToBeDeactivate.id).subscribe();
        //Assert
        const req = mockHttpClient.expectOne(deactivateMemberURL);
        expect(req.request.url).toBe(deactivateMemberURL);
        expect(req.request.method).toBe('PUT');
        expect(req.request['withCredentials']).toBeTruthy();
        expect(req.request.body).toBe(memberToBeDeactivate.id);
    }));

    it('should REACTIVATE the given member calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const [memberToBeReactivate] = dummyMembers.filter(({flag}) => flag === 0);
        const updateMemberURL = Constants.MEMBER_CONTROLLER + Constants.UPDATE_MEMBER;
        //Act
        service.reactivateMember(memberToBeReactivate).subscribe(reactivatedMember => {
            expect(reactivatedMember.flag).toBe(1);
            expect(reactivatedMember).toEqual(memberToBeReactivate);
        });
        //Assert
        const req = mockHttpClient.expectOne(updateMemberURL);
        expect(req.request.url).toBe(updateMemberURL);
        expect(req.request.method).toBe('PUT');
        expect(req.request['withCredentials']).toBeTruthy();
        expect(req.request.body['flag']).toBe(1);

        req.flush({...memberToBeReactivate})
    }));

    it('should ADD the given member calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const newMember = {
            staffNumber: '2284075',
            lastName: 'ANGHELESCU',
            firstName: 'Paulina',
            flag: 1,
            technologyId: 4,
            technologyDescription: 'JAVASCRIPT',
            comment: 'very motivated'
        };
        //Act
        service.addMember(newMember).subscribe((addedMember: Member) => {
            //Assert
            const {id: addedMemberId, ...addedMemberWithoutIdProperty} = addedMember;
            expect(addedMemberId).toBeTruthy();
            expect(newMember).toEqual(addedMemberWithoutIdProperty);
        });
        //Assert
        const req = mockHttpClient.expectOne(Constants.MEMBER_CONTROLLER);
        expect(req.request.url).toBe(Constants.MEMBER_CONTROLLER);
        expect(req.request.method).toBe('POST');
        expect(req.request['withCredentials']).toBeTruthy();
        expect(req.request.body).toBe(newMember);

        req.flush({id: 75, ...newMember})
    }));

    it('should perform EDIT on the given member calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const editedMember = {...dummyMembers[2], technologyId: 4, technologyDescription: 'JAVASCRIPT'};
        const updateMemberURL = Constants.MEMBER_CONTROLLER + Constants.UPDATE_MEMBER;
        //Act
        service.reactivateMember(editedMember).subscribe();
        //Assert
        const req = mockHttpClient.expectOne(updateMemberURL);
        expect(req.request.url).toBe(updateMemberURL);
        expect(req.request.method).toBe('PUT');
        expect(req.request['withCredentials']).toBeTruthy();
        expect(req.request.body).toBe(editedMember);
    }));

    it('should ADD the given Member Position calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const {id, ...newMemberPositionToAdd} = {
            ...dummyMemberPositions[0],
            memberDTO: {...dummyMembers.find(({id}) => id === 69)}
        };
        //Act
        service.addMemberPosition(newMemberPositionToAdd).subscribe((addedMemberPosition: any) => {
            //Assert
            const {id: addedMemberPositionId, ...addedMemberPositionWithoutIdProperty} = addedMemberPosition;
            expect(addedMemberPositionId).toBeTruthy();
            expect(newMemberPositionToAdd).toEqual(addedMemberPositionWithoutIdProperty);
        });
        //Assert
        const req = mockHttpClient.expectOne(Constants.MEMBER_POSITION_CONTROLLER);
        expect(req.request.url).toBe(Constants.MEMBER_POSITION_CONTROLLER);
        expect(req.request.method).toBe('POST');
        expect(req.request['withCredentials']).toBeTruthy();
        expect(req.request.body).toBe(newMemberPositionToAdd);

        req.flush({id: 25, ...newMemberPositionToAdd})
    }));

    it(`should get all member's positions calling the right endpoint with the member with id 35`, inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const memberWithId35: Member = dummyMembers.find(({id}) => id === 35);
        const positionsForMemberWithId35 = dummyMemberPositions.filter(({memberDTO: {id}}) => id === memberWithId35.id);
        const memberPositionsURL = Constants.MEMBER_POSITION_CONTROLLER + Constants.FIND_BY_ID_MEMBER_POSITION;
        //Act
        service.getMemberPositions(memberWithId35).subscribe(memberPositions => {
            expect(memberPositions.length).toBe(positionsForMemberWithId35.length);
            expect(memberPositions).toEqual(positionsForMemberWithId35 as any);
        });
        //Assert
        const req = mockHttpClient.expectOne(memberPositionsURL + memberWithId35.id);
        expect(req.request.url).toBe(memberPositionsURL + 35);
        expect(req.request.method).toBe('GET');
        expect(req.request['withCredentials']).toBeTruthy();

        req.flush(positionsForMemberWithId35);
    }));

    it('should perform EDIT on the given member position calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const editedMemberPosition = {...dummyMemberPositions[0], percentId: 8, percentDescription: '80'};
        const editMemberPositionsURL = Constants.MEMBER_POSITION_CONTROLLER + Constants.UPDATE_MEMBER_POSITION;
        //Act
        service.editMemberPositions(editedMemberPosition).subscribe();
        //Assert
        const req = mockHttpClient.expectOne(editMemberPositionsURL);
        expect(req.request.url).toBe(editMemberPositionsURL);
        expect(req.request.method).toBe('PUT');
        expect(req.request['withCredentials']).toBeTruthy();
        expect(req.request.body).toEqual(editedMemberPosition);
    }));

    it('should check BackendHealth calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const fakeResponseFromBackend = 'Backend is alive';
        const checkBackendHealthURL = Constants.MEMBER_CONTROLLER + '/health';
        //Act
        service.checkBackendHealth().subscribe(response => {
            expect(response).toEqual(fakeResponseFromBackend);
        });
        //Assert
        const req = mockHttpClient.expectOne(checkBackendHealthURL);
        expect(req.request.url).toBe(checkBackendHealthURL);
        expect(req.request.method).toBe('GET');
        expect(req.request['responseType']).toBe('text');

        req.flush(fakeResponseFromBackend);
    }));

    it('should perform DELETE on the given member position calling the right endpoint', inject([MembersTableService], (service: MembersTableService) => {
        //Arrange;
        const memberPositionToDelete = {...dummyMemberPositions[1]};
        const deleteMemberPositionsURL = Constants.MEMBER_POSITION_CONTROLLER + Constants.DELETE_MEMBER_POSITION;
        //Act
        service.deleteMemberPosition(memberPositionToDelete.id).subscribe();
        //Assert
        const req = mockHttpClient.expectOne(deleteMemberPositionsURL + memberPositionToDelete.id);
        expect(req.request.url).toBe(deleteMemberPositionsURL + 15);
        expect(req.request.method).toBe('DELETE');
        expect(req.request['withCredentials']).toBeTruthy();
    }));
});

const mockLocalStorage = {
    store: {},
    getItem: (key: string): string => key in mockLocalStorage.store ? mockLocalStorage.store[key] : null,
    setItem: (key: string, value: string) => mockLocalStorage.store[key] = `${value}`,
    clear: () => mockLocalStorage.store = {}
};
const dummyMembers: Member[] = [
    {
        id: 11,
        staffNumber: '22489075',
        lastName: 'POPESCU',
        firstName: 'Stefania',
        flag: 1,
        technologyId: 5,
        technologyDescription: 'JAVA',
        comment: null
    },
    {
        id: 23,
        staffNumber: '23068932',
        lastName: 'ENESCU',
        firstName: 'Marius',
        flag: 0,
        technologyId: 3,
        technologyDescription: 'SALESFORCE',
        comment: null
    },
    {
        id: 35,
        staffNumber: '23005800',
        lastName: 'STANCIU',
        firstName: 'Ionela',
        flag: 1,
        technologyId: 5,
        technologyDescription: 'JAVA',
        comment: null
    },
    {
        id: 47,
        staffNumber: '22689931',
        lastName: 'STOIAN',
        firstName: 'Sorin',
        flag: 1,
        technologyId: 1,
        technologyDescription: 'PHP',
        comment: null
    },
    {
        id: 69,
        staffNumber: '23562280',
        lastName: 'MITRESCU',
        firstName: 'Alexia',
        flag: 0,
        technologyId: 2,
        technologyDescription: 'BUSINESS ANALYST',
        comment: null
    }
];
const dummyMemberPositions = [
    {
        id: 12,
        percentId: 10,
        projectPositionDTO: {
            id: 6,
            positionId: 6,
            numberPositions: 2,
            percentId: 10,
            projectDTO: {
                id: 13,
                projectCode: 'PISTE-18-12-001',
                projectName: 'IKEA FR',
                statusId: 3,
                percentId: 10,
                statusDescription: 'TERMINATED',
                percentDescription: '100',
                startDateCalendarDTO: {
                    id: 'FY19-P01-W01',
                    fiscalYear: 2019,
                    period: 1,
                    week: 1,
                    bop: '2018-09-23',
                    eop: '2018-09-29',
                    quarter: 'Q1'
                },
                endDateCalendarDTO: {
                    id: 'FY19-P04-W15',
                    fiscalYear: 2019,
                    period: 4,
                    week: 15,
                    bop: '2018-12-30',
                    eop: '2019-01-05',
                    quarter: 'Q2'
                }
            },
            startDateCalendarDTO: {
                id: 'FY19-P01-W02',
                fiscalYear: 2019,
                period: 1,
                week: 2,
                bop: '2018-09-30',
                eop: '2018-10-06',
                quarter: 'Q1'
            },
            endDateCalendarDTO: {
                id: 'FY19-P04-W14',
                fiscalYear: 2019,
                period: 4,
                week: 14,
                bop: '2018-12-23',
                eop: '2018-12-29',
                quarter: 'Q2'
            },
            positionDescription: 'Salesforce',
            percentDescription: '100',
            projectName: 'IKEA FR'
        },
        memberDTO: {...dummyMembers.find(({id}) => id === 23)},
        startDateCalendarDTO: {
            id: 'FY19-P01-W02',
            fiscalYear: 2019,
            period: 1,
            week: 2,
            bop: '2018-09-30',
            eop: '2018-10-06',
            quarter: 'Q1'
        },
        endDateCalendarDTO: {
            id: 'FY19-P04-W14',
            fiscalYear: 2019,
            period: 4,
            week: 14,
            bop: '2018-12-23',
            eop: '2018-12-29',
            quarter: 'Q2'
        },
        percentDescription: '100'
    },
    {
        id: 15,
        percentId: 10,
        projectPositionDTO: {
            id: 8,
            positionId: 1,
            numberPositions: 2,
            percentId: 10,
            projectDTO: {
                id: 14,
                projectCode: 'PISTE-18-12-006',
                projectName: 'THALES JAVA ADVANCED',
                statusId: 1,
                percentId: 8,
                statusDescription: 'In Negotiation',
                percentDescription: '80',
                startDateCalendarDTO: {
                    id: 'FY19-P03-W11',
                    fiscalYear: 2019,
                    period: 3,
                    week: 11,
                    bop: '2018-12-02',
                    eop: '2018-12-08',
                    quarter: 'Q1'
                },
                endDateCalendarDTO: {
                    id: 'FY19-P12-W52',
                    fiscalYear: 2019,
                    period: 12,
                    week: 52,
                    bop: '2019-09-15',
                    eop: '2019-09-21',
                    quarter: 'Q4'
                }
            },
            startDateCalendarDTO: {
                id: 'FY19-P03-W11',
                fiscalYear: 2019,
                period: 3,
                week: 11,
                bop: '2018-12-02',
                eop: '2018-12-08',
                quarter: 'Q1'
            },
            endDateCalendarDTO: {
                id: 'FY19-P12-W52',
                fiscalYear: 2019,
                period: 12,
                week: 52,
                bop: '2019-09-15',
                eop: '2019-09-21',
                quarter: 'Q4'
            },
            positionDescription: 'Java Developer',
            percentDescription: '100',
            projectName: 'THALES JAVA ADVANCED'
        },
        memberDTO: {...dummyMembers.find(({id}) => id === 35)},
        startDateCalendarDTO: {
            id: 'FY19-P03-W11',
            fiscalYear: 2019,
            period: 3,
            week: 11,
            bop: '2018-12-02',
            eop: '2018-12-08',
            quarter: 'Q1'
        },
        endDateCalendarDTO: {
            id: 'FY19-P12-W52',
            fiscalYear: 2019,
            period: 12,
            week: 52,
            bop: '2019-09-15',
            eop: '2019-09-21',
            quarter: 'Q4'
        },
        percentDescription: '100'
    },
    {
        id: 18,
        percentId: 10,
        projectPositionDTO: {
            id: 12,
            positionId: 1,
            numberPositions: 2,
            percentId: 10,
            projectDTO: {
                id: 4,
                projectCode: 'PISTE-18-12-012',
                projectName: 'DECATHLON JAVA TEAM1',
                statusId: 2,
                percentId: 10,
                statusDescription: 'VALID',
                percentDescription: '100',
                startDateCalendarDTO: {
                    id: 'FY19-P03-W10',
                    fiscalYear: 2019,
                    period: 3,
                    week: 10,
                    bop: '2018-11-25',
                    eop: '2018-12-01',
                    quarter: 'Q1'
                },
                endDateCalendarDTO: {
                    id: 'FY19-P12-W52',
                    fiscalYear: 2019,
                    period: 12,
                    week: 52,
                    bop: '2019-09-15',
                    eop: '2019-09-21',
                    quarter: 'Q4'
                }
            },
            startDateCalendarDTO: {
                id: 'FY19-P03-W10',
                fiscalYear: 2019,
                period: 3,
                week: 10,
                bop: '2018-11-25',
                eop: '2018-12-01',
                quarter: 'Q1'
            },
            endDateCalendarDTO: {
                id: 'FY19-P12-W52',
                fiscalYear: 2019,
                period: 12,
                week: 52,
                bop: '2019-09-15',
                eop: '2019-09-21',
                quarter: 'Q4'
            },
            positionDescription: 'Java Developer',
            percentDescription: '100',
            projectName: 'DECATHLON JAVA TEAM1'
        },
        memberDTO: {...dummyMembers.find(({id}) => id === 35)},
        startDateCalendarDTO: {
            id: 'FY19-P03-W10',
            fiscalYear: 2019,
            period: 3,
            week: 10,
            bop: '2018-11-25',
            eop: '2018-12-01',
            quarter: 'Q1'
        },
        endDateCalendarDTO: {
            id: 'FY19-P12-W52',
            fiscalYear: 2019,
            period: 12,
            week: 52,
            bop: '2019-09-15',
            eop: '2019-09-21',
            quarter: 'Q4'
        },
        percentDescription: '100'
    }
];
