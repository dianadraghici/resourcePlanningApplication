import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ScheduleMembersService} from "./schedule-members.service";
import {Calendar} from "../models/calendar.model";
import {MembersTableService} from "../members-table/members-table.service";
import {Member} from "../models/member.model";
import {CommonServicesService} from "../utils/common-services.service";
import {Constants} from "../utils/utils.constants";

@Component({
    selector: 'app-schedule-members',
    templateUrl: './schedule-members.component.html',
    styleUrls: ['./schedule-members.component.css']
})
export class ScheduleMembersComponent implements OnInit{
    calendar: Map<string,Array<Calendar>>;
    keysCalendar: any[];
    membersPosition: any[];
    keysMembers: Member[];
    keysForMember: any[];
    quarterYearModel: string;
    idColumnNumber: number;
    sumGeneralPercetage: number = 0;
    searchText: string;
    member: Member;
    inBetweenAssignments : any = Constants.IN_BETWEEN_ASSIGNMENTS;

    constructor(private router: Router, private scheduleMembers: ScheduleMembersService,
                private memberService: MembersTableService, private commonService: CommonServicesService) {
        this.idColumnNumber = 1;

        this.keysMembers = [];
    }

    ngOnInit() {
        this.scheduleMembers.getCalendar()
            .subscribe( data => {
                this.calendar = data;
                this.keysCalendar = this.getKeys(this.calendar);
                this.quarterYearModel = this.keysCalendar[0];
            });

        this.scheduleMembers.getProjectPositionMembers()
            .subscribe(data =>{
                    this.membersPosition = data;
                    this.transformToObject();
                }
            )
    }

    getKeys(array) : any[]{
        return Array.from(Object.keys(array));
    }

    /** get all the projects and percentage for the current member
     * @param member - the full name of the member
     * @param cal - the current column (date) from the table
     * */
    getTooltipDetailsForMembers(member : string, cal : any){
        /*verify if this.membersPosition[member] is not undefined */
        if (!!this.membersPosition[member]) {

            /*reinitialize the array of project names with the current percentage for the current  member*/
            cal.tooltipDetailsForMembers = "";

            this.membersPosition[member].forEach((mem) => {
                /*if a member has a date of a positions that is equal with the header of the calendar*/
                if((mem.idCalendarStartDateFk <= cal.id) && mem.idCalendarEndDateFk >= cal.id){
                    /*then concat project name and percentage for tooltip for each member*/
                    cal.tooltipDetailsForMembers = cal.tooltipDetailsForMembers + " " + mem.projectName  + " - "  + mem.percentMember + Constants.PERCENT_SYMBOL ;
                }

                /*get each column id by member firstName + lastName*/
                let columnId = document.getElementById(this.idColumnNumber + '-' + mem.firstName+ " " + mem.lastName);
                /*wait for columnId to be load*/
                if(columnId != undefined){
                    if(columnId.style.backgroundColor == "rgb(255, 0, 0)"){
                        /*set tooltip with the following text: 'in between assignments'*/
                        cal.tooltipDetailsForMembers = this.inBetweenAssignments;
                    }
                }
            });
        }
        return cal.tooltipDetailsForMembers;
    }

    getColor(member : string, cal : any): string{
        let idColumn = document.getElementById(this.idColumnNumber + '-' + member);

        if(this.membersPosition[member] != undefined){
            for( var mem of this.membersPosition[member]){
                if(mem.idProjectPositionFk == null){
                    this.percentageFunction(mem, idColumn, cal);
                    return null;
                }
                if((mem.idCalendarStartDateFk == cal.id
                    || mem.idCalendarStartDateFk < cal.id) &&
                    mem.idCalendarEndDateFk >= cal.id){

                    if(mem.statusProject == "VALID" || mem.statusProject == "TERMINATED"){
                        this.sumGeneralPercetage = this.sumGeneralPercetage + ((mem.percentMember)/100 * (mem.percentProject)/100);
                    }else{
                        if(mem.statusProject != "TERMINATED") {
                            let percetageCalculate = ((mem.percentMember) / 100 * (mem.percentProject) / 100);
                            this.sumIntervalPercentage(percetageCalculate);
                        }
                    }
                }
            }
            this.percentageFunction(mem, idColumn, cal);
        }
        this.contorId();
        this.resetIntervalPercentage();
    }

    percentageFunction(member, idColumn, cal : any){
        if(this.sumGeneralPercetage == 0) {
            idColumn.style.backgroundColor = "#FF0000";
            this.contorId();
        }else if(this.sumGeneralPercetage > 0 && this.sumGeneralPercetage <= 0.25){
            idColumn.style.backgroundColor = "#FFC000";
            this.contorId();
        } else if(this.sumGeneralPercetage > 0.25 && this.sumGeneralPercetage <= 0.50 ){
            idColumn.style.backgroundColor = "#FFFF00";
            this.contorId();
        }else if(this.sumGeneralPercetage > 0.50 && this.sumGeneralPercetage <= 0.75){
            idColumn.style.backgroundColor = "#00B0F0";
            this.contorId();
        }else if(this.sumGeneralPercetage > 0.75 && this.sumGeneralPercetage < 1) {
            idColumn.style.backgroundColor = "#0070C0";
            this.contorId();
        } else if (this.sumGeneralPercetage == 1) {
            let valid = true;
            if(this.membersPosition[member.firstName + " " + member.lastName] != undefined) {
                for (let mem of this.membersPosition[member.firstName + " " + member.lastName]) {
                    if((mem.idCalendarStartDateFk == cal.id
                        || mem.idCalendarStartDateFk < cal.id) &&
                        mem.idCalendarEndDateFk >= cal.id) {
                        if (mem.idProjectPositionFk == null) {
                            idColumn.style.backgroundColor = "#FF0000";
                            this.contorId();
                            return;
                        } else {
                            if (mem.statusProject != "VALID" && mem.statusProject != "TERMINATED") {
                                valid = false;
                            }
                        }
                    }
                }
            }
            if(valid) {
                idColumn.style.backgroundColor = "#92d050";
                this.contorId();
            } else {
                idColumn.style.backgroundColor = "#0070C0";
                this.contorId();
            }
        }else if(this.sumGeneralPercetage > 1){
            idColumn.style.backgroundColor = "#000000";
            this.contorId();
        }
    }

    resetIntervalPercentage(){
        this.sumGeneralPercetage = 0;
    }

    sumIntervalPercentage(intervalString){
        this.sumGeneralPercetage = this.sumGeneralPercetage + Number(intervalString);
    }

    contorId(){
        this.idColumnNumber ++;
    }

    resetId(){
        this.idColumnNumber = 1;
    }

    search() {
        if (!this.searchText) {
            return this.keysMembers;
        } else {
            return this.commonService.filterIt(this.keysMembers, this.searchText);
        }
    }

    transformToObject(){
        this.keysForMember = this.getKeys(this.membersPosition);
        for(let index in this.keysForMember){
            this.member = new Member();
            this.member.firstName = this.keysForMember[index];
            this.keysMembers.push(this.member);
        }
    }
}
