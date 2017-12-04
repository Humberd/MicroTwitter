import { PageDTO } from "../dto/PageDTO";
import { Pageable } from "../models/Pageable";
import { Observable } from "rxjs/Observable";

export abstract class AbstractScrollPageableComponent<T> {
  itemsList: T[] = [];
  currentPage: PageDTO<T>;
  loadingNextPage = false;

  protected itemIdKey = "id";

  abstract invokeGetPageMethod(...params: any[]): Observable<PageDTO<T>>;

  public requestNextPage(...params: any[]): void {
    if (this.currentPage.last) {
      console.info("Current page is the last page. Cannot get more.");
      return;
    }

    const nextPageNumber = this.currentPage.number + 1;
    console.info(`Requesting page number ${nextPageNumber}`);

    this.getPage(...params, {page: nextPageNumber} as Pageable)
      .subscribe();
  }

  protected getPage(...params: any[]): Observable<PageDTO<T>> {
    return Observable.of(1)
      .do(() => this.loadingNextPage = true)
      .flatMap(() => this.invokeGetPageMethod(...params))
      .do(page => {
        this.currentPage = page;

        if (this.itemsList.length > 0) {
          // makes sure that the tweet is not displayed twice
          const lastElement = this.itemsList[this.itemsList.length - 1];
          while (page.content.length > 0 && lastElement[this.itemIdKey] <= page.content[0][this.itemIdKey]) {
            page.content.splice(0, 1);
          }
        }
        this.itemsList.push(...page.content);
        console.info(`Received page number ${page.number}`);
        this.loadingNextPage = false;
      })
      .catch(err => {
        this.loadingNextPage = false;
        throw err;
      });
  }

  public prependItem(item: T) {
    this.itemsList.unshift(item);
  }
}
