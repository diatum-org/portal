import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { DeviceService } from '../device.service';

@Component({
  selector: 'device-expire',
  templateUrl: './expire.component.html',
  styleUrls: ['./expire.component.scss']
})
export class ExpireComponent implements OnInit {

  public login: string;
  public password: string;
  public mode: string;
  public emigoId: string;
  public url: string = null;
  public expire: number = 24;
  public copied: boolean = false;

  constructor(
      private deviceService: DeviceService,
      private dialog: MatDialogRef<ExpireComponent>,
      @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.mode = data.mode;
    this.login = data.login;
    this.password = data.password;
    this.emigoId = data.emigoId;
  }

  ngOnInit() {
  }

  onGenerate() {
    if(this.mode == 'create') {
      this.deviceService.addAccount(this.login, this.password, this.expire * 3600).then(l => {
        this.url = l;
      }).catch(err => {
        window.alert("failed to create link");
      });
    }
    if(this.mode == 'reset') {
      this.deviceService.resetAccount(this.login, this.password, this.expire * 3600, this.emigoId).then(l => {
        this.url = l;
      }).catch(err => {
        window.alert("failed to create link");
      });
    }
  }

  onClose() {
    this.dialog.close();
  }

  onCopy() {
    if(this.url != null) {
      this.copied = true;
      var clipArea = document.createElement("textarea");
      document.body.appendChild(clipArea);
      clipArea.value = this.url;
      clipArea.select();
      document.execCommand("copy");
      document.body.removeChild(clipArea);
      setTimeout(() => { this.copied = false; }, 1000);
    }
  }
    
}

