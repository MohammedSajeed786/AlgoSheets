import { Component } from '@angular/core';
import { ToastService } from '../../services/toast.service';
import { ToastMessage } from '../../../interfaces/ToastMessage';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.css',
})
export class ToastComponent {
[x: string]: any;
  toastMessage!: ToastMessage|null ;

  constructor(private toastService: ToastService) {}
  ngOnInit() {
    this.toastService.toastMessage$.subscribe({
      next: (toastMessage: ToastMessage|null) => (this.toastMessage = toastMessage),
    });
  }
}
