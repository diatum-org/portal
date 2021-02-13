import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { AccountService } from '../account.service';
import { AccountEntry } from '../accountEntry';
import { Emigo } from '../emigo';

@Component({
  selector: 'device-username',
  templateUrl: './username.component.html',
  styleUrls: ['./username.component.scss']
})
export class UsernameComponent implements OnInit {

  public handle: string;
  public registry: string;
  private emigo: Emigo;
  private entry: AccountEntry;
  public disabled: boolean = true;
  public alert: boolean = false;
  private debounce: number = null;
  private counter: number = 0;

  constructor(
      private accountService: AccountService,
      private dialog: MatDialogRef<UsernameComponent>,
      @Inject(MAT_DIALOG_DATA) private data: any
  ) {
    this.emigo = JSON.parse(JSON.stringify(data.emigo));
    this.entry = data.entry;

    this.handle = this.emigo.handle;
    if(this.emigo.registry != null) {
      if(this.emigo.registry.startsWith("https://registry.") && this.emigo.registry.endsWith("/app")) {
        this.registry = this.emigo.registry.substr(17, this.emigo.registry.length-21);
      }
    }
  }

  ngOnInit() {
    this.onUpdate();
  }

  onSave() {
    if((this.handle == '' || this.handle == null) && (this.registry == '' || this.registry == null)) {
      this.emigo.handle = null;
      this.emigo.registry = null;
      this.accountService.setHandle(this.entry, null).then(m => {
        this.accountService.setRegistry(this.entry, null).then(m => {
          this.data.emigo.handle = null;
          this.data.emigo.registry = null;
          this.dialog.close();
        }).catch(err => {
          window.alert("failed to set registry");
        });
      }).catch(err => {
        window.alert("failed to set username");
      });
    }
    else {
      this.emigo.handle = this.handle;
      this.emigo.registry = "https://registry." + this.registry + "/app";
    }
    this.accountService.setHandle(this.entry, this.emigo.handle).then(m => {
      this.accountService.setRegistry(this.entry, this.emigo.registry).then(m => {
        this.data.emigo.handle = this.emigo.handle;
        this.data.emigo.registry = this.emigo.registry;
        this.accountService.setMessage(this.emigo.registry, m).then(() => {
          this.dialog.close();
        }).catch(err => {
          window.alert("failed to update registry");
        });
      }).catch(err => {
        window.alert("failed to set registry");
      });
    }).catch(err => {
      window.alert("failed to set username");
    });
  }

  onCancel() {
    this.dialog.close();
  }

  onClose() {
    this.dialog.close();
  }

  onUpdate() {
    this.counter+=1;
    let cur: number = this.counter;
    this.disabled = true;
    if(this.debounce != null) {
      clearTimeout(this.debounce);
    }
    if((this.handle == '' || this.handle == null) && (this.registry == '' || this.registry == null)) {
      this.disabled = false;
      this.alert = false;
    }
    else {
      this.debounce = setTimeout(() => {
        this.debounce = null;
        this.accountService.checkHandle("https://registry." + this.registry + "/app", this.emigo.emigoId, this.handle).then(r => {
          if(this.counter == cur) {
            if(r.boolValue == true) {
              this.disabled = false;
              this.alert = false;
            }
            else {
              this.disabled = true;
              this.alert = true;
            }
          }
        }).catch(err => {
          if(this.counter == cur) {
            this.disabled = true;
            this.alert = false;
          }
        });
      }, 1000);
    }
  }

}

