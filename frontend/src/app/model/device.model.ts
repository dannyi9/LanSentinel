export interface Device {
  id: number;
  hostname: string;
  ipAddress: string;
  os: string;
  openPorts: number[];
  lastSeen: string;
  isTrusted: boolean;
  isOnline: boolean;
  createdAt: string;
}
