import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Member} from "../models/member.model";
import {Constants} from "../utils/utils.constants";
import {MemberPosition} from "../models/member-position.model";
import {Observable} from "rxjs";
import {PageParams} from "../models/page-params.model";
import {PageResponse} from "../models/page-response.model";
import {Sort} from "../models/sort.model";

@Injectable()
export class MembersTableService {
    private memberServiceUrl = Constants.MEMBER_CONTROLLER;
    private memberPositionServiceUrl = Constants.MEMBER_POSITION_CONTROLLER;

    constructor(private http: HttpClient) {}

    public getActiveMembers() {
        return this.http.get<Member[]>(this.memberServiceUrl + Constants.GET_ACTIVE_MEMBERS, {withCredentials: true});
    }

    public getActiveMembersPage(page: number, size?: number): Observable<PageResponse<Member>> {
        const url = `${this.memberServiceUrl}${Constants.GET_PAGINATED_ACTIVE_MEMBERS}`;
        return this.http.get<PageResponse<Member>>(url, {withCredentials: true, params: PageParams.build(page, size, MembersTableService.sortCriteriaForActiveMembers())});
    }

    public getInactiveMembersPage(page: number, size?: number): Observable<PageResponse<Member>> {
        const url = `${this.memberServiceUrl}${Constants.GET_PAGINATED_INACTIVE_MEMBERS}`;
        return this.http.get<PageResponse<Member>>(url, {withCredentials: true, params: PageParams.build(page, size, MembersTableService.sortCriteriaForInactiveMembers())});
    }

    public getInactiveMembers() {
        return this.http.get<Member[]>(this.memberServiceUrl + Constants.GET_INACTIVE_MEMBERS, {withCredentials: true});
    }

    public deactivateMember(id) {
        return this.http.put(this.memberServiceUrl + Constants.DEACTIVATE_MEMBER, id, {withCredentials: true});
    }

    reactivateMember(member: Member): Observable<any> {
        member.flag = 1;
        return this.editMember(member);
    }

    public addMember(member) {
        return this.http.post(this.memberServiceUrl, member, {withCredentials: true});
    }

    public editMember(member) {
        return this.http.put(this.memberServiceUrl + Constants.UPDATE_MEMBER, member, {withCredentials: true});
    }

    public addMemberPosition(memberPosition) {
        return this.http.post(Constants.MEMBER_POSITION_CONTROLLER, memberPosition, {withCredentials: true});
    }

    public getMemberPositions({id}) {
        return this.http.get<MemberPosition[]>(this.memberPositionServiceUrl + Constants.FIND_BY_ID_MEMBER_POSITION + id, {withCredentials: true});
    }

    public editMemberPositions(memberPositions) {
        return this.http.put(this.memberPositionServiceUrl + Constants.UPDATE_MEMBER_POSITION, memberPositions, {
            withCredentials: true
        });
    }

    public checkBackendHealth() {
        return this.http.get(this.memberServiceUrl + '/health', {responseType: 'text'});
    }

    public deleteMemberPosition(id: number) {
        return this.http.delete(this.memberPositionServiceUrl + Constants.DELETE_MEMBER_POSITION + id, {withCredentials: true});
    }

    private static sortCriteriaForActiveMembers(): Sort {
        return PageParams.processSortCriteria(localStorage.getItem(Constants.SORT_ORDER_ACTIVE_MEMBERS));
    }

    private static sortCriteriaForInactiveMembers(): Sort {
        return PageParams.processSortCriteria(localStorage.getItem(Constants.SORT_ORDER_INACTIVE_MEMBERS));
    }

}
