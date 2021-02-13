import { Component, OnInit } from '@angular/core';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import * as pluginAnnotations from 'chartjs-plugin-annotation';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { AccountsComponent } from './accounts/accounts.component';
import { StatsComponent } from './stats/stats.component';
import { DeviceService } from './device.service';
import { DeviceEntry } from './deviceEntry';

@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.scss']
})
export class DeviceComponent implements OnInit {

  public entry: DeviceEntry = null;
  public login: string;
  public password: string;

  constructor(private dialog: MatDialog, private deviceService: DeviceService) { }

  ngOnInit(): void {
  }

  public onLogin() {
    this.deviceService.getEntry(this.login, this.password).then(e => {
      this.entry = e;
    }).catch(err => {
      window.alert("login failed");
    });
  }

  public getNode(): string {
    if(this.entry.hostname == null || this.entry.app == null || this.entry.port == null) {
      return "URL: N/A";
    }
    return "https://" + this.entry.hostname + ":" + this.entry.port + "/" + this.entry.app;
  }

  public getAddress(): string {
    if(this.entry.address == null) {
      return "IP: N/A";
    }
    return this.entry.address;
  }

  public getTimestamp(): string {
    if(this.entry.timestamp == null) {
      return "Updated: N/A";
    }
    let d: Date = new Date(this.entry.timestamp * 1000);
    return d.toUTCString();
  }

  public getStatus(): string {
    if(this.entry.status == null) {
      return "Status: N/A";
    }
    else if(this.entry.status == "failed") {
      return "Status: Error";
    }
    else if(this.entry.status == "synced") {
      return "Status: Synced";
    }
    else {
      return "Status: Updating";
    }
  }

  public onStats() {
    let dialogRef: MatDialogRef<StatsComponent> = this.dialog.open(StatsComponent, 
      { position: { left: 'calc(50% - 400px)' }, width: '800px', 
      data: { node: this.getNode(), token: this.entry.identity.statToken }});
  }

  public onAccounts() {
    let dialogRef: MatDialogRef<AccountsComponent> = this.dialog.open(AccountsComponent,
      { position: { left: 'calc(50% - 400px)' }, width: '800px', data: { node: this.getNode(), 
      token: this.entry.identity.accountToken, login: this.login, password: this.password }});
  }

}

