import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../utils/base-component';
import { User } from '../../../models/User';
import { PostService } from '../../../services/post.service';
import { ToastModule } from 'primeng/toast';
import { AddPostComponent } from './add-post/add-post.component';
import { PostComponent } from './post/post.component';
import { PostTo } from '../../../models/response/PostTo';
import { finalize, Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { Dictionary } from '../../../models/Dictionary';
import { categoryList } from '../../../costants/CategoryList';
import { DropdownModule } from 'primeng/dropdown';
import { sortOptions } from '../../../costants/SortOptions';
import { GetPostsRequest } from '../../../models/request/GetPostsRequest';
import { FormsModule } from '@angular/forms';
import { SpinnerService } from '../../../services/spinner.service';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    ToastModule,
    AddPostComponent,
    PostComponent,
    AsyncPipe,
    DropdownModule,
    FormsModule,
  ],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss',
})
export class MainPageComponent extends BaseComponent implements OnInit {
  user: User = this.getUser();
  posts$!: Observable<PostTo[]>;
  getPostsRequest: GetPostsRequest = {
    userId: this.user.userId,
    category: '0',
    sortOption: '1',
  };

  categories: Dictionary[] = categoryList;
  sortOptions: Dictionary[] = sortOptions;

  constructor(
    private postService: PostService,
    private spinnerService: SpinnerService,
  ) {
    super();
  }

  ngOnInit() {
    this.getPosts();
  }

  getPosts(): void {
    this.spinnerService.show();
    if (this.getPostsRequest.userId) {
      this.posts$ = this.postService
        .getPostsExceptUser(this.getPostsRequest)
        .pipe(
          this.autoUnsubscribe(),
          finalize(() => {
            this.spinnerService.hide();
          }),
        );
    }
  }
}
