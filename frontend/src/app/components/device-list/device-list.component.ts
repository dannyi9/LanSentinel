import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Device} from '../../model/device.model';
import {DeviceService} from '../../service/device.service';
import {ScanService} from '../../service/scan.service';

@Component({
  selector: 'app-device-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './device-list.component.html',
  styleUrls: ['device-list.component.css']
})
export class DeviceListComponent implements OnInit {

  scanEnabled: Boolean = true;
  devices: Device[] = [];
  selectedDevice: Device | null = null;
  scanInterval: number = 0;

  constructor(private deviceService: DeviceService,
              private scanService: ScanService) {}

  ngOnInit() {
    this.scanService.getScanEnabled().subscribe({
      next: (data: Boolean) => {
        console.log('Scan is:', (data ? 'enabled' : 'disabled'));
        this.scanEnabled = data;
      },
      error: err => {
        console.error('Failed to load scan status:', err);
      }
    });

    this.deviceService.getAllDevices().subscribe({
      next: (data: Device[]) => {
        console.log('Loaded devices:', data);
        this.devices = data;
      },
      error: err => {
        console.error('Failed to load devices:', err);
      }
    });

    this.scanService.getScanInterval().subscribe({
      next: (data: number) => {
        this.scanInterval = data;
      },
      error: err => {
        console.error('Failed to get scan interval:', err);
      }
    });
  }

  selectDevice(device: Device) {
    if (device.id == null) {
      console.warn('Device has no ID, cannot fetch details:', device);
      return;
    }

    this.deviceService.getDeviceById(device.id).subscribe({
      next: (data: Device) => {
        this.selectedDevice = data;
      },
      error: err => {
        console.error('Error fetching device by ID:', err);
      }
    })
  }

  setTrustedDevice(device: Device, isTrusted: boolean) {
    if (device.id == null) {
      console.warn('Device has no ID, cannot set trusted:', device);
      return;
    }

    this.deviceService.setTrustedDeviceById(device.id, isTrusted).subscribe({
      next: (updatedDevice: Device) => {
        console.log('Device trusted status updated:', updatedDevice);
        device.trusted = updatedDevice.trusted;
      },
      error: (error) => {
        console.error('Error updating trusted status:', error);
      }
    });
  }

  toggleScan() {
    this.scanService.setScanEnabled(!this.scanEnabled).subscribe({
      next: (result: Boolean) => {
        console.log('Scan status set to:', (result ? 'ENABLED' : 'DISABLED'))
      },
      error: (error) => {
        console.error('Error setting scan status:', error);
      }
    })
    this.scanEnabled = !this.scanEnabled;
  }

  updateInterval(event: Event) {
    const value = (event.target as HTMLSelectElement).value;
    const interval = Number(value);

    this.scanService.setScanInterval(interval).subscribe({
      next: (response: number) => {
        console.log('Scan interval successfully set on backend:', response);
        this.scanInterval = response;
      },
      error: (err) => {
        console.error('Failed to set scan interval:', err);
      }
    });
  }

}


