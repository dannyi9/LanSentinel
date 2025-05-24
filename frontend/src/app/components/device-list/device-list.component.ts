import {Component, OnInit} from '@angular/core';
import {Device} from '../../model/device.model';

@Component({
  selector: 'app-device-list',
  standalone: true,
  templateUrl: './device-list.component.html'
})
export class DeviceListComponent implements OnInit {

  devices: Device[] = [];

  constructor() {}

  ngOnInit() {
    console.log('Device List initiated');
  }

}


