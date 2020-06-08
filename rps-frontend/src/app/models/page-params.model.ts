import {Constants} from "../utils/utils.constants";
import {HttpParams} from "@angular/common/http";
import {Sort} from "./sort.model";

export class PageParams {
    private page: number;
    private size: number;
    private sort: Sort = new Sort();

    private constructor(page: number, size: number, sort: Sort) {
        this.page = page;
        this.size = size;
        this.sort.by = sort.by;
        this.sort.direction = sort.direction;
    }

    static build(page?: number, size?: number, sort: Sort = new Sort()): HttpParams {
        const pageParam = new PageParams(0, Constants.MAX_ITEMS_PER_PAGE_FOR_MAIN_PAGINATION, sort); //Creating a PageParams instance with default values.
        return new HttpParams()
            .set("page", `${(page || pageParam.page) - 1}`)
            .set("size", `${size || pageParam.size}`)
            .set("sortBy", sort.by || pageParam.sort.by)
            .set("direction", sort.direction || pageParam.sort.direction);
    }

    static processSortCriteria(fullSortCriteria: string): Sort {
        const sort: Sort = new Sort();
        if (!!fullSortCriteria) {
            if (fullSortCriteria.startsWith('ascending')) {
                sort.direction = 'ASC';
                const sortCriteria = fullSortCriteria.substring('ascending'.length);
                sort.by = `${sortCriteria.charAt(0).toLocaleLowerCase()}${sortCriteria.slice(1)}`;
            }
            if (fullSortCriteria.startsWith('descending')) {
                sort.direction = 'DESC';
                const sortCriteria = fullSortCriteria.substring('descending'.length);
                sort.by = `${sortCriteria.charAt(0).toLocaleLowerCase()}${sortCriteria.slice(1)}`;
            }
        }
        return sort;
    }

}