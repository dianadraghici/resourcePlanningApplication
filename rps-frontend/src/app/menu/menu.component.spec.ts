import {Directive, Input, Pipe, PipeTransform} from "@angular/core";
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {Router} from "@angular/router";
import { MenuComponent } from './menu.component';
import { TranslateService } from "../utils/translate.service";
import { SessionService } from "../login/session.service";
import {NgbDropdown, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {By} from "@angular/platform-browser";
import {of} from "rxjs";

describe('MenuComponent', () => {
  let fixture: ComponentFixture<MenuComponent>;
  const mockSessionService = jasmine.createSpyObj(['logout']);
  const mockTranslateService = jasmine.createSpyObj(['use']);
  const mockRouter = jasmine.createSpyObj(['navigate']);
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

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MenuComponent , TranslatePipeStub, RouterLinkActiveOptionsStub ],
      providers: [
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [NgbModule],
    });
    spyOn(localStorage, 'getItem').and.callFake(mockLocalStorage.getItem);
    spyOn(localStorage, 'setItem').and.callFake(mockLocalStorage.setItem);
    spyOn(localStorage, 'removeItem').and.callFake(mockLocalStorage.removeItem);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuComponent);
    localStorage.removeItem('lang');
  });

  it('should create an instance of MenuComponent', () => {
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should the language FR be active if none selected', () => {
    //Arrange
    const menuComponent: MenuComponent = fixture.componentInstance;
    mockTranslateService.use.and.callFake(() => localStorage.setItem('lang','fr'));
    //ACT
    menuComponent.ngOnInit();
    //Assert
    // expect(mockTranslateService.use).toHaveBeenCalledWith('fr');
    expect(menuComponent.lang).toBe('fr');
  });

  it('should test the setLang method', () => {
    //Arrange
    const menuComponent: MenuComponent = fixture.componentInstance;
    mockTranslateService.use.and.callFake(() => localStorage.setItem('lang', 'en'));
    //ACT
    const newLang = menuComponent.setLang('en');
    //Assert
    expect(newLang).toBe('en');
    expect(menuComponent.lang).toBe(newLang);
  });

  it('should activate the language RO', () => {
    //Arrange
    const menuComponent: MenuComponent = fixture.componentInstance;
    mockTranslateService.use.and.callFake(() => localStorage.setItem('lang','ro'));
    fixture.detectChanges();
    const langRoBTN = fixture.debugElement.queryAll(By.css('button')).filter(debugEl => debugEl.nativeElement.textContent == 'RO')[0];
    //ACT
    langRoBTN.triggerEventHandler('click',null);
    fixture.detectChanges();
    //Assert
    expect(menuComponent.lang).toBe('ro');
    expect(langRoBTN.nativeElement.getAttributeNode('class').value).toContain('language-button-active');
    expect(langRoBTN.nativeElement.textContent).toBe('RO');
  });

  it(`should navigate to 'login' when the logout button is clicked`, () => {
    //Arrange
    mockSessionService.logout.and.returnValue(of(true));
    //ACT
    fixture.nativeElement.querySelector('.logoutBtn').click();
    //Assert
    expect(mockRouter.navigate).toHaveBeenCalledWith(['login']);
  });

  it(`should the routerLinkActiveOptions of the route '/members-table' be {exact:true}`, () => {
    //Arrange
    fixture.detectChanges();
    const membersTableLink = fixture.debugElement.query(By.css('#dropdownBasic2'));
    //ACT
    membersTableLink.triggerEventHandler('click',null);
    //Assert
    expect(membersTableLink.attributes['routerLink']).toBe('/members-table');
    expect(membersTableLink.injector.get(RouterLinkActiveOptionsStub).isExact).toBe(true);
  });

  it(`should the members-table dropdown be open on mouseover's event`, () => {
    //Arrange
    fixture.detectChanges();
    const membersTableDropdown = fixture.debugElement.query(By.css('#dropdownBasic2'));
    const graphicsDropdown = fixture.debugElement.query(By.css('#dropdownBasic3'));
    //Act
    membersTableDropdown.triggerEventHandler('mouseover', {stopPropagation: () => {} });
    //Assert
    expect(membersTableDropdown.injector.get(NgbDropdown).isOpen()).toBe(true);
    expect(graphicsDropdown.injector.get(NgbDropdown).isOpen()).toBe(false);
  });

  it(`should the Graphics dropdown be open on mouseover's event`, () => {
    //Arrange
    fixture.detectChanges();
    const graphicsDropdown = fixture.debugElement.query(By.css('#dropdownBasic3'));
    const membersTableDropdown = fixture.debugElement.query(By.css('#dropdownBasic2'));
    //Act
    graphicsDropdown.triggerEventHandler('mouseover', {stopPropagation: () => {} });
    //Assert
    expect(graphicsDropdown.injector.get(NgbDropdown).isOpen()).toBe(true);
    expect(membersTableDropdown.injector.get(NgbDropdown).isOpen()).toBe(false);
  });
});

@Pipe({name: 'translate'})
class TranslatePipeStub implements PipeTransform {
  transform(value: string): string {
    return value;
  }
}

@Directive({ selector:'[routerLinkActiveOptions]', host: {'(click)':'onClicked()'}})
class RouterLinkActiveOptionsStub {
  @Input('routerLinkActiveOptions') activeOptions: any;
  isExact: boolean;
  onClicked(){
    this.isExact = !!this.activeOptions.exact;
  }
}
