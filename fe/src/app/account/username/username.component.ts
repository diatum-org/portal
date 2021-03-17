import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { AccountService } from '../account.service';
import { AccountEntry } from '../accountEntry';
import { Amigo } from '../amigo';

@Component({
  selector: 'device-username',
  templateUrl: './username.component.html',
  styleUrls: ['./username.component.scss']
})
export class UsernameComponent implements OnInit {

  public handle: string;
  public registry: string;
  private amigo: Amigo;
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
    this.amigo = JSON.parse(JSON.stringify(data.amigo));
    this.entry = data.entry;

    this.handle = this.amigo.handle;
    if(this.amigo.registry != null) {
      if(this.amigo.registry.startsWith("https://registry.") && this.amigo.registry.endsWith("/app")) {
        this.registry = this.amigo.registry.substr(17, this.amigo.registry.length-21);
      }
    }
  }

  ngOnInit() {
    this.onUpdate();
  }

  onSave() {
    if((this.handle == '' || this.handle == null) && (this.registry == '' || this.registry == null)) {
      this.amigo.handle = null;
      this.amigo.registry = null;
      this.accountService.setHandle(this.entry, null).then(m => {
        this.accountService.setRegistry(this.entry, null).then(m => {
          this.data.amigo.handle = null;
          this.data.amigo.registry = null;
          this.dialog.close();
        }).catch(err => {
          window.alert("failed to set registry");
        });
      }).catch(err => {
        window.alert("failed to set username");
      });
    }
    else {
      this.amigo.handle = this.handle;
      this.amigo.registry = "https://registry." + this.registry + "/app";
    }
    this.accountService.setHandle(this.entry, this.amigo.handle).then(m => {
      this.accountService.setRegistry(this.entry, this.amigo.registry).then(m => {
        this.data.amigo.handle = this.amigo.handle;
        this.data.amigo.registry = this.amigo.registry;
        this.accountService.setMessage(this.amigo.registry, m).then(() => {
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
        this.accountService.checkHandle("https://registry." + this.registry + "/app", this.amigo.amigoId, this.handle).then(r => {
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

