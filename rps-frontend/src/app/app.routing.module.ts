import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MembersTableComponent} from "./members-table/members-table.component";
import {ScheduleMembersComponent} from "./schedule-members/schedule-members.component";
import {ProjectComponent} from "./project/project.component";
import {ScheduleProjectsComponent} from "./schedule-projects/schedule-projects.component";
import {AuthGuard} from "./guards/auth-guard.service";
import {HomeComponent} from "./home/home.component";
import {DeactivatedMembersTableComponent} from "./members-table/deactivated-members/deactivated-members-table.component";
import {LoginComponent} from "./login/login.component";
import {ScheduleProjectsResolver} from "./schedule-projects/schedule-projects.resolver";
import {PageNotFoundComponent} from "./404NotFound/page-not-found.component";
import {MembersTableResolver} from "./members-table/members-table.resolver";

const routes: Routes = [
    {path: '', pathMatch: 'full', redirectTo: 'home'},
    {path: 'login', component: LoginComponent},
    {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
    {path: 'projects', component: ProjectComponent, canActivate: [AuthGuard]},
    {path: 'graphics/scheduleMembers', component: ScheduleMembersComponent, canActivate: [AuthGuard]},
    {path: 'graphics/scheduleProjects', component: ScheduleProjectsComponent, canActivate: [AuthGuard], resolve: { projectsGraphicsData: ScheduleProjectsResolver }},
    {path: 'members-table', component: MembersTableComponent, canActivate: [AuthGuard], resolve: { membersTableData: MembersTableResolver }},
    {path: 'members-table/deactivated', component: DeactivatedMembersTableComponent, canActivate: [AuthGuard]},
    {path: '**', component: PageNotFoundComponent}
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, {useHash: true})
    ],
    exports: [
        RouterModule
    ],
    declarations: []
})
export class AppRoutingModule {
}
