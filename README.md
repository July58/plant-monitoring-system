# Plant Monitoring System 🌱

A Spring Boot application for **plant monitoring and management**, featuring **sensor data aggregation**, **user authentication**, and **photo gallery support**.

---

## Features

### **User Authentication & Management**
- Secure sign-up and sign-in with JWT token-based authentication.

### **Plant CRUD Operations**
- Create, update, and delete plant records.
- Assign plants to specific users (ownership).
- Store plant metadata: name, type, location, planting date, and custom info.

### **Sensor Data Collection & Aggregation**
- Collect real-time sensor data: temperature, humidity, soil moisture, light intensity, DS18B20 temperature.
- Store historical sensor readings for each plant.
- Aggregate sensor data over custom intervals (daily, weekly, etc.) for analytics.
- Export sensor data to CSV for offline analysis.

### **Journal & Notes**
- Add, edit, and delete notes for each plant.
- Timestamped journal entries for tracking plant care and observations.
- Link notes to specific users and plants.

### **Photo Gallery & Camera Integration**
- Upload and store plant photos in organized directories by plant and date.
- View photo galleries for each plant.
- Stream live camera feeds (if hardware is connected).
- Store images in a structured format for easy retrieval.

### **API Documentation**
- Comprehensive OpenAPI 3.0 specification (`open-api.yaml`) for all endpoints.

---

## Prerequisites

- Java 17+
- Maven
- Docker (for PostgreSQL)

---

## Setup

1. **Clone the repository:**

```bash
git clone 
cd plant_system
```

2. **Start PostgreSQL with Docker**:**

```bash
docker-compose up -d
```
3. **Configure application properties:**

Edit src/main/resources/application.properties with your credentials

4. **Build and run the application:** 

```bash
mvn clean install
mvn spring-boot:run
```
API Documentation
You can view the OpenAPI specification here: [Plant System API Docs](https://July58.github.io/plant-monitoring-system/main/redoc.html)

