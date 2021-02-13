import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { AccountService } from '../account.service';
import { AccountEntry } from '../accountEntry';
import { ServiceAccess } from '../serviceAccess';

@Component({
  selector: 'device-expire',
  templateUrl: './expire.component.html',
  styleUrls: ['./expire.component.scss']
})
export class ExpireComponent implements OnInit {

  private entry: AccountEntry;
  public expire: number = 5;
  public code: string;
  public copied: boolean = false;

  constructor(
      private accountService: AccountService,
      private dialog: MatDialogRef<ExpireComponent>,
      @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.entry = data;
  }

  ngOnInit() {
  }

  onGenerate() {
    let service: ServiceAccess = { enableShow: true, enableIdentity: true, enableProfile: true,
        enableGroup: true, enableShare: true, enablePrompt: true, enableService: true,
        enableIndex: true, enableUser: true, enableAccess: true, enableAccount: true, 
        enableAgent: true };
    this.accountService.generatePass(this.entry, this.expire * 60, service).then(p => {
      this.code = p.data;
    }).catch(err => {
      window.alert("failed to generate code");
    });
  }

  onClose() {
    this.dialog.close();
  }

  onCopy() {
    if(this.code != null) {
      this.copied = true;
      var clipArea = document.createElement("textarea");
      document.body.appendChild(clipArea);
      clipArea.value = this.code;
      clipArea.select();
      document.execCommand("copy");
      document.body.removeChild(clipArea);
      setTimeout(() => { this.copied = false; }, 1000);
    }
  }
    
}

