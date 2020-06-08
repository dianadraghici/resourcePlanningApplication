import {ComponentFixture, TestBed } from '@angular/core/testing';
import {UserComponent } from './user.component';
import {Component} from "@angular/core";
import {UserService} from "./user.service";
import {of} from "rxjs";
import {By} from "@angular/platform-browser";

describe('UserComponent', () => {

  @Component({
    selector: 'app-menu',
    template: ''
  })
  class MenuComponentStub{}

  let userServiceMock;
  const DUMMY_USERS: any[] = [
    {id: 1, email: 'mail1@test.com', password: '123' },
    {id: 2, email: 'mail2@test.com', password: '123' },
    {id: 3, email: 'mail3@test.com', password: '123' },
  ];

  beforeEach(() => {
    userServiceMock = jasmine.createSpyObj(['getUsers']);
    TestBed.configureTestingModule({
      declarations: [UserComponent, MenuComponentStub],
      providers: [ {provide: UserService, useValue: userServiceMock}],
    });
  });

  it('should have a list of users with the given size',() => {
    //arrange
    userServiceMock.getUsers.and.returnValue(of(DUMMY_USERS));
    const fixture: ComponentFixture<UserComponent> = TestBed.createComponent(UserComponent);
    const component: UserComponent = fixture.componentInstance;

    //act
    fixture.detectChanges();
    const result = component['users'];

    //assert
    expect(result.length).toBe(3);
    expect(result).toEqual(DUMMY_USERS);
  });

  it('should have a div with class \'col-md-6\'',() => {
    //arrange
    userServiceMock.getUsers.and.returnValue(of(DUMMY_USERS));
    const fixture: ComponentFixture<UserComponent> = TestBed.createComponent(UserComponent);
    const debugElement = fixture.debugElement;

    //act
    fixture.detectChanges();
    const result: HTMLElement = debugElement.query(By.css('.col-md-6')).nativeElement;

    //assert
    expect(result).toBeTruthy();
  });

  it('should have a app-menu template',() => {
    //arrange
    userServiceMock.getUsers.and.returnValue(of(DUMMY_USERS));
    const fixture: ComponentFixture<UserComponent> = TestBed.createComponent(UserComponent);
    const debugElement = fixture.debugElement;

    //act
    fixture.detectChanges();
    const result: HTMLElement = debugElement.query(By.css('app-menu')).nativeElement;
    const result2 = debugElement.query(By.directive(MenuComponentStub)).injector.get(MenuComponentStub);
    expect(result2).toBeTruthy();

    //assert
    expect(result).toBeTruthy();
  });

});



