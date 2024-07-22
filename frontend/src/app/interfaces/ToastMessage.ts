export interface ToastMessage {
  show: Boolean;
  message: String;
  type: 'success'|'warn'|'danger'|'info';
}

