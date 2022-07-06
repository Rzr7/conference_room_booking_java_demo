# Conference Room Booking Demo Backend

Frontend: https://github.com/Rzr7/conference_room_booking_vue_demo

To run tests: `./gradlew test`

To run application: `./gradlew bootRun`

## Specification

### Auth

Base endpoint: `/api/auth`

#### Endpoints

| Method | Endpoint    | Body                                                                                                    | Description     |
|--------|-------------|---------------------------------------------------------------------------------------------------------|-----------------|
| `POST` | `/register` | `username: String`<br/>`password: String`<br/>`name: String`<br/>`birthdate: Date (format: yyyy-MM-dd)` | Create new user |
| `POST` | `/login`    | `username: String`<br/>`password: String`                                                               | Login to user   |

---

### Conference

Base endpoint: `/api/conference`

#### Endpoints

| Method   | Endpoint                            | Body                                                                                                | Description                   |
|----------|-------------------------------------|-----------------------------------------------------------------------------------------------------|-------------------------------|
| `GET`    | `/`                                 |                                                                                                     | Returns all conferences       |
| `GET`    | `/{conferenceId}`                   |                                                                                                     | Returns conference data       |
| `POST`   | `/`                                 | `name: String`<br/>`bookedAt: Date`<br/>`duration: Integer`<br/>`roomId: Integer`                   | Creates new conference        |
| `PUT`    | `/`                                 | `id: Integer`<br/>`name: String`<br/>`bookedAt: Date`<br/>`duration: Integer`<br/>`roomId: Integer` | Modify existing conference    |
| `DELETE` | `/{conferenceId}`                   |                                                                                                     | Delete conference             |
| `POST`   | `/{conferenceId}/person/{personId}` |                                                                                                     | Add person to conference      |
| `DELETE` | `/{conferenceId}/person/{personId}` |                                                                                                     | Delete person from conference |

---

### Room

Base endpoint: `/api/room`

#### Endpoints

| Method   | Endpoint                       | Body                                                          | Description                                                             |
|----------|--------------------------------|---------------------------------------------------------------|-------------------------------------------------------------------------|
| `GET`    | `/`                            |                                                               | Returns all rooms                                                       |
| `GET`    | `/{roomId}`                    |                                                               | Returns room data                                                       |
| `GET`    | `/{roomId}/booked?date={date}` |                                                               | Returns room booked times for specific date (date format: `yyyy-MM-dd`) |
| `GET`    | `/{roomId}/conferences`        |                                                               | Returns room conferences                                                |
| `POST`   | `/`                            | `name: String`<br/>`location: String`<br/>`capacity: Integer` | Creates new room                                                        |
| `DELETE` | `/{roomId}`                    |                                                               | Delete room                                                             |

---

### Person

Base endpoint: `/api/person`

#### Endpoints

| Method   | Endpoint                  | Body                                                                                                | Description                                          |
|----------|---------------------------|-----------------------------------------------------------------------------------------------------|------------------------------------------------------|
| `GET`    | `/`                       |                                                                                                     | Returns all persons/users                            |
| `GET`    | `/info`                   |                                                                                                     | Returns currently logged in user data                |
| `GET`    | `/{personId}`             |                                                                                                     | Returns person/user data                             |
| `GET`    | `/{personId}/conferences` |                                                                                                     | Returns all person/user conferences (attendee/owner) |

---
