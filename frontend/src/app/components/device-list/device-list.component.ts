import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-device-list',
  standalone: true,
  templateUrl: './device-list.component.html'
})
export class DeviceListComponent implements OnInit {

  constructor() {}

  ngOnInit() {
    console.log('Device List initiated');
  }

}


