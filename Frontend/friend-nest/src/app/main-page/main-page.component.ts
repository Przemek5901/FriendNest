import { Component } from '@angular/core';
import { LeftSidebarComponent } from './left-sidebar/left-sidebar.component';
import { CommentComponent } from './comment/comment.component';
import { RigthSidebarComponent } from './rigth-sidebar/rigth-sidebar.component';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [LeftSidebarComponent, CommentComponent, RigthSidebarComponent],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss',
})
export class MainPageComponent {}
