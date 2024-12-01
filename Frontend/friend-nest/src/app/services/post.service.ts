import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AddPostRequest } from '../models/request/AddPostRequest';
import { Observable } from 'rxjs';
import { Post } from '../models/Post';
import { PostTo } from '../models/response/PostTo';
import { GetPostsRequest } from '../models/request/GetPostsRequest';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient) {}

  addPost(payload: AddPostRequest): Observable<Post> {
    return this.http.post<Post>('http://localhost:8080/api/addPost', payload);
  }

  getPostsExceptUser(getPostsRequest: GetPostsRequest): Observable<PostTo[]> {
    return this.http.post<PostTo[]>(
      'http://localhost:8080/api/getPostsExceptUser',
      getPostsRequest,
    );
  }
}
