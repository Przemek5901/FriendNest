import { Component } from '@angular/core';
import {Observable} from 'rxjs';
import {SpinnerService} from '../../services/spinner.service';
import {CommonModule} from '@angular/common';


@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './spinner.component.html',
  styleUrl: './spinner.component.scss'
})
export class SpinnerComponent {
  isLoading: Observable<boolean>;

  constructor(private spinnerService: SpinnerService) {
    this.isLoading = this.spinnerService.loading$;
  }
}
