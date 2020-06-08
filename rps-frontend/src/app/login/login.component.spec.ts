import {ComponentFixture, TestBed} from '@angular/core/testing';
import {LoginComponent} from './login.component';
import {Pipe, PipeTransform} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {SessionService} from "./session.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {By} from "@angular/platform-browser";
import {TranslateService} from "../utils/translate.service";
import {Login} from "../models/login.model";
import {of, throwError} from "rxjs";

describe('LoginComponent', () => {
    let mockRouter;
    let mockSessionService;
    let mockTranslateService;
    const mockLocalStorage = {
        store: {},
        getItem: (key: string): string => {
            return key in mockLocalStorage.store ? mockLocalStorage.store[key] : null;
        },
        setItem: (key: string, value: string) => {
            mockLocalStorage.store[key] = `${value}`;
        },
        removeItem: (key: string) => {
            delete mockLocalStorage.store[key];
        }
    };

    @Pipe({name: 'translate'})
    class TranslatePipeStub implements PipeTransform {
        transform(value: string): string {
            return value;
        }
    }

    beforeEach(() => {
        mockRouter = jasmine.createSpyObj(['navigateByUrl']);
        mockSessionService = jasmine.createSpyObj(['login']);
        mockTranslateService = jasmine.createSpyObj(['use']);

        TestBed.configureTestingModule({
            declarations: [LoginComponent, TranslatePipeStub],
            providers: [
                {provide: ActivatedRoute, useValue: {snapshot: {queryParams: {}}}},
                {provide: Router, useValue: mockRouter},
                {provide: SessionService, useValue: mockSessionService},
                {provide: TranslateService, useValue: mockTranslateService},
            ],
            imports: [FormsModule, HttpClientTestingModule]
        });

        spyOn(localStorage, 'getItem').and.callFake(mockLocalStorage.getItem);
        spyOn(localStorage, 'setItem').and.callFake(mockLocalStorage.setItem);
        spyOn(localStorage, 'removeItem').and.callFake(mockLocalStorage.removeItem);
    });

    beforeEach(() => localStorage.removeItem('lang'));

    it('should create an instance of LoginComponent', () => {
        const component = TestBed.createComponent(LoginComponent).componentInstance;
        expect(component).toBeTruthy();
    });

    it(`should check lang and returnUrl fields when user first try to log in`, () => {
        //Arrange
        const component: LoginComponent = TestBed.createComponent(LoginComponent).componentInstance;
        mockTranslateService.use.and.callFake(() => localStorage.setItem('lang', 'fr'));
        //Act
        component.ngOnInit();
        //Assert
        expect(component.lang).toBeTruthy();
        expect(component.returnUrl).toEqual('/home');
        expect(mockTranslateService.use).toHaveBeenCalledTimes(1);
    });

    it('should returnUrl be the url where the user were before logged out', () => {
        //Arrange
        TestBed.overrideProvider(ActivatedRoute, {useValue: {snapshot: {queryParams: {returnUrl: '/projects'}}}});
        const component: LoginComponent = TestBed.createComponent(LoginComponent).componentInstance;
        //Act
        component.ngOnInit();
        //Assert
        expect(component.returnUrl).toEqual('/projects');
    });

    it(`should set the lang to be 'en'`, () => {
        //Arrange
        const fixture: ComponentFixture<LoginComponent> = TestBed.createComponent(LoginComponent);
        const component: LoginComponent = fixture.componentInstance;
        fixture.detectChanges();
        const langEnBTN = fixture.debugElement.queryAll(By.css('button')).filter(debugEl => debugEl.nativeElement.textContent == 'EN')[0];
        mockTranslateService.use.and.callFake(() => localStorage.setItem('lang', langEnBTN.nativeElement.textContent.toLocaleLowerCase()));
        // //ACT
        langEnBTN.triggerEventHandler('click', null);
        //Assert
        expect(component.lang).toBe('en');
        expect(mockTranslateService.use).toHaveBeenCalled();
    });

    it('should check the login field binding', () => {
        //Arrange
        const fixture: ComponentFixture<LoginComponent> = TestBed.createComponent(LoginComponent);
        const component: LoginComponent = fixture.componentInstance;
        const userToLogin = {username: 'gigel.popescu', password: 'abcd1545'};
        //Act
        fixture.detectChanges();
        const usernameInput = fixture.debugElement.query(By.css('#username')).nativeElement;
        const passwordInput = fixture.debugElement.query(By.css('#password')).nativeElement;
        fixture.whenStable().then(() => {
            usernameInput.value = userToLogin.username;
            passwordInput.value = userToLogin.password;
            usernameInput.dispatchEvent(new Event('input'));
            passwordInput.dispatchEvent(new Event('input'));
            //Assert
            expect(component.login.user).toEqual(userToLogin.username);
            expect(component.login.credential).toEqual(userToLogin.password);
        });
    });

    it('should login a dummy user', () => {
        //Arrange
        const fixture: ComponentFixture<LoginComponent> = TestBed.createComponent(LoginComponent);
        const component: LoginComponent = fixture.componentInstance;
        mockSessionService.login.and.returnValue(of(true));
        const userToLogin: Login = {user: 'gigel.popescu', credential: 'abcd1545'};
        fixture.detectChanges();
        const formLogin = fixture.debugElement.query(By.css('#login-form'));
        const alertElement = fixture.debugElement.query(By.css('#alerta')).nativeElement;
        const eventRaised = {
            preventDefault: () => {},
            target: {elements: [{value: userToLogin.user}, {value: userToLogin.credential}]}
        };
        //Act
        formLogin.triggerEventHandler('submit', eventRaised);
        //Assert
        expect(component.login.user).toEqual(userToLogin.user);
        expect(component.login.credential).toEqual(userToLogin.credential);
        expect(mockRouter.navigateByUrl).toHaveBeenCalledTimes(1);
        expect(mockRouter.navigateByUrl).toHaveBeenCalledWith('/home');
        expect(alertElement.style.display).toEqual('none');
    });

    it('should simulate an error event happened when login() was called', () => {
        //Arrange
        const fixture: ComponentFixture<LoginComponent> = TestBed.createComponent(LoginComponent);
        mockSessionService.login.and.returnValue(throwError(new Error()));
        fixture.detectChanges();
        const formLogin = fixture.debugElement.query(By.css('#login-form'));
        const alertElement = fixture.debugElement.query(By.css('#alerta')).nativeElement;
        const eventRaised = {
            preventDefault: () => {},
            target: {elements: [{value: 'gigel.popescu'}, {value: 'abcd1545'}]}
        };
        //Act
        formLogin.triggerEventHandler('submit', eventRaised);
        //Assert
        expect(alertElement.style.display).toEqual('block');
    });

});
