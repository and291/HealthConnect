# HealthConnect Test Utility

The **HealthConnect Test Utility** is an open-source diagnostic application created to explore and
verify the data stored
within [Android Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect).
This project aimed at helping developers and curious users understand the capabilities of the Health
Connect library.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [Development Status](#development-status)
- [Technical Notes](#technical-notes)
- [Permissions](#permissions)
- [Contributing](#contributing)
- [License](#license)

## About

This project was started to explore the Health Connect library out of curiosity. It is currently in
the early stages of development and supports testing for several types of health data.

## Features

- **Heart Rate and Steps Data Access**:
    - Read Heart Rate and Steps data stored in Health Connect.
    - Write test Heart Rate and Steps records for validation.
- **Privacy Policy Dialog**:
    - A permission rationale dialog is configured to be displayed when requested by user.
- **Comprehensive SDK Status Handling**:
    - The app handles all possible HealthConnect SDK statuses to ensure robust functionality.
- **Future Plans**:
    - Expand support to additional data types.
    - Add a user-friendly interface for easier testing and analysis.

## Setup

To try the HealthConnect Test Utility:

TBD

## Usage

At this stage, the app supports:
1**Steps Data**: Reading and writing Steps records in Health Connect.

## Development Status

- **Current Phase**: Early development
- **Focus**: Testing Heart Rate and Steps permissions with Health Connect records.
- **Planned Updates**:
    - Implement a user-friendly interface.
    - Expand support for more health and fitness data types.

## Technical Notes

- **Project Scale**: The project is currently too small to introduce any formal software
  architecture.
- **Asynchronous API**: Health Connect provides an asynchronous API, but the current implementation
  in this app is incorrect and will need to be refactored in future updates.

## Permissions

The app requests the following permissions:

- **Heart Rate**: To read and write heart rate records.
- **Steps**: To read and write steps records.

### Privacy Assurance

This app does not store or transmit any personal health data. All data remains local to your device
and is used solely for testing purposes.

## Contributing

TBD

## License

TBD

---

For more information about the Health Connect library, visit the
official [Android documentation](https://developer.android.com/health-and-fitness/guides/health-connect).
