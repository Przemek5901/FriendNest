import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { debounceTime, delay, Observable } from 'rxjs';
import { SearchResults } from '../models/response/SearchResults';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  constructor(private http: HttpClient) {}

  search(keyword: string, userId: number): Observable<SearchResults> {
    const payload = {
      keyword: keyword,
      userId: userId.toString(),
    };
    return this.http
      .post<SearchResults>(`http://localhost:8080/api/search`, payload)
      .pipe(debounceTime(400), delay(500));
  }
}
