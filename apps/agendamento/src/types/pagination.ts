export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number; // página atual
  first: boolean;
  last: boolean;
  empty: boolean;
}