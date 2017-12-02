import { Pageable, PageableStr } from "../models/Pageable";

export class PageHelper {
  static convertToPageableStr(pageable: Pageable): PageableStr {
    return {
      page: (pageable.page || "") + "",
      size: (pageable.size || "") + "",
    };
  }
}
