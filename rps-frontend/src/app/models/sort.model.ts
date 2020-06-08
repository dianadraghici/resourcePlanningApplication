export class Sort {
    by: string;
    direction: string;

    constructor(by: string = 'id', direction: string = 'ASC') {
        this.by = by;
        this.direction = direction;
    }
}