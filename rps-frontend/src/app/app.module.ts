import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER  } from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { AngularFontAwesomeModule } from 'angular-font-awesome';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing.module';
import {HttpClientModule} from '@angular/common/http';

import { ScheduleMembersComponent } from './schedule-members/schedule-members.component';
import {CdkTableModule} from "@angular/cdk/table";

import { ProjectComponent } from './project/project.component';
import {ProjectService} from "./project/project.service";
import {NgbdAddProject} from "./project/add/add-project";
import { CalendarComponent } from './calendar/calendar.component';
import { ParametersComponent } from './parameters/parameters.component';
import {UserComponent} from "./user/user.component";
import {UserService} from "./user/user.service";
import {MenuComponent} from "./menu/menu.component";
import {LoginComponent} from "./login/login.component";
import {HomeComponent} from "./home/home.component";
import {RegisterComponent} from "./register/register.component";
import {MembersTableComponent } from './members-table/members-table.component';
import {MembersTableService} from "./members-table/members-table.service";

import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {
    MatSelectModule
} from "@angular/material";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbdAddMember} from "./members-table/add/add-member";
import {TranslateService} from './utils/translate.service';
import {TranslatePipe} from './utils/translate.pipe';
import {CalendarService} from "./calendar/calendar.service";
import {NgbdMemberPositions} from "./members-table/member-position/member-positions";
import {NgbdProjectPositions} from "./project/position/view/project-positions";
import {NgbdProjectPositionDetail} from "./project/position/detail/project-position-detail";
import {NgbdAddProjectPosition} from "./project/add-position/add-position-project";
import {NgbdAddMemberPosition} from "./members-table/add-position/add-member-position";
import {CommonServicesService} from './utils/common-services.service';
import {NgbdMemberPositionDetail} from "./members-table/member-position/detail/member-positions-detail";
import {ScheduleProjectsComponent} from './schedule-projects/schedule-projects.component';
import {ContenteditableModule} from "ng-contenteditable";
import {ErrorMessages} from "./utils/error.messages";
import {GraphicsComponent} from './graphics/graphics.component';
import {SessionService} from "./login/session.service";
import {CookieService} from "ngx-cookie-service";
import {AuthGuard} from "./guards/auth-guard.service";
import {DeactivatedMembersTableComponent} from "./members-table/deactivated-members/deactivated-members-table.component";
import {ScheduleProjectsResolver} from "./schedule-projects/schedule-projects.resolver";
import {ParametersService} from "./parameters/parameters.service";
import {PageNotFoundComponent} from "./404NotFound/page-not-found.component";
import {AlertModule} from 'ngx-bootstrap/alert';
import {CustomErrorHandlerService} from "./global-error-handler.service";
import {MembersTableResolver} from "./members-table/members-table.resolver";

export function setupTranslateFactory(
    service: TranslateService): Function {
    return () => service.use(localStorage.getItem('lang'));
}

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        ProjectComponent,
        NgbdAddProject,
        NgbdAddMember,
        NgbdMemberPositions,
        NgbdAddMemberPosition,
        NgbdProjectPositions,
        NgbdProjectPositionDetail,
        NgbdAddProjectPosition,
        NgbdMemberPositionDetail,
        CalendarComponent,
        ParametersComponent,
        UserComponent,
        MenuComponent,
        LoginComponent,
        RegisterComponent,
        MembersTableComponent,
        DeactivatedMembersTableComponent,
        ScheduleMembersComponent,
        TranslatePipe,
        ScheduleProjectsComponent,
        GraphicsComponent,
        PageNotFoundComponent
    ],
    imports: [
        HttpClientModule,
        FormsModule,
        BrowserAnimationsModule,
        NgbModule,
        AppRoutingModule,
        BrowserModule,
        CdkTableModule,
        ReactiveFormsModule.withConfig({warnOnNgModelWithFormControl: 'never'}),
        MatSelectModule,
        AngularFontAwesomeModule,
        ContenteditableModule,
        AlertModule.forRoot()

    ],
    providers: [
        AuthGuard,
        ProjectService,
        UserService,
        CalendarService,
        SessionService,
        MembersTableService,
        TranslateService,
        CommonServicesService,
        ErrorMessages,
        MembersTableResolver,
        ScheduleProjectsResolver,
        ParametersService,
        CookieService,
        CustomErrorHandlerService,
        {
            provide: APP_INITIALIZER,
            useFactory: setupTranslateFactory,
            deps: [ TranslateService ],
            multi: true
        }
    ],
    bootstrap: [AppComponent],
    exports: [CdkTableModule]
})
export class AppModule { }
