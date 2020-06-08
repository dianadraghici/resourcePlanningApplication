import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Member} from "../models/member.model";
import {MembersTableService} from "./members-table.service";
import {Angular5Csv} from 'angular5-csv/dist/Angular5-csv';
import {CommonServicesService} from "../utils/common-services.service";
import {Constants} from "../utils/utils.constants";
import {MemberExport} from "../models/memberExportCSV.model";
import {Parameters} from "../models/parameters.model";
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";
import {ErrorMessages} from "../utils/error.messages";
import {TranslatePipe} from "../utils/translate.pipe";
import {PaginationProperties} from "../models/pagination-properties.model";
import {Project} from "../models/project.model";
import {ProjectPosition} from "../models/project-position";
import {Subscription} from "rxjs";

@Component({
    selector: 'app-members-table',
    templateUrl: './members-table.component.html',
    styleUrls: ['./members-table.component.css'],
    providers: [
        TranslatePipe
    ],
})
export class MembersTableComponent implements OnInit, OnDestroy {

    @ViewChildren('pages') pages: QueryList<any>;
    technologyList: Parameters[];
    percentList: Parameters[];
    projectsList: Project[];
    memberExportList: MemberExport[] = [];
    memberExport: MemberExport;
    members: Member[];
    membersFiltered: Member[];
    projectPositionsMappedByProject: {string: ProjectPosition[]};
    searchText: string;
    flagAscendingFirstName: string;
    flagAscendingLastName: string;
    errorMessage: any;
    errorHasOccurred: boolean;
    options = {
        fieldSeparator: ',',
        quoteStrings: '"',
        decimalseparator: '.',
        showLabels: true,
        showTitle: true,
        useBom: true,
        headers: ['Staff Number', 'First Name', 'Last Name', 'Technology', 'Comment'],
        title: 'Members'
    };
    optionsForExport = {
        fieldSeparator: ',',
        quoteStrings: '"',
        decimalseparator: '.',
        showLabels: true,
        showTitle: true,
        useBom: true,
        headers: ['', '', '', '', ''],
        title: ''
    };
    memberForm: FormGroup = new FormGroup({});
    validationError = {img: {path: "assets/img/validationError.png", height: 15}};
    private fullActiveMembersList: Member[] = [];
    private membersSorted: Member[] = [];
    mainPagination: PaginationProperties<Member> = new PaginationProperties(true, Constants.MAX_ITEMS_PER_PAGE_FOR_MAIN_PAGINATION);
    searchPagination: PaginationProperties<Member> = new PaginationProperties(false, Constants.MAX_ITEMS_PER_PAGE_FOR_SEARCH_PAGINATION);
    deactivateSubscription: Subscription;
    getSubscription: Subscription;
    editSubscription: Subscription;
    getPageSubscription: Subscription;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private memberTableService: MembersTableService,
                private commonService: CommonServicesService,
                public errorMessages: ErrorMessages,
                private modalService: NgbModal,
                private translatePipe: TranslatePipe) {}

    ngOnInit() {
        const {membersTableData} = this.route.snapshot.data;
        this.fullActiveMembersList = membersTableData.activeMembers;
        this.projectsList = membersTableData.projectsList;
        this.technologyList = membersTableData.technologyList;
        this.percentList = membersTableData.percentList;
        this.projectPositionsMappedByProject = membersTableData.projectPositionsMappedByProject;
        this.flagAscendingFirstName = 'unsorted';
        this.flagAscendingLastName = 'unsorted';
        this.setActivePageForMainPagination(this.mainPagination.currentPage);
        this.applySortingRules();
    }

    /*save data for export from json(members) into a temporary array*/
    generateExcelWithTabelHeaders(membersFiltered: Member[]) {
        /*empty array of member list*/
        this.memberExportList.splice(0, this.memberExportList.length);
        for (let i in membersFiltered) {
            /*initialize memberExport with null values  on every loop  to avoid overriding*/
            this.memberExport = new MemberExport(null, null, null, null, null);
            this.memberExport.firstName = membersFiltered[i].firstName;
            this.memberExport.lastName = membersFiltered[i].lastName;
            this.memberExport.technologyDescription = membersFiltered[i].technologyDescription;
            this.memberExport.staffNumber = membersFiltered[i].staffNumber;
            this.memberExport.comment = membersFiltered[i].comment;
            if (this.memberExport.comment == null) {
                this.memberExport.comment = "";
            }
            /*add in memberExportList all members from  the table*/
            this.memberExportList.push(this.memberExport);
        }
        return this.memberExportList;
    }

    generateCsv() {
        /*translate the title and headers of columns in Excel*/
        this.optionsForExport.title = this.translatePipe.transform(this.options.title);
        for (let option in this.options.headers) {
            this.optionsForExport.headers[option] = this.translatePipe.transform(this.options.headers[option]);
        }
        /*create the Excel*/
        new Angular5Csv(this.getMembersToBeExported().filter((p, i) => this.commonService.objetsExported[i]),
            this.translatePipe.transform('Members_List'), this.optionsForExport);
    }

    deactivate(memberId: any) {
        this.deactivateSubscription = this.memberTableService.deactivateMember(memberId).subscribe(() => this.refreshTableData())
    };

    /*open modal for confirmation on member deactivation */
    openModal(content) {
        this.modalService.open(content, {centered: true});
    }

    receiveMessageMember(event) {
        if (!this.searchText) {
            this.refreshTableData();
        }else {
            this.search();
        }
        this.getSubscription = this.memberTableService.getActiveMembers().subscribe(members => this.fullActiveMembersList = members);
    }

    receiveNewAssignedPosition(event) {
        this.refreshTableData();
    }

    private getMembersToBeExported() {
        if (!this.searchText) {
            this.applySortingRules();
            this.memberExportList = this.generateExcelWithTabelHeaders(this.fullActiveMembersList);
        }
        this.commonService.enableExportFor(this.memberExportList);
        return this.memberExportList;
    }

    search() {
        if (!this.searchText) {
            this.refreshTableData();
        }
        if (this.searchText) {
            this.getSubscription = this.memberTableService.getActiveMembers().subscribe(members => {
                this.members = members;
                this.members.forEach((member, index) => this.memberFormValidator(index));
                this.membersSorted = this.commonService.filterIt(this.members, this.searchText);
                this.searchPagination.data.source = this.membersSorted;
                this.membersFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.membersSorted);
                this.memberExportList = this.generateExcelWithTabelHeaders(this.membersSorted);
                this.mainPagination.display = false;
                this.searchPagination.display = true;
            });
        }
    }

    sortByColFN() {
        if (!!this.searchText) {
            return this.searchNavigationSortByFN();
        }
        if (this.flagAscendingFirstName === 'sorted-asc') {
            localStorage.setItem(Constants.SORT_ORDER_ACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_DESCENDING_FIRSTNAME);
            this.fullActiveMembersList.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.flagAscendingFirstName = 'sorted-desc';
        } else if(this.flagAscendingFirstName === 'sorted-desc') {
            localStorage.setItem(Constants.SORT_ORDER_ACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_UNSORTED_FIRSTNAME);
            this.fullActiveMembersList.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.flagAscendingFirstName = 'unsorted';
        }else {
            localStorage.setItem(Constants.SORT_ORDER_ACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_ASCENDING_FIRSTNAME);
            this.flagAscendingFirstName = 'sorted-asc';
        }
        this.flagAscendingLastName = 'unsorted';
        this.refreshTableData();
    }

    sortByColLN() {
        if (!!this.searchText) {
            return this.searchNavigationSortByLN();
        }
        if (this.flagAscendingLastName === 'sorted-asc') {
            localStorage.setItem(Constants.SORT_ORDER_ACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_DESCENDING_LASTNAME);
            this.fullActiveMembersList.sort((a, b) => a.lastName.localeCompare(b.lastName));
            this.flagAscendingLastName = 'sorted-desc';
        } else if(this.flagAscendingLastName === 'sorted-desc'){
            localStorage.setItem(Constants.SORT_ORDER_ACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_UNSORTED_LASTNAME);
            this.fullActiveMembersList.sort((a, b) => b.lastName.localeCompare(a.lastName));
            this.flagAscendingLastName = 'unsorted';
        }else {
            localStorage.setItem(Constants.SORT_ORDER_ACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_ASCENDING_LASTNAME);
            this.flagAscendingLastName = 'sorted-asc';
        }
        this.flagAscendingFirstName = 'unsorted';
        this.refreshTableData();
    }

    editOnClick(member: Member, index: number) {
        if (this.canUpdateMemberAt(index)) {
            this.errorHasOccurred = false;
            /*Set flag with default  value*/
            member.flag = "1";
            if (!!this.technologyList) {
                member.technologyDescription = this.technologyList.filter(tech => tech.id == member.technologyId)[0].description;
            }

            /*call update function for the current member,which is received from html with ngModule*/
            this.editSubscription = this.memberTableService.editMember(member).subscribe(
                isSuccess => {
                },
                isError => {
                    this.errorHasOccurred = true;
                    this.errorMessage = isError;
                }
            )
        }
    }

    private memberFormValidator(index: number, action: string = "addControl") {
        this.memberForm[action]('member_staffNumber_' + index, new FormControl('', [Validators.required, Validators.pattern('^[\\w-]+$'), Validators.minLength(1)]));
        this.memberForm[action]('member_firstName_' + index, new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z ,.\'-]+$'), Validators.minLength(1)]));
        this.memberForm[action]('member_lastName_' + index, new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z ,.\'-]+$'), Validators.minLength(1)]));
        this.memberForm[action]('member_technologyId_' + index, new FormControl('', [Validators.required, Validators.minLength(1)]));
        this.memberForm[action]('member_comment_' + index, new FormControl('', []));
    }

    private canUpdateMemberAt(index: number): Boolean {
        return this.staffNumberControl(index).valid
            && this.firstNameControl(index).valid
            && this.lastNameControl(index).valid
            && this.memberForm.controls['member_technologyId_' + index].valid
            && this.memberForm.controls['member_comment_' + index].valid;
    }

    staffNumberControl(index: number): AbstractControl {
        return this.memberForm.controls['member_staffNumber_' + index];
    }

    firstNameControl(index: number) {
        return this.memberForm.controls['member_firstName_' + index];
    }

    lastNameControl(index: number) {
        return this.memberForm.controls['member_lastName_' + index];
    }

    /**
     * The pageChange event handler for the main pagination.
     * @param page - the current page number on the pagination.
     * */
    setActivePageForMainPagination(page: number) {
        this.mainPagination.currentPage = page;
        this.getPageSubscription = this.memberTableService.getActiveMembersPage(this.mainPagination.currentPage, this.mainPagination.maxPerPage)
            .subscribe(pageResponse => {
                this.members = pageResponse.first;
                this.mainPagination.data.size = pageResponse.second;
                this.membersFiltered = this.members;
                this.commonService.enableExportFor(this.membersFiltered);
                /*Register on init the projectFormValidator for active members's attributes*/
                this.members.forEach((member, index) => this.memberFormValidator(index));
            });
    }

    /**
     * The pageChange event handler for the search pagination.
     * @param page - the current page number on the pagination.
     * */
    setActivePageForSearchPagination(page: number) {
        this.searchPagination.currentPage = page;
        this.membersFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.searchPagination.data.source);
    }

    /** The sort rules to be apply on this component on initialization. */
    private applySortingRules() {
        switch (localStorage.getItem(Constants.SORT_ORDER_ACTIVE_MEMBERS)) {
            case Constants.SORT_ORDER_MEMBER_ASCENDING_FIRSTNAME:
                this.flagAscendingFirstName = 'sorted-asc';
                this.sortByColFN();
                break;
            case Constants.SORT_ORDER_MEMBER_DESCENDING_FIRSTNAME:
                this.flagAscendingFirstName = 'sorted-desc';
                this.sortByColFN();
                break;
            case Constants.SORT_ORDER_MEMBER_UNSORTED_FIRSTNAME:
                this.flagAscendingFirstName = 'unsorted';
                this.sortByColFN();
                break;
            case Constants.SORT_ORDER_MEMBER_ASCENDING_LASTNAME:
                this.flagAscendingLastName = 'sorted-asc';
                this.sortByColLN();
                break;
            case Constants.SORT_ORDER_MEMBER_DESCENDING_LASTNAME:
                this.flagAscendingLastName = 'sorted-desc';
                this.sortByColLN();
                break;
            case Constants.SORT_ORDER_MEMBER_UNSORTED_LASTNAME:
                this.flagAscendingLastName = 'unsorted';
                this.sortByColLN();
                break;
            case undefined:
                this.flagAscendingFirstName = 'unsorted';
                this.sortByColFN();
                this.flagAscendingLastName = 'unsorted';
                this.sortByColLN();
                break;
        }
    }

    /** Refresh the data table for the current page. */
    private refreshTableData(): void {
        this.getPageSubscription = this.memberTableService.getActiveMembersPage(this.mainPagination.currentPage, this.mainPagination.maxPerPage).subscribe(pageResponse => {
            this.members = pageResponse.first;
            this.members.forEach((member, index) => this.memberFormValidator(index));
            this.mainPagination.data.size = pageResponse.second;
            this.membersFiltered = this.members;
            this.mainPagination.display = true;
            this.searchPagination.display = false;
        });
    }

    /** The Sort to be apply for the FirstName column when the user is in search mode. */
    private searchNavigationSortByFN() {
        if (this.flagAscendingFirstName=== 'sorted-asc') {
            this.membersSorted = this.searchPagination.data.source.sort((a, b) => a.firstName.localeCompare(b.firstName));
            this.memberExportList.sort((a, b) => a.firstName.localeCompare(b.firstName));
            this.flagAscendingFirstName = 'sorted-desc';
        } else if (this.flagAscendingFirstName === 'sorted-desc'){
            this.membersSorted = this.searchPagination.data.source.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.memberExportList.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.flagAscendingFirstName = 'unsorted';
        } else {
            this.flagAscendingFirstName = 'sorted-asc';
        }
        this.membersFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.membersSorted);
        return;
    }

    /** The Sort to be apply for the LastName column when the user is in search mode. */
    private searchNavigationSortByLN() {
        if (this.flagAscendingLastName === 'sorted-asc') {
            this.membersSorted = this.searchPagination.data.source.sort((a, b) => a.lastName.localeCompare(b.lastName));
            this.memberExportList.sort((a, b) => a.lastName.localeCompare(b.lastName));
            this.flagAscendingLastName = 'sorted-desc';
        } else  if (this.flagAscendingLastName === 'sorted-desc'){
            this.membersSorted = this.searchPagination.data.source.sort((a, b) => b.lastName.localeCompare(a.lastName));
            this.memberExportList.sort((a, b) => b.lastName.localeCompare(a.lastName));
            this.flagAscendingLastName = 'unsorted';
        } else {
            this.flagAscendingLastName = 'sorted-asc';
        }
        this.membersFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.membersSorted);
        return;
    }

    ngOnDestroy(): void {
        if(this.getSubscription) this.getSubscription.unsubscribe();
        if(this.getPageSubscription) this.getPageSubscription.unsubscribe();
        if(this.editSubscription) this.editSubscription.unsubscribe();
        if(this.deactivateSubscription) this.deactivateSubscription.unsubscribe();
    }
}
