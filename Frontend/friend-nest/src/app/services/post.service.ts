import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AddPostRequest } from '../models/request/AddPostRequest';
import { Observable } from 'rxjs';
import { Post } from '../models/Post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient) {}

  addPost(payload: AddPostRequest): Observable<Post> {
    return this.http.post<Post>('http://localhost:8080/api/addPost', payload);
  }
}
