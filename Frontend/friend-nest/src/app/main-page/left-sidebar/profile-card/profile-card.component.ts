import { Component, OnInit } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { BaseComponent } from '../../../utils/base-component';
import { MessageService } from 'primeng/api';
import { User } from '../../../models/User';

@Component({
  selector: 'app-profile-card',
  standalone: true,
  imports: [NgOptimizedImage],
  providers: [MessageService],
  templateUrl: './profile-card.component.html',
  styleUrl: './profile-card.component.scss',
})
export class ProfileCardComponent extends BaseComponent implements OnInit {
  user: User = {};

  constructor(messageService: MessageService) {
    super(messageService);
  }

  ngOnInit() {
    this.user = this.getUser();
  }
}
