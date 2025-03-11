# cinema-tickets-java

## Overview

This project is a solution to the Department for Work and Pensions (DWP) Java Software Engineer coding exercise. It implements a `TicketService` that adheres to the business rules, constraints, and assumptions provided in the instructions.

## Requirements

- **Java Version:** 11 or later
- **GitHub Repository:** Must be public for assessment
- **Submission Deadline:** Within 5 working days of receiving the assignment

## Business Rules

- There are three types of tickets:
  - **Infant:** £0 (No seat allocated, sits on an Adult's lap)
  - **Child:** £15
  - **Adult:** £25
- A maximum of **25 tickets** can be purchased in one transaction.
- **Infant and Child tickets cannot be purchased without at least one Adult ticket.**
- Payments are processed through the provided `TicketPaymentService`.
- Seat reservations are handled by the `SeatReservationService`.

## Constraints

- The `TicketService` interface **CANNOT** be modified.
- Code within `thirdparty.*` packages **CANNOT** be modified.
- The `TicketTypeRequest` class **MUST** be immutable.

## Assumptions

- All accounts with an ID greater than zero are valid and have sufficient funds.
- The `TicketPaymentService` and `SeatReservationService` work correctly without defects.

## Implementation

This solution ensures:

- Proper validation of ticket requests to comply with business rules.
- Calculation of the correct payment amount and invocation of `TicketPaymentService`.
- Determination of seat reservations and interaction with `SeatReservationService`.
- Rejection of invalid ticket requests based on given constraints.
