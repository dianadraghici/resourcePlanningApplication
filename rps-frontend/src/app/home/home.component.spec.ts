import { Component, Pipe, PipeTransform } from "@angular/core";
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { By } from "@angular/platform-browser";

describe('HomeComponent', () => {
  let fixture: ComponentFixture<HomeComponent>;

  @Component({ selector: 'app-menu', template: '' })
  class MenuComponentStub {}

  @Pipe({name: 'translate'})
  class TranslatePipeStub implements PipeTransform {
    transform(value: string): string {
      return value;
    }
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeComponent , MenuComponentStub, TranslatePipeStub]
    });
    fixture = TestBed.createComponent(HomeComponent);
  });

  it('should create an instance of HomeComponent', () => {
    const component: HomeComponent = fixture.componentInstance;
    expect(component).toBeTruthy();
  });

  it(`should display 'JAVA TEAM' in a div tag`, () => {
    //Act
    fixture.detectChanges();
    const {nativeElement: nativeEl, name} = fixture.debugElement.query(By.css('.text'));
    //Assert
    expect(name).toBe('div');
    expect(nativeEl.textContent).toBe('JAVA TEAM');
  });

  it(`should the child menu component have a html selector 'app-menu'`, () => {
    //Act
    fixture.detectChanges();
    //Assert
    const menuComponent = fixture.debugElement.query(By.directive(MenuComponentStub));
    const {selector} = menuComponent.injector.get(MenuComponentStub).constructor['__annotations__'][0];
    expect(selector).toBe('app-menu');
  });
});
