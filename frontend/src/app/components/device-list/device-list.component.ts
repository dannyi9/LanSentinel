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

}


