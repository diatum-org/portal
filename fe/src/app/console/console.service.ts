import { Injectable, Type, Component } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse, HttpEvent } from '@angular/common/http';
import { HttpUrlEncodingCodec } from '@angular/common/http';

import { PortalStat } from './portalStat';
import { PortalConfig } from './portalConfig';
import { Domain } from './domain';

@Injectable()
export class ConsoleService {

  private headers: HttpHeaders;

  constructor(private httpClient: HttpClient) {
    this.headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
  }

  public getStats(token: string): Promise<PortalStat[]> {
    return this.httpClient.get<PortalStat[]>("admin/stats?token=" + token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public getConfig(token: string): Promise<PortalConfig> {
    return this.httpClient.get<PortalConfig>("admin/config?token=" + token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public addDomain(token: string): Promise<Domain> {
    return this.httpClient.post<Domain>("admin/config/domain?token=" + token,
        { headers: this.headers, observe: 'body' }).toPromise();
  }

  public updateDomain(token: string, d: Domain): Promise<Domain> {
    return this.httpClient.put<Domain>("admin/config/domain?token=" + token,
        d, { headers: this.headers, observe: 'body' }).toPromise();
  }

  public updateNode(token: string, n: string): Promise<void> {
    return this.httpClient.put<void>("admin/config/node?token=" + token + "&value=" + encodeURIComponent(n),
        { headers: this.headers, observe: 'body' }).toPromise();
  }
  public updateToken(token: string, t: string): Promise<void> {
    return this.httpClient.put<void>("admin/config/token?token=" + token + "&value=" + encodeURIComponent(t),
        { headers: this.headers, observe: 'body' }).toPromise();
  }


}

