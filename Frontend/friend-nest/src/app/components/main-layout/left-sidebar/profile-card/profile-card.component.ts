import { Component, OnInit } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { MessageService } from 'primeng/api';
import { BaseComponent } from '../../../../utils/base-component';
import { User } from '../../../../models/User';

@Component({
  selector: 'app-profile-card',
  standalone: true,
  imports: [NgOptimizedImage],
  providers: [MessageService],
  templateUrl: './profile-card.component.html',
  styleUrl: './profile-card.component.scss',
})
export class ProfileCardComponent extends BaseComponent implements OnInit {
  user: User = this.getUser();

  ngOnInit() {}

  constructor() {
    super();
  }
}
