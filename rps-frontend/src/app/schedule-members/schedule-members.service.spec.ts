import { TestBed, inject } from '@angular/core/testing';

import { ScheduleMembersService } from './schedule-members.service';

describe('ScheduleMembersService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScheduleMembersService]
    });
  });

  // it('should be created', inject([ScheduleMembersService], (service: ScheduleMembersService) => {
  //   expect(service).toBeTruthy();
  // }));
});
