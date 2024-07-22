import { S } from '@angular/cdk/keycodes';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ToastMessage } from '../../interfaces/ToastMessage';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  constructor() {}

  private toastMessage: BehaviorSubject<ToastMessage | null> =
    new BehaviorSubject<ToastMessage | null>(null);

  toastMessage$ = this.toastMessage.asObservable();

  showToast(message: String, type: 'success'|'warn'|'danger'|'info') {
    this.toastMessage.next({
      show: true,
      message: message,
      type: type,
    });
    setTimeout(() => this.hideToast(), 3000);
  }

  hideToast() {
    setTimeout(() => this.toastMessage.next(null), 3000);
  }
}
