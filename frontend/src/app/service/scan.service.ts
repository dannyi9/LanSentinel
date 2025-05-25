import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScanService {
  private scanUrl = 'http://localhost:8080/scan';

  constructor(private http: HttpClient) {}

  getScanEnabled(): Observable<Boolean> {
    return this.http.get<Boolean>(`${this.scanUrl}/enabled`);
  }

  setScanEnabled(enabled: boolean): Observable<Boolean> {
    return this.http.get<Boolean>(`${this.scanUrl}/enable/${enabled}`);
  }

  getScanInterval(): Observable<number> {
    return this.http.get<number>(`${this.scanUrl}/interval`);
  }

  setScanInterval(intervalToSet: number): Observable<number> {
    return this.http.put<number>(`${this.scanUrl}/setinterval`, {
      interval: intervalToSet
    });
  }
}
