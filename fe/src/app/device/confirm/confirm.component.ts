import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'device-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent implements OnInit {

  constructor(
      private dialog: MatDialogRef<ConfirmComponent>,
      @Inject(MAT_DIALOG_DATA) private data: any
  ) {
  }

  ngOnInit() {
  }

  onConfirm() {
    this.dialog.close(true);
  }

  onCancel() {
    this.dialog.close(false);
  }

  onClose() {
    this.dialog.close(false);
  }

}

