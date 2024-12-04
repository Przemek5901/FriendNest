import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AddPostRequest } from '../models/request/AddPostRequest';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  constructor(private http: HttpClient) {}

  addComment(addPostRequest: AddPostRequest): Observable<Comment> {
    return this.http.post<Comment>(
      'http://localhost:8080/api/addComment',
      addPostRequest,
    );
  }

  deleteComment(commentId: number): Observable<Comment> {
    return this.http.delete<Comment>(
      `http://localhost:8080/api/deleteComment/${commentId}`,
    );
  }
}
