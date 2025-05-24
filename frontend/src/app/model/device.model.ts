export interface Device {
  id: number;
  hostname: string;
  ipAddress: string;
  os: string;
  openPorts: number[];
  lastSeen: string;
  trusted: boolean;
}
