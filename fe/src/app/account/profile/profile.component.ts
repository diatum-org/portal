import { Component, OnInit, AfterViewInit, ViewChild, ElementRef, Input } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { Emigo } from '../emigo';
import { AccountEntry } from '../accountEntry';
import { AccountService } from '../account.service';
import { ExpireComponent } from '../expire/expire.component';
import { UsernameComponent } from '../username/username.component';

@Component({
  selector: 'account-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, AfterViewInit {

  @Input('emigo') public emigo: Emigo;
  @Input('entry') public entry: AccountEntry;

  @ViewChild('ico', {static:false}) private icon: ElementRef<any>;

  constructor(private accountService: AccountService,
      private dialog: MatDialog) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {

    if(this.emigo == null || this.emigo.logo == null || this.emigo.logo.length == 0) {
      this.icon.nativeElement.src = "assets/avatar.png";
    }
    else {
      this.icon.nativeElement.src = "data:image/png;base64," + this.emigo.logo;
    }
    this.icon.nativeElement.width = 128;
    this.icon.nativeElement.height = 128;
  }

  public getHandle(): string {
    
    let handle: string = "";
    if(this.emigo != null) {
      if(this.emigo.handle != null) {
        handle += this.emigo.handle;
      }
      if(this.emigo.registry != null) {
        if(this.emigo.registry.startsWith("https://registry.") && this.emigo.registry.endsWith("/app")) {
          handle += "@" + this.emigo.registry.substr(17, this.emigo.registry.length-21);
        }
      }
    }
    return handle;
  } 

  public onCode() {
    let dialogRef: MatDialogRef<ExpireComponent> = this.dialog.open(ExpireComponent,
        { width: '300px', data: this.entry });
  }

  public onUsername() {
    let dialogRef: MatDialogRef<UsernameComponent> = this.dialog.open(UsernameComponent,
        { width: 'min(500px, 90vw)',
        data: { entry: this.entry, emigo: this.emigo } });
  }
  
}

