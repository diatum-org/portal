import { Component, OnInit, Inject, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import * as pluginAnnotations from 'chartjs-plugin-annotation';

import { DeviceService } from '../device.service';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.scss']
})
export class StatsComponent implements OnInit {

  public lineChartData: ChartDataSets[] = [
    { data: [] }
  ];
  public lineChartLabels: Label[] = [];

  public mode: string = "processor";
  public storageOptions: ChartOptions = { elements: { line: { borderWidth: 1 }, point: { radius: 0 } }, scales: { xAxes: [{ gridLines: { display: false } }], yAxes: [{ ticks: { min: 0, stepSize: 1024 }}] }};
  public memoryOptions: ChartOptions = { elements: { line: { borderWidth: 1 }, point: { radius: 0 } }, scales: { xAxes: [{ gridLines: { display: false } }], yAxes: [{ ticks: { min: 0, stepSize: 64 }}]}};
  public processorOptions: ChartOptions = { elements: { line: { borderWidth: 1 }, point: { radius: 0 } }, scales: { xAxes: [{ gridLines: { display: false } }], yAxes: [{ ticks: { max: 100, min: 0, stepSize: 10 } }] } };
  public requestsOptions: ChartOptions = { elements: { line: { borderWidth: 1 }, point: { radius: 0 } }, scales: { xAxes: [{ gridLines: { display: false } }], yAxes: [{ type: "logarithmic", ticks: { min: 0, stepSize: 10 } }] } };
  public accountsOptions: ChartOptions = { elements: { line: { borderWidth: 1 }, point: { radius: 0 } }, scales: { xAxes: [{ gridLines: { display: false } }], yAxes: [{ ticks: { min: 0, stepSize: 128 }}]}};

  public lineChartColors: Color[] = [
    { // grey
      backgroundColor: 'rgba(148,159,177,0.2)',
      borderColor: 'rgba(148,159,177,1)',
      pointBackgroundColor: 'rgba(148,159,177,1)',
      pointBorderColor: '#fff',
      pointHoverBackgroundColor: '#fff',
      pointHoverBorderColor: 'rgba(148,159,177,0.8)'
    }
  ];
  public lineChartOptions = { };
  public lineChartLegend = false;
  public lineChartType = 'line';
  public lineChartPlugins = [pluginAnnotations];

  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  private node: string;
  private token: string;
  private processor: number[] = [];
  private storage: number[] = [];
  private memory: number[] = [];
  private requests: number[] = [];
  private accounts: number[] = [];
  private times: number[] = []; 

  constructor(
      private deviceService: DeviceService,
      private dialogRef: MatDialogRef<StatsComponent>,
      @Inject(MAT_DIALOG_DATA) private data: any
      ) { 
    this.node = data.node;
    this.token = data.token;
  }

  ngOnInit() {
    this.deviceService.getStats(this.node, this.token).then(s => {
      this.processor = [];
      this.storage = [];
      this.memory = [];
      this.lineChartLabels = [];
      for(let i = 0; i < s.length; i++) {
        this.processor.unshift(s[i].processor);
        this.storage.unshift(Math.floor(s[i].storage / 1024));
        this.memory.unshift(Math.floor(s[i].memory / 1024));
        this.requests.unshift(s[i].requests);
        this.accounts.unshift(s[i].accounts);
        this.lineChartLabels.unshift('');
      }
      if(this.mode == "processor") { 
        this.lineChartData = [ { data: this.processor } ];
        this.lineChartOptions = this.processorOptions;
      }
      if(this.mode == "storage") {
        this.lineChartData = [ { data: this.storage } ];
        this.lineChartOptions = this.storageOptions;
      }
      if(this.mode == "requests") {
        this.lineChartData = [ { data: this.requests } ];
        this.lineChartOptions = this.requestsOptions; 
      }
      if(this.mode == "accounts") {
        this.lineChartData = [ { data: this.accounts } ];
        this.lineChartOptions = this.accountsOptions; 
      }
      if(this.mode == "memory") {
        this.lineChartData = [ { data: this.memory } ];
        this.lineChartOptions = this.memoryOptions; 
      }
    }).catch(err => {
      window.alert("failed to get stats");
    });
  }

  onMode(m: string) {
    this.mode = m;
    if(this.mode == "processor") { 
      this.lineChartData = [ { data: this.processor } ];
      this.lineChartOptions = this.processorOptions;
    }
    if(this.mode == "storage") {
      this.lineChartData = [ { data: this.storage } ];
      this.lineChartOptions = this.storageOptions;
    }
    if(this.mode == "memory") {
      this.lineChartData = [ { data: this.memory } ];
      this.lineChartOptions = this.memoryOptions; 
    }
    if(this.mode == "accounts") {
      this.lineChartData = [ { data: this.accounts } ];
      this.lineChartOptions = this.accountsOptions; 
    }
    if(this.mode == "requests") {
      this.lineChartData = [ { data: this.requests } ];
      this.lineChartOptions = this.requestsOptions; 
    }
  }

  onClose() {
    this.dialogRef.close("close");
  }

}
