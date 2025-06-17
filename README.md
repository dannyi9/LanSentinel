![Run Spring Boot Tests](https://github.com/dannyi9/LanSentinel/actions/workflows/run-tests.yml/badge.svg)

# LanSentinel

**LanSentinel** is a powerful local network asset monitoring tool designed to help network administrators and security enthusiasts efficiently track and secure their network. With real-time scanning, device classification, and vulnerability detection, **LanSentinel** ensures your network remains safe and well-monitored.

It scans your network for connected devices, identifies open ports, and detects potential vulnerabilities. Powered by Java 21 and built for scalability, **LanSentinel** is an essential tool for anyone looking to improve their network security posture.

## 🚀 Key Features

- **User-Friendly Web Interface**: Visualise the status of your network with an intuitive and responsive UI.
- **TODO - Real-Time Network Scanning**: Automatically detects and displays all active devices on your network.
- **TODO - Port Scanning & Vulnerability Detection**: Scans devices for open ports and identifies potential security threats.
- **TODO - Device Classification**: Groups devices based on identifiers like HTTP server strings, device type, and more.
- **TODO - Notifications & Alerts**: Receive real-time alerts for suspicious activities such as unknown devices or unusual port scans.
- **TODO - Customisable Scans**: Tailor your scans by selecting the IP range, port range, and notification settings.

## 🔧 Installation

### Prerequisites

- **Java 21**
- Git
- A working network connection
- Nmap installed (unless using Docker)

## ⚙️ Run
- Run backend: ```./backend/gradlew -p backend dockerBuildRun```
- Run frontend: ``cd frontend && ng serve``