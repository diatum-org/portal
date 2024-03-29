import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { ConfirmComponent } from '../confirm/confirm.component';
import { ExpireComponent } from '../expire/expire.component';
import { DeviceService } from '../device.service';
import { AccountEntry } from '../accountEntry';
import { Amigo } from '../amigo';

@Component({
  selector: 'device-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  private node: string;
  private login: string;
  private password: string;
  private token: string;
  public accounts: AccountEntry[] = [];
  private entries: Map<string, Amigo>;

  constructor(private deviceService: DeviceService,
      private dialog: MatDialog,
      private dialogRef: MatDialogRef<AccountsComponent>,
      @Inject(MAT_DIALOG_DATA) private data: any) {
    this.entries = new Map<string, Amigo>(); 
    this.node = data.node;
    this.login = data.login;
    this.password = data.password;
    this.token = data.token;
  }

  ngOnInit(): void {
    this.getAccounts();
  }

  public onClose() {
    this.dialogRef.close("close");
  }

  public getAccounts() {
    this.deviceService.getAccounts(this.node, this.token).then(a => {
      this.accounts = a;

      if(a.length <= 128) {
        for(let i = 0; i < a.length; i++) {
          this.deviceService.getIdentity(this.node, this.token, a[i].amigoId).then(e => {
            this.entries.set(a[i].amigoId, e);
          }).catch(err => {
            window.alert("failed to retrieve account");
          });
        }
      }
    }).catch(err => {
      window.alert("failed to retrieve accounts");
    });
  }

  public getAccountName(id): string {
    let name: string = null;
    if(this.entries.has(id)) {
      let amigo: Amigo = this.entries.get(id);
      if(amigo != null) {
        if(amigo.registry != null && amigo.handle != null) {
          if(amigo.registry.startsWith("https://registry.") && amigo.registry.endsWith("/app")) {
            name = amigo.handle + "@" + amigo.registry.substr(17, amigo.registry.length-21);
          }
        }
      }
    }
    if(name == null) {
      return id;
    }
    return name;
  } 

  public onAdd() {
    let dialogRef: MatDialogRef<ExpireComponent> = this.dialog.open(ExpireComponent,
      { position: { left: 'calc(50% - 300px)' }, width: '600px', 
      data: { mode: 'create', login: this.login, password: this.password, amigoId: null }});
  } 

  public onReset(id: string) {
    let dialogRef: MatDialogRef<ExpireComponent> = this.dialog.open(ExpireComponent,
      { position: { left: 'calc(50% - 300px)' }, width: '600px', 
      data: { mode: 'reset', login: this.login, password: this.password, amigoId: id }});
  }

  public onEdit(id: string) {
    console.log("edit: " + id);
  }

  public onDelete(id: string) {
    let dialogRef: MatDialogRef<ConfirmComponent> = this.dialog.open(ConfirmComponent,
      { position: { left: 'calc(50% - 200px)' }, width: '400px' });
    dialogRef.afterClosed().subscribe(res => {
      if(res) {
        this.deviceService.removeAccount(this.node, this.token, id).then(() => {
          this.getAccounts();
        }).catch(err => {
          window.alert("failed to delete account");
        });
      }
    });
  }
}

 
