import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../utils/base-component';
import { SearchService } from '../../../services/search.service';
import { FormsModule } from '@angular/forms';
import { SearchResults } from '../../../models/response/SearchResults';
import { User } from '../../../models/User';
import { Router } from '@angular/router';
import { PostTo } from '../../../models/response/PostTo';
import { HashtagService } from '../../../services/hashtag.service';
import { HashtagTo } from '../../../models/response/HashtagTo';
import { AutoCompleteModule } from 'primeng/autocomplete';

@Component({
  selector: 'app-rigth-sidebar',
  standalone: true,
  imports: [FormsModule, AutoCompleteModule],
  templateUrl: './rigth-sidebar.component.html',
  styleUrl: './rigth-sidebar.component.scss',
})
export class RigthSidebarComponent extends BaseComponent implements OnInit {
  user: User = this.getUser();
  filteredGroups: any[] = [];
  hashtagList: HashtagTo[] = [];

  selectedResult!: SearchResults;

  private lastQuery: string = '';

  constructor(
    private searchService: SearchService,
    private router: Router,
    private hashtagService: HashtagService,
  ) {
    super();
  }

  ngOnInit() {
    this.getHashatgs();
  }

  private getHashatgs(): void {
    this.hashtagService
      .getHashtags()
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (results) => (this.hashtagList = results),
        error: (err) => this.hadleHttpError(err),
      });
  }

  onInputChange(event: any) {
    const query = event.target.value.trim();

    if (query.length > 0 && query !== this.lastQuery && this.user?.userId) {
      this.lastQuery = query;
      this.searchService
        .search(query, this.user.userId)
        .pipe(this.autoUnsubscribe())
        .subscribe((response) => {
          this.filteredGroups = this.transformToGroups(response);
        });
    } else if (query.length === 0) {
      this.filteredGroups = []; //
    }
  }

  onKeydown(autoComplete: any): void {
    this.router.navigate(['/search'], {
      queryParams: { keyword: this.lastQuery },
    });
    autoComplete.hide();
  }

  transformToGroups(data: SearchResults): any[] {
    const groups = [];

    if (data.users && data.users.length > 0) {
      groups.push({
        label: 'Użytkowicy',
        value: 'Users',
        items: data.users.map((user: User) => ({
          label: user.profileName,
          value: user,
        })),
      });
    }

    if (data.posts && data.posts.length > 0) {
      groups.push({
        label: 'Posty',
        value: 'Posts',
        items: data.posts.map((post: PostTo) => ({
          label: post.post.content,
          value: post,
        })),
      });
    }

    return groups;
  }

  openPostProfile(event: any) {
    if (event?.value?.value?.post?.postId) {
      this.router.navigate(['post', event?.value.value.post.postId]);
    } else {
      this.router.navigate(['profile', event?.value?.value?.login]);
    }
  }

  searchHashtag(tag: string) {
    this.router.navigate(['/search'], {
      queryParams: { keyword: tag, focusOnPosts: true },
    });
  }
}
