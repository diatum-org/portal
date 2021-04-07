import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import * as pluginAnnotations from 'chartjs-plugin-annotation';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';

import { ExpireComponent } from './expire/expire.component';
import { AccountService } from './account.service';
import { AccountEntry } from './accountEntry';
import { Amigo } from './amigo';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

  public mode: string = null;
  public entry: AccountEntry = null;
  public defaultRegistry: string = null;
  public password: string = "";
  public username: string = "";
  public registry: string = "";
  public confirm: string = "";
  public init: boolean = false;
  private token: string;
  public busy: boolean = false;
  public amigo: Amigo;
  public passwordType: string = "password";
  public confirmType: string = "password";
  public available: boolean = false;
  public alert: boolean = false;
  private counter: number = 0;
  private debounce: number = null;

  constructor(private dialog: MatDialog, 
      private accountService: AccountService,
      private router: Router,
      private route: ActivatedRoute) { }

  ngOnInit(): void {
    
    // get default registry
    this.accountService.getDefaultRegistry().then(r => {
      this.defaultRegistry = r;
      console.log("default registry: " + r);
    }).catch(err => {
      console.log("failed to get default registry");
    });

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
    if(this.defaultRegistry == null && !this.username.includes("@")) {
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

  public setPasswordType(t: string) {
    this.passwordType = t;
  }

  public setConfirmType(t: string) {
    this.confirmType = t;
  }

  public onCreate() {
    this.busy = true;
    this.accountService.createIdentity(this.token, this.password).then(e => {
      this.accountService.getAmigo(e).then(i => {
        this.amigo = i;
        this.entry = e;
        this.busy = false;
        this.username = i.name;
        if(i.registry.length >= 21 && i.registry.startsWith("https://registry.") && i.registry.endsWith("/app")) {
          this.registry = i.registry.substring(17, i.registry.length - 4);
        }
        else {
          this.registry = this.defaultRegistry;
        } 
        window.scrollTo(0,0);
      }).catch(err => {
        window.alert("failed to retrieve account");
        this.busy = false;
      });
    }).catch(err => {
      let details: string = err.headers.get("details");
      window.alert("create identity failed: " + details);
      this.busy = false;
    });
  }

  public onSave() {
    this.busy = true;
    this.accountService.setHandle(this.entry, this.username).then(m => {
      let reg = "https://registry." + this.registry + "/app";
      this.accountService.setRegistry(this.entry, reg).then(m => {
        this.accountService.setMessage(reg, m).then(() => {
          this.busy = false;
          this.amigo.handle = this.username;
          this.amigo.registry = reg;
          window.scrollTo(0,0);
        }).catch(err => {
          this.busy = false;
          window.alert("failed to update registry");
        });
      }).catch(err => {
        this.busy = false;
        window.alert("failed to set registry");
      });
    }).catch(err => {
      this.busy = false;
      window.alert("failed to set username");
    });
  }

  public onCode() {
    let dialogRef: MatDialogRef<ExpireComponent> = this.dialog.open(ExpireComponent,
        { width: '300px', data: this.entry });
  }

  onUsername() {
    this.available = false;
    this.alert = false;
    this.counter ++;
    let cur: number = this.counter;
    if(this.debounce != null) {
      clearTimeout(this.debounce);
    }

    // dont save empty username
    if(this.username == null || this.username == '') {
      return;
    }
    if(this.registry == null || this.registry == '') {
      return;
    }

    // save username
    this.debounce = setTimeout(() => {
      this.debounce = null;
      this.accountService.checkHandle("https://registry." + this.registry + "/app", this.amigo.amigoId, 
          this.username).then(r => {
        if(this.counter == cur) {
          if(r.boolValue) {
            this.available = true;
            this.alert = false;
          }
          else {
            this.available = false;
            this.alert = true;
          }
        }
      }).catch(err => {
        if(this.counter == cur) {
          this.available = false;
          this.alert = true;
        }
      });
    }, 500);
  }

  hasUsername() {
    if(this.amigo == null || this.amigo.handle == null) {
      return false;
    }
    return true;
  }

  public onReset() {
    this.busy = true;
    this.accountService.setIdentity(this.token, this.password).then(e => {
      this.accountService.getAmigo(e).then(i => {
        this.amigo = i;
        this.entry = e;
        this.busy = false;
      }).catch(err => {
        window.alert("failed to retrieve account");
        this.busy = false;
      });
    }).catch(err => {
      let details: string = err.headers.get("details");
      window.alert("reset identity failed: " + details);
      this.busy = false;
    });
  }

  public onLogin() {
    let name: string = "";
    let registry: string = "";
    if(this.username.includes("@")) {
      let split = this.username.split("@");
      name = split[0];
      registry = split[1];
    }
    else {
      name = this.username;
      registry = this.defaultRegistry;
    }
    this.busy = true;
    this.accountService.getId("https://registry." + registry + "/app", name).then(i => {
      this.accountService.getIdentity(i, this.password).then(e => {
        this.accountService.getAmigo(e).then(i => {
          this.amigo = i;
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

  public isIos() {
    if(navigator.userAgent.match(/iPhone|iPad|iPod/i)) {
      return true;
    }
    return false;
  }

  public isAndroid(): boolean {
    if(navigator.userAgent.match(/Android/i)) {
      return true;
    }
    return false;
  }

  public isWeb(): boolean {
    if(this.isIos() || this.isAndroid()) {
      return false;
    }
    return true;
  }

}


