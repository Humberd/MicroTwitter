import { PageDTO } from "../dto/PageDTO";
import { Pageable } from "../models/Pageable";
import { Observable } from "rxjs/Observable";

export abstract class AbstractScrollPageableComponent<T> {
  itemsList: T[] = [];
  currentPage: PageDTO<T>;
  loadingNextPage = false;

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
          while (page.content.length > 0 && this.shouldRemoveNewPageItem(lastElement, page.content[0])) {
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

  /**
   * In case in the new page there is an item already in the list we want to make sure its not being displayed twice
   *
   * This method would be invoked until it returns {false}
   * When it returns {true} it removes [newPageFirstItem], which is at the beginning of the new page from it.
   *
   * The default compare key is "id" and the itemsList is treated as descending
   *
   * @param lastListItem
   * @param newPageFirstItem
   * @returns {boolean} should [newPageFirstItem] be removed from the new page
   */
  protected shouldRemoveNewPageItem(lastListItem, newPageFirstItem): boolean {
    return lastListItem["id"] <= newPageFirstItem["id"];
  }

  public prependItem(item: T) {
    this.itemsList.unshift(item);
  }
}
