import { Component } from "@angular/core";
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GraphicsComponent } from './graphics.component';
import { By } from "@angular/platform-browser";

describe('GraphicsComponent', () => {
  let fixture: ComponentFixture<GraphicsComponent>;

  @Component({ selector: 'app-menu', template: '' })
  class MenuComponentStub {}

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ GraphicsComponent , MenuComponentStub]
    });
    fixture = TestBed.createComponent(GraphicsComponent);
  });

  it('should create an instance of GraphicsComponent', () => {
    const component: GraphicsComponent = fixture.componentInstance;
    expect(component).toBeTruthy();
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
