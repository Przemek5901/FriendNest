import { Component } from '@angular/core';
import { CommentComponent } from './comment/comment.component';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [CommentComponent],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss',
})
export class MainPageComponent {}
