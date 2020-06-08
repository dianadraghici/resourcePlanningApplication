import {Component, Input, OnDestroy} from '@angular/core';
import {Member} from "../../models/member.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {MembersTableService} from "../members-table.service";
import {MemberPosition} from "../../models/member-position.model";
import {Parameters} from "../../models/parameters.model";
import {Subscription} from "rxjs";

@Component({
    selector: 'ngbd-member-positions',
    templateUrl: './member-positions.html',
    styleUrls: ['./member-positions.css']
})
export class NgbdMemberPositions implements OnDestroy {

    @Input() member: Member;
    @Input() isDeactivatedMembersTableInactive: boolean;
    @Input() percentList: Parameters[];
    memberPositions: MemberPosition[];
    toggle: object = {};
    getSubscription: Subscription;
    deleteSubscription: Subscription;

    constructor(private modalService: NgbModal,
                private membersTableService: MembersTableService,
                private memberTableService: MembersTableService) {
    }

    open(content: any) {
        this.getSubscription = this.membersTableService.getMemberPositions(this.member)
            .subscribe(positions => {
                this.memberPositions = positions;
                this.modalService.open(content, {ariaLabelledBy: 'member-positions-title'});
            });
    }

    close() {
        this.modalService.dismissAll();
    }

    /** Remove from this.memberPositions the memberPosition with given memberPosition.id*/
    deleteMemberPosition({id: positionId}): void {
        this.deleteSubscription = this.memberTableService.deleteMemberPosition(positionId).subscribe(() => {
            this.memberPositions = this.memberPositions.filter(mp => positionId != mp.id);
        });
    }

    ngOnDestroy(): void {
        if (this.getSubscription) this.getSubscription.unsubscribe();
        if (this.deleteSubscription) this.deleteSubscription.unsubscribe();
    }
}
