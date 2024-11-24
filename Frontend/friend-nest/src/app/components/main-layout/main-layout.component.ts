import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LeftSidebarComponent } from './left-sidebar/left-sidebar.component';
import { RigthSidebarComponent } from './rigth-sidebar/rigth-sidebar.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, LeftSidebarComponent, RigthSidebarComponent],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss',
})
export class MainLayoutComponent {}
