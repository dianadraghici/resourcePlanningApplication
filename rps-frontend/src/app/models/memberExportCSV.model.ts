export class MemberExport {
    staffNumber ?: string;
    firstName ?: string;
    lastName ?: string;
    technologyDescription ?: string;
    comment ?: string;


    constructor(staffNumber, firstName, lastName, technologyDescription, comment) {
        this.staffNumber = staffNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.technologyDescription = technologyDescription;
        this.comment = comment;
    }
}

