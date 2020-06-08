import {ComponentFixture, TestBed } from '@angular/core/testing';
import { ParametersComponent } from './parameters.component';
import {Pipe, PipeTransform} from "@angular/core";

describe('ParametersComponent', () => {
  let component: ParametersComponent;
  let fixture: ComponentFixture<ParametersComponent>;

  @Pipe({name: 'translate'})
  class TranslatePipeStub implements PipeTransform {
    transform(key: string): string {
      return key;
    }
  }

  beforeEach( () => {
    TestBed.configureTestingModule({
      declarations: [ ParametersComponent, TranslatePipeStub ]
    });
  });

  it('should create ParametersComponent', () => {
    //arrange
    fixture = TestBed.createComponent(ParametersComponent);

    //act
    component = fixture.componentInstance;

    //assert
    expect(component).toBeTruthy();
  });

});
