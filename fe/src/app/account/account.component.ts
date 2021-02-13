import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import * as pluginAnnotations from 'chartjs-plugin-annotation';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';

import { AccountService } from './account.service';
import { AccountEntry } from './accountEntry';
import { Emigo } from './emigo';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

  public mode: string = null;
  public entry: AccountEntry = null;
  public username: string = "";
  public password: string = "";
  public confirm: string = "";
  public init: boolean = false;
  private token: string;
  public busy: boolean = false;
  public emigo: Emigo;

  constructor(private dialog: MatDialog, 
      private accountService: AccountService,
      private router: Router,
      private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if(params.create != null) {
        this.token = params.create;
        this.accountService.checkToken(this.token).then(v => {
          if(v) {
            this.mode = "create";
          }
          else {
            this.mode = "invalid";
            this.loginDelay();
          }
          this.init = true;
        }).catch(err => {
          this.mode = "invalid";
          this.loginDelay();
          this.init = true;
        });
      }
      else if(params.reset != null) {
        this.token = params.reset;
        this.accountService.checkToken(this.token).then(v => {
          if(v) {
            this.mode = "reset";
          }
          else {
            this.mode = "invalid";
            this.loginDelay();
          }
          this.init = true;
        }).catch(err => {
          this.mode = "invalid";
          this.loginDelay();
          this.init = true;
        });
      }
      else {
        this.mode = "login";
        this.init = true;
      }
    });
  }

  private loginDelay() {
    setTimeout(() => {
      this.router.navigate(["/account"]);
    }, 2500);
  }

  public isConfirmed(): boolean {
    if(this.busy) {
      return false;
    }
    if(this.password == "" || this.password == null) {
      return false;
    }
    if(this.password == this.confirm) {
      return true;
    }
    return false;
  }

  public isReady(): boolean {
    if(this.busy) {
      return false;
    }
    if(!this.username.includes("@")) {
      return false;
    }
    if(this.username == "" || this.username == null) {
      return false;
    }
    if(this.password == "" || this.password == null) {
      return false;
    }
    return true;
  }

  public onCreate() {
    this.busy = true;
    this.accountService.createIdentity(this.token, this.password).then(e => {
      this.accountService.getEmigo(e).then(i => {
        this.emigo = i;
        this.entry = e;
        this.busy = false;
      }).catch(err => {
        window.alert("failed to retrieve account");
        this.busy = false;
      });
    }).catch(err => {
      window.alert("create identity failed");
      this.busy = false;
    });
  }

  public onReset() {
    this.busy = true;
    this.accountService.setIdentity(this.token, this.password).then(e => {
      this.accountService.getEmigo(e).then(i => {
        this.emigo = i;
        this.entry = e;
        this.busy = false;
      }).catch(err => {
        window.alert("failed to retrieve account");
        this.busy = false;
      });
    }).catch(err => {
      window.alert("reset identity failed");
      this.busy = false;
    });
  }

  public onLogin() {
    let name = this.username.split("@");
    this.busy = true;
    this.accountService.getId("https://registry." + name[1] + "/app", name[0]).then(i => {
      this.accountService.getIdentity(i, this.password).then(e => {
        this.accountService.getEmigo(e).then(i => {
          this.emigo = i;
          this.entry = e;
          this.busy = false;
        }).catch(err => {
          window.alert("failed to retrieve account");
          this.busy = false;
        });
      }).catch(err => {
        window.alert("login failed");
        this.busy = false;
      });
    }).catch(err => {
      window.alert("failed to retrieve id");
      this.busy = false;
    });
  }
}


