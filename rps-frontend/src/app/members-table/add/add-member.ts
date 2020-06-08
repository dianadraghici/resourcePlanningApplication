import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Member} from "../../models/member.model";
import {MembersTableService} from "../members-table.service";
import {Parameters} from "../../models/parameters.model";
import {ErrorMessages} from "../../utils/error.messages";
import {Constants} from "../../utils/utils.constants";

@Component({
    selector: 'ngbd-add-member',
    templateUrl: './add-member.html',
    styleUrls: ['./add-member.css']
})
export class NgbdAddMember {
    dismissible = true;
    member: Member;
    successMsg: { type: string, msg: string };
    errorMsg: { type: string, msg: string } | undefined;
    @Input() technologyList: Parameters[];
    @Output() memberToBeSent = new EventEmitter<Member>();

    constructor(private modalService: NgbModal,
                private membersTableService: MembersTableService,
                public errorMessages: ErrorMessages) {
    }

    openAddMemberModal(content: any) {
        this.member = new Member();
        this.modalService.open(content, {ariaLabelledBy: 'add-member-title'});
    }

    save() {
        this.errorMsg = undefined;
        this.member.flag = "1";
        const {description} = this.technologyList.find(({id}) => id === this.member.technologyId);
        this.member.technologyDescription = description;
        this.membersTableService.addMember(this.member).subscribe(
            (addedMember: Member) => {
                this.close();
                this.successMsg = {type: 'success', msg: `${Constants.MEMBER_SAVED}`};
                this.memberToBeSent.emit(addedMember);
            },
            ({error: {message}}) => {
                this.errorMsg = {type: 'danger', msg: `${this.errorMessages.MEMBER_SAVED_ERROR}`};
                if(message.includes('ConstraintViolationException')){
                    this.errorMsg = {type: 'danger', msg: `${this.errorMessages.MEMBER_STAFF_ALREADY_REGISTERED}`};
                }
            }
        )
    }

    close() {
        this.modalService.dismissAll();
    }
}
