# DoctorHolidays

## API Documentation

### Endpoints

#### 1. GET /doctor-holidays

- **Description**: Retrieves the holidays for a specific doctor at a specific clinic.
- **Query Parameters**:
  - `doctorId`: The ID of the doctor.
  - `clinicId`: The ID of the clinic.
- **Sample API Call**:
  ```
  GET /doctor-holidays?doctorId=123&clinicId=456
  ```
- **Expected Results**:
  - **200 OK**:
    ```json
    {
      "id": "someObjectId",
      "doctorId": "123",
      "clinicId": "456",
      "holidays": [
        {
          "holidayDate": "2023-10-01T00:00:00.000+00:00",
          "reason": "Vacation",
          "frequency": "Annual"
        }
      ]
    }
    ```
  - **400 Bad Request**:
    ```json
    "Missing doctorId or clinicId query parameters."
    ```
  - **404 Not Found**:
    ```json
    "No holidays found for this doctor and clinic."
    ```

#### 2. PUT /doctor-holidays/add

- **Description**: Adds a holiday for a specific doctor at a specific clinic.
- **Request Body**: A `DoctorHoliday` object containing:
  - `doctorId`: The ID of the doctor.
  - `clinicId`: The ID of the clinic.
  - `holidays`: A list of `Holiday` objects.
- **Sample API Call**:

  ```
  PUT /doctor-holidays/add
  Content-Type: application/json

  {
    "doctorId": "123",
    "clinicId": "456",
    "holidays": [
      {
        "holidayDate": "2023-10-01T00:00:00.000+00:00",
        "reason": "Vacation",
        "frequency": "Annual"
      }
    ]
  }
  ```

- **Expected Results**:
  - **200 OK**:
    ```json
    {
      "id": "someObjectId",
      "doctorId": "123",
      "clinicId": "456",
      "holidays": [
        {
          "holidayDate": "2023-10-01T00:00:00.000+00:00",
          "reason": "Vacation",
          "frequency": "Annual"
        }
      ]
    }
    ```
  - **400 Bad Request**:
    ```json
    "Invalid request data."
    ```
  - **409 Conflict**:
    ```json
    "Server error."
    ```
  - **500 Internal Server Error**:
    ```json
    "Server error."
    ```

### FileCompression Endpoints

#### 1. POST /rarpdf/{username}/compress

- **Description**: Compresses a file uploaded by the user into a ZIP format.
- **Path Variable**:
  - `username`: The username of the user uploading the file.
- **Request Parameter**:
  - `file`: The file to be compressed (MultipartFile).
- **Sample API Call**:

  ```
  POST /rarpdf/user123/compress
  Content-Type: multipart/form-data

  {
    "file": [PDF file to compress]
  }
  ```

- **Expected Results**:
  - **200 OK**:
    ```
    "File successfully compressed and stored in the list."
    ```
  - **500 Internal Server Error**:
    ```
    "Error compressing file to RAR: [error message]"
    ```

#### 2. GET /rarpdf/{username}/list

- **Description**: Retrieves a list of all ZIP files associated with the user.
- **Sample API Call**:
  ```
  GET /rarpdf/user123/list
  ```
- **Expected Results**:
  - **200 OK**: Returns a list of `ZIPFile` objects.

#### 3. GET /rarpdf/{username}/file/{index}

- **Description**: Retrieves a specific ZIP file based on its index.
- **Path Variable**:
  - `index`: The index of the ZIP file.
- **Sample API Call**:
  ```
  GET /rarpdf/user123/file/0
  ```
- **Expected Results**:
  - **200 OK**: Returns the `ZIPFile` object.
  - **404 Not Found**: If the file does not exist.

#### 4. GET /rarpdf/{username}/viewrar/{index}

- **Description**: Views a specific ZIP file based on its index.
- **Path Variable**:
  - `index`: The index of the ZIP file.
- **Sample API Call**:
  ```
  GET /rarpdf/user123/viewrar/0
  ```
- **Expected Results**:
  - **200 OK**: Returns the ZIP file content.
  - **404 Not Found**: If the file does not exist.
  - **500 Internal Server Error**: If an error occurs.

#### 5. GET /rarpdf/{username}/view/{index}

- **Description**: Views a specific PDF file extracted from a ZIP file based on its index.
- **Path Variable**:
  - `index`: The index of the ZIP file.
- **Sample API Call**:
  ```
  GET /rarpdf/user123/view/0
  ```
- **Expected Results**:
  - **200 OK**: Returns the extracted PDF file content.
  - **404 Not Found**: If the file does not exist.
  - **500 Internal Server Error**: If an error occurs.

### SearchEngine Endpoints

#### 1. GET /doctors

- **Description**: Triggers a re-indexing of doctor data.
- **Sample API Call**:
  ```
  GET /doctors
  ```
- **Expected Results**:
  - **200 OK**: The data synchronization process is initiated.

#### Search Example

To search for doctors based on various fields, you can use the following `curl` command:

```bash
curl --location --request GET 'http://ec2-13-232-76-117.ap-south-1.compute.amazonaws.com:9200/doctors/_search' \
--header 'Content-Type: application/json' \
--data '{
  "query": {
    "multi_match": {
      "query": "car",
      "type": "bool_prefix",
      "fields": [
        "name",
        "specialization",
        "qualification"
      ],
      "fuzziness": "AUTO",
      "operator": "or"
    }
  }
}'
```

### TimeSlots Endpoints

#### 1. GET /api/doctor/{doctorId}

- **Description**: Retrieves the availability of a specific doctor at a specific clinic.
- **Path Variable**:
  - `doctorId`: The ID of the doctor.
- **Request Parameter**:
  - `clinicId`: The ID of the clinic.
- **Sample API Call**:
  ```
  GET /api/doctor/123?clinicId=456
  ```
- **Expected Results**:
  - **200 OK**: Returns the `AvailableDays` object if found.
  - **400 Bad Request**: If `clinicId` is missing or empty.
  - **404 Not Found**: If no availability data is found for the specified doctor and clinic.

#### 2. PUT /api/doctor

- **Description**: Updates the time slot availability for a specific doctor.
- **Request Body**: An `AvailableDays` object containing the updated availability information.
- **Sample API Call**:

  ```
  PUT /api/doctor
  Content-Type: application/json

  {
    "doctorId": "123",
    "clinicId": "456",
    "availableDays": [
      {
        "day": "Monday",
        "timeSlots": ["09:00-10:00", "10:00-11:00"]
      }
    ]
  }
  ```

- **Expected Results**:
  - **200 OK**:
    ```
    "TimeSlot updated successfully"
    ```
  - **404 Not Found**:
    ```
    "Doctor, clinic, day, or timeslot not found"
    ```
  - **500 Internal Server Error**:
    ```
    "Server error: [error message]"
    ```
