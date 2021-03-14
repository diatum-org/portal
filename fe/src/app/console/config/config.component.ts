import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PortalConfig } from '../portalConfig';
import { Domain } from '../domain';
import { ConsoleService } from '../console.service';
import { ConfirmComponent } from '../confirm/confirm.component';

@Component({
  selector: 'console-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.scss']
})
export class ConfigComponent implements OnInit {

  public nodeSet: boolean = false;
  public tokenSet: boolean = false;
  public registrySet: boolean = false;
  private ids: Set<number>;
  private token: string = null;
  public configSet: boolean = false;
  public config: PortalConfig = { emigoToken: "", emigoNode: "" };

  constructor(private consoleService: ConsoleService,
      private dialog: MatDialog,
      private dialogRef: MatDialogRef<ConfigComponent>,
      @Inject(MAT_DIALOG_DATA) private data: any) { 
    this.token = data;
    this.ids = new Set<number>();
  }

  ngOnInit(): void {
    this.consoleService.getConfig(this.token).then(c => {
      this.config = c;
      this.configSet = true;
    }).catch(err => {
      console.log(err);
      window.alert("failed to get config");
    });
  }

  public onClose() {
    this.dialogRef.close("close");
  }
  
  public onDomain(id: number) {
    this.ids.add(id);
  }

  public onNode() {
    this.nodeSet = true;
  }

  public onToken() {
    this.tokenSet = true;
  }

  public onRegistry() {
    this.registrySet = true;
  }

  public isSet(id: number): boolean {
    return this.ids.has(id);
  }

  public onEnable(d: Domain) {
    d.enabled = !d.enabled;
    this.ids.add(d.id);
  }

  public onSaveNode() {
    this.consoleService.updateNode(this.token, this.config.emigoNode).then(() => {
      this.nodeSet = false;
    }).catch(err => {
      console.log(err);
      window.alert("failed to update node");
    });
  }

  public onSaveRegistry() {
    this.consoleService.updateRegistry(this.token, this.config.defaultRegistry).then(() => {
      this.registrySet = false;
    }).catch(err => {
      console.log(err);
      window.alert("failed to update registry");
    });
  }

  public onSaveToken() {
    this.consoleService.updateToken(this.token, this.config.emigoToken).then(() => {
      this.tokenSet = false;
    }).catch(err => {
      console.log(err);
      window.alert("failed to update token");
    });
  }

  public onSaveDomain(d: Domain) {
    this.consoleService.updateDomain(this.token, d).then(z => {
      d = z;
      this.ids.delete(d.id);
    }).catch(err => {
      console.log(err);
      window.alert("failed to update domain");
    });
  }

  public onAdd() {
    this.consoleService.addDomain(this.token).then(d => {
      this.config.domains.push(d);
    }).catch(err => {
      console.log(err);
      window.alert("failed to add domain");
    });
  }
}
