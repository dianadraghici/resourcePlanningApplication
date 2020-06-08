
export class PageResponse<T> {
    private readonly _first: T[];
    private readonly _second: number;

    get first(): T[] {
        return this._first;
    }

    get second(): number {
        return this._second;
    }
}