import { Component } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-left-sidebar',
  standalone: true,
  imports: [NgOptimizedImage, ButtonModule],
  templateUrl: './left-sidebar.component.html',
  styleUrl: './left-sidebar.component.scss',
})
export class LeftSidebarComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  onLogout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }
}
