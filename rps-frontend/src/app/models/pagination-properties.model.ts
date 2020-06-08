export class PaginationProperties<T> {
    display: boolean;
    rotate: boolean = false;
    maxPagesToDisplay: number = 3;
    maxPerPage: number;
    currentPage: number = 1;
    ellipses: boolean = true;
    boundaryLinks: boolean = true;
    size: string = 'sm';
    data: {
        source: T[],
        size: number
    };

    constructor(display: boolean, maxPerPage: number) {
        this.display = display;
        this.maxPerPage = maxPerPage;
        this.data = {source: [], size: 0};
    }
}