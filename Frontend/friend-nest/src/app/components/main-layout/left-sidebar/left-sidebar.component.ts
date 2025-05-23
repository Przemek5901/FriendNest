import { Component } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Router, RouterLink } from '@angular/router';
import { ProfileCardComponent } from './profile-card/profile-card.component';
import { AuthService } from '../../../services/auth.service';
import { BaseComponent } from '../../../utils/base-component';

@Component({
  selector: 'app-left-sidebar',
  standalone: true,
  imports: [NgOptimizedImage, ButtonModule, ProfileCardComponent, RouterLink],
  templateUrl: './left-sidebar.component.html',
  styleUrl: './left-sidebar.component.scss',
})
export class LeftSidebarComponent extends BaseComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
  ) {
    super();
  }

  onLogout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }

  openProfile(): void {
    this.router.navigate(['profile', this.getUser().login]);
  }

  navigateToMainPage(): void {
    this.router.navigate(['/main-page'], {
      queryParams: { focus: true },
    });
  }
}
