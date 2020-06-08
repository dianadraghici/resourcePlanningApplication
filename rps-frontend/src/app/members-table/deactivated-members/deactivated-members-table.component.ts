import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {Router} from "@angular/router";
import {MembersTableService} from "../members-table.service";
import {Angular5Csv} from 'angular5-csv/dist/Angular5-csv';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Parameters} from "../../models/parameters.model";
import {MemberExport} from "../../models/memberExportCSV.model";
import {Member} from "../../models/member.model";
import {CommonServicesService} from "../../utils/common-services.service";
import {ParametersService} from "../../parameters/parameters.service";
import {Constants} from "../../utils/utils.constants";
import {TranslatePipe} from "../../utils/translate.pipe";
import {PaginationProperties} from "../../models/pagination-properties.model";

@Component({
    templateUrl: './deactivated-members-table.component.html',
    styleUrls: ['./deactivated-members-table.component.css'],
    providers: [
        TranslatePipe
    ],
})
export class DeactivatedMembersTableComponent implements OnInit {

    @ViewChildren('pages') pages: QueryList<any>;

    technologyList: Parameters[];
    memberExportList: MemberExport[] = [];
    memberExport: MemberExport;
    members: Member[];
    membersFiltered: Member[];
    percentList: Parameters[];
    searchText: string;
    flagAscendingFirstName: string = 'unsorted';
    flagAscendingLastName: string = 'unsorted';
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
        title: 'CGI Inactive Members'
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
    private fullInactiveMembersList: Member[] = [];
    mainPagination: PaginationProperties<Member> = new PaginationProperties(true, Constants.MAX_ITEMS_PER_PAGE_FOR_MAIN_PAGINATION);
    searchPagination: PaginationProperties<Member> = new PaginationProperties(false, Constants.MAX_ITEMS_PER_PAGE_FOR_SEARCH_PAGINATION);

    constructor(private router: Router, private memberTableService: MembersTableService,
                public commonService: CommonServicesService,
                private parameterService: ParametersService,
                private modalService: NgbModal,
                private translatePipe: TranslatePipe) {
    }

    ngOnInit() {
        this.setActivePageForMainPagination(this.mainPagination.currentPage);
        this.applySortingRules();
        this.memberTableService.getInactiveMembers().subscribe(members => this.fullInactiveMembersList = members);
        this.parameterService.getTechnologyParameters().subscribe(data => this.technologyList = data);
        this.parameterService.getPercentParameters().subscribe(data => this.percentList = data);
    }

    /*save data for export from json(members) into a temorary array*/
    generateExcelWithTabelHeaders(membersFiltered: Member[]) {
        /*empty array of member list*/
        this.memberExportList.splice(0, this.memberExportList.length);
        for (let i in membersFiltered) {
            this.memberExport = new MemberExport(null, null, null, null, null);
            this.memberExport.firstName = membersFiltered[i].firstName;
            this.memberExport.lastName = membersFiltered[i].lastName;
            this.memberExport.technologyDescription = membersFiltered[i].technologyDescription;
            this.memberExport.staffNumber = membersFiltered[i].staffNumber;
            this.memberExport.comment = membersFiltered[i].comment;
            if (this.memberExport.comment == null) {
                this.memberExport.comment = "";
            }
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
        new Angular5Csv(this.getMembersToBeExported(), this.translatePipe.transform('Deactivated_Members_List'), this.optionsForExport);
    }

    reactivate(member: Member) {
        this.memberTableService.reactivateMember(member).subscribe(() => this.refreshDataTable());
    };

    /* open modal for confirmation on member deactivation */
    openModal(content) {
        this.modalService.open(content, {centered: true});
    }


    /** Refresh the data table for the current page. */
    refreshDataTable() {
        this.memberTableService.getInactiveMembersPage(this.mainPagination.currentPage, this.mainPagination.maxPerPage).subscribe(pageResponse => {
            this.members = pageResponse.first;
            this.mainPagination.data.size = pageResponse.second;
            this.membersFiltered = this.members;
            this.mainPagination.display = true;
            this.searchPagination.display = false;
        });
        this.memberTableService.getInactiveMembers().subscribe(members => this.fullInactiveMembersList = members);
    }

    private getMembersToBeExported() {
        if (!this.searchText) {
            this.applySortingRules();
            /*export selected columns*/
            this.memberExportList = this.generateExcelWithTabelHeaders(this.fullInactiveMembersList);
        }
        this.commonService.enableExportFor(this.memberExportList);
        return this.memberExportList;
    }

    search() {
        if (!this.searchText) {
            this.refreshDataTable();
        }
        if (this.searchText) {
            this.searchPagination.data.source = this.commonService.filterIt(this.fullInactiveMembersList, this.searchText);
            this.membersFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, this.searchPagination.data.source);
            /*export selected columns*/
            this.memberExportList = this.generateExcelWithTabelHeaders(this.searchPagination.data.source);
            this.mainPagination.display = false;
            this.searchPagination.display = true;
        }

        return this.memberExportList;
    }

    sortByColFN() {
        if (!!this.searchText) {
            return this.searchNavigationSortByFN();
        }
        if (this.flagAscendingFirstName === 'sorted-asc') {
            localStorage.setItem(Constants.SORT_ORDER_INACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_DESCENDING_FIRSTNAME);
            this.fullInactiveMembersList.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.flagAscendingFirstName = 'sorted-desc';
        } else if(this.flagAscendingFirstName === 'sorted-desc') {
            localStorage.setItem(Constants.SORT_ORDER_INACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_UNSORTED_FIRSTNAME);
            this.fullInactiveMembersList.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.flagAscendingFirstName = 'unsorted';
        }else {
            localStorage.setItem(Constants.SORT_ORDER_INACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_ASCENDING_FIRSTNAME);
            this.flagAscendingFirstName = 'sorted-asc';
        }
        this.flagAscendingLastName = 'unsorted';
        this.setActivePageForMainPagination(this.mainPagination.currentPage);
    }

    sortByColLN() {
        if (!!this.searchText) {
            return this.searchNavigationSortByLN();
        }
        if (this.flagAscendingLastName === 'sorted-asc') {
            localStorage.setItem(Constants.SORT_ORDER_INACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_DESCENDING_LASTNAME);
            this.fullInactiveMembersList.sort((a, b) => a.lastName.localeCompare(b.lastName));
            this.flagAscendingLastName = 'sorted-desc';
        } else if(this.flagAscendingLastName === 'sorted-desc'){
            localStorage.setItem(Constants.SORT_ORDER_INACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_UNSORTED_LASTNAME);
            this.fullInactiveMembersList.sort((a, b) => b.lastName.localeCompare(a.lastName));
            this.flagAscendingLastName = 'unsorted';
        }else {
            localStorage.setItem(Constants.SORT_ORDER_INACTIVE_MEMBERS, Constants.SORT_ORDER_MEMBER_ASCENDING_LASTNAME);
            this.flagAscendingLastName = 'sorted-asc';
        }
        this.flagAscendingFirstName = 'unsorted';
        this.setActivePageForMainPagination(this.mainPagination.currentPage);
    }

    /**
     * The pageChange event handler for the main pagination.
     * @param page - the current page number on the pagination.
     * */
    setActivePageForMainPagination(page: number) {
        this.mainPagination.currentPage = page;
        this.memberTableService.getInactiveMembersPage(this.mainPagination.currentPage, this.mainPagination.maxPerPage)
            .subscribe(pageResponse => {
                this.members = pageResponse.first;
                this.mainPagination.data.size = pageResponse.second;
                this.membersFiltered = this.members;
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

    /** The Sort to be apply for the FirstName column when the user is in search mode. */
    private searchNavigationSortByFN() {
        let sortedData: Member[];
        if (this.flagAscendingFirstName=== 'sorted-asc') {
            sortedData = this.searchPagination.data.source.sort((a, b) => a.firstName.localeCompare(b.firstName));
            this.memberExportList.sort((a, b) => a.firstName.localeCompare(b.firstName));
            this.flagAscendingFirstName = 'sorted-desc';
        } else if (this.flagAscendingFirstName === 'sorted-desc'){
            sortedData = this.searchPagination.data.source.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.memberExportList.sort((a, b) => b.firstName.localeCompare(a.firstName));
            this.flagAscendingFirstName = 'unsorted';
        } else {
            this.flagAscendingFirstName = 'sorted-asc';
        }
        this.membersFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, sortedData);
        return;
    }

    /** The Sort to be apply for the LastName column when the user is in search mode. */

    private searchNavigationSortByLN() {
        let sortedData: Member[];
        if (this.flagAscendingLastName === 'sorted-asc') {
            sortedData = this.searchPagination.data.source.sort((a, b) => a.lastName.localeCompare(b.lastName));
            this.memberExportList.sort((a, b) => a.lastName.localeCompare(b.lastName));
            this.flagAscendingLastName = 'sorted-desc';
        } else  if (this.flagAscendingLastName === 'sorted-desc'){
            sortedData = this.searchPagination.data.source.sort((a, b) => b.lastName.localeCompare(a.lastName));
            this.memberExportList.sort((a, b) => b.lastName.localeCompare(a.lastName));
            this.flagAscendingLastName = 'unsorted';
        } else {
            this.flagAscendingLastName = 'sorted-asc';
        }
        this.membersFiltered = this.commonService.applyPageDataSource(this.searchPagination.currentPage, this.searchPagination.maxPerPage, sortedData);
        return;
    }

}
