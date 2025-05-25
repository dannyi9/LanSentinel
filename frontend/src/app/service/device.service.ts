import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Device} from '../model/device.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {
  private apiUrl = 'http://localhost:8080/devices';
  private scanUrl = 'http://localhost:8080/scan';

  constructor(private http: HttpClient) {}

  getAllDevices(): Observable<Device[]> {
    return this.http.get<Device[]>(`${this.apiUrl}/all`);
  }

  getDeviceById(id: number): Observable<Device> {
    return this.http.get<Device>(`${this.apiUrl}/${id}`);
  }

  setTrustedDeviceById(id: number, trusted: boolean): Observable<Device> {
    return this.http.put<Device>(`${this.apiUrl}/${id}/trust`, null, {
      params: { trusted: trusted.toString() }
    });
  }

}
