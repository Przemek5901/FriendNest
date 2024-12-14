import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HashtagTo } from '../models/response/HashtagTo';

@Injectable({
  providedIn: 'root',
})
export class HashtagService {
  constructor(private http: HttpClient) {}

  getHashtags(): Observable<HashtagTo[]> {
    return this.http.get<HashtagTo[]>('http://localhost:8080/api/getHashtags');
  }
}
