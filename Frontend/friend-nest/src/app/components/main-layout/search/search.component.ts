import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../utils/base-component';
import { AddPostComponent } from '../main-page/add-post/add-post.component';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { SearchService } from '../../../services/search.service';
import { SearchResults } from '../../../models/response/SearchResults';
import { User } from '../../../models/User';
import { PostComponent } from '../main-page/post/post.component';
import { TabViewModule } from 'primeng/tabview';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [
    AddPostComponent,
    ToastModule,
    InputTextModule,
    PaginatorModule,
    ReactiveFormsModule,
    IconFieldModule,
    InputIconModule,
    PostComponent,
    TabViewModule,
    NgOptimizedImage,
    RouterLink,
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss',
})
export class SearchComponent extends BaseComponent implements OnInit {
  user: User = this.getUser();
  keyword: string | null = '';
  searchResults: SearchResults = { users: [], posts: [] };

  constructor(
    private searchService: SearchService,
    private route: ActivatedRoute,
    private router: Router,
  ) {
    super();
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe((params) => {
      this.keyword = params.get('keyword');
      if (this.keyword) {
        this.search();
      }
    });
  }

  search() {
    if (this.keyword && this.keyword?.length > 0 && this.user.userId) {
      this.searchService
        .search(this.keyword, this.user.userId)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (res) => this.respondToSearch(res),
          error: (err) => this.hadleHttpError(err),
        });
    }
  }

  private respondToSearch(res: SearchResults) {
    this.searchResults = res;
  }

  openPost(postId: number) {
    this.router.navigate(['post', postId]);
  }
}
