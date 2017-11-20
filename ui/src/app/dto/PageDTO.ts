export interface PageDTO<T> {
  content?: T[];
  first?: boolean;
  last?: boolean;
  number?: number;
  numberOfElements?: number;
  pageable?: PageableDTO;
  size?: number;
  sort?: SortDTO;
  totalElement?: number;
  totalPages?: number;
}

export interface PageableDTO {
  offset?: number;
  pageNumber?: number;
  pageSize?: number;
  paged?: boolean;
  sort?: SortDTO;
  unpaged?: boolean;
}

export interface SortDTO {
  sorted?: boolean;
  unsorted?: boolean;
}
