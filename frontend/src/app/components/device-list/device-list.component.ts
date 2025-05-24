import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Device} from '../../model/device.model';
import {DeviceService} from '../../service/device.service';

@Component({
  selector: 'app-device-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './device-list.component.html'
})
export class DeviceListComponent implements OnInit {

  devices: Device[] = [];
  selectedDevice: Device | null = null;

  constructor(private deviceService: DeviceService) {}

  ngOnInit() {
    console.log('Device List initiated');

    this.deviceService.getAllDevices().subscribe({
      next: data => {
        console.log('Loaded devices:', data);
        this.devices = data;
      },
      error: err => {
        console.error('Failed to load devices:', err);
      }
    });
  }

  selectDevice(device: Device) {
    if (device.id == null) {
      console.warn('Device has no ID, cannot fetch details:', device);
      return;
    }

    this.deviceService.getDeviceById(device.id).subscribe({
      next: data => {
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

    console.log('Setting device ID', device.id, ' as trusted:', isTrusted);

    this.deviceService.setTrustedDeviceById(device.id, isTrusted).subscribe(
      (updatedDevice: Device) => {
        console.log('Device trusted status updated:', updatedDevice);
        device.trusted = updatedDevice.trusted;
      },
      (error) => {
        console.error('Error updating trusted status:', error);
      }
    );
  }

}


