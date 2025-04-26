
# ⚡ Electricity Bill Management System

A web-based application to manage electricity customers, their bills, payment status, and downloads of electricity bills in PDF format.  
Built using **Java Servlet**, **MySQL**, **HTML/CSS/JS**, and **iTextPDF 5.5.13.3**.

---

## 🛠 Features

- Admin and Customer login functionality
- Customer registration and approval system
- Customer dashboard showing bill details
- Download electricity bills as **PDF** (named with `meter_no_date.pdf`)
- Pay pending bills directly from the customer dashboard
- Logout functionality
- Glassmorphism UI design
- Session management and basic validations

---

## 📁 Technologies Used

| Technology | Purpose                    |
|------------|-----------------------------|
| Java       | Backend (Servlets)           |
| MySQL      | Database                     |
| HTML/CSS   | Frontend with Glassmorphism  |
| JavaScript | Dynamic data fetching (Fetch API) |
| iTextPDF (v5.5.13.3) | PDF Generation    |
| Apache Tomcat 10  | Server deployment     |

---

## ⚙ Project Structure

```
ElectricityBillManagement/
├── src/
│   ├── DBConnection.java
│   ├── LoginServlet.java
│   ├── LogoutServlet.java
│   ├── CustomerDashboardServlet.java
│   ├── DownloadBillServlet.java
│   └── (Other servlets)
├── WebContent/
│   ├── login.html
│   ├── customerDashboard.html
│   ├── adminDashboard.html
│   ├── css/
│   │   └── styles.css
│   ├── js/
│   │   └── popup.js
├── WEB-INF/
│   └── web.xml
└── lib/
    └── itextpdf-5.5.13.3.jar
```

---

## 📦 How to Run

1. **Database Setup**
   - Create a MySQL database (e.g., `electricity`).
   - Create necessary tables: `admin`, `customer`, `bill`.
   - Insert admin credentials manually into the `admin` table.

2. **Import Project**
   - Import into **Eclipse IDE** or any Java EE IDE.
   - Make sure Tomcat 10 server is configured.

3. **Add Libraries**
   - Add `itextpdf-5.5.13.3.jar` inside `WEB-INF/lib` folder.

4. **Configure Database Connection**
   - Check `DBConnection.java` and update your MySQL username, password, and URL if needed.

5. **Deploy and Run**
   - Start Tomcat Server.
   - Access the application at:  
     `http://localhost:8080/ElectricityBillManagement/login.html`

---

## 📋 Database Tables (Sample)

### Table: `admin`

| Column    | Type     |
|-----------|----------|
| id        | INT (PK) |
| username  | VARCHAR  |
| email     | VARCHAR  |
| password  | VARCHAR  |

### Table: `customer`

| Column      | Type     |
|-------------|----------|
| id          | INT (PK) |
| name        | VARCHAR  |
| email       | VARCHAR  |
| mobile_no   | VARCHAR  |
| meter_no    | VARCHAR  |
| password    | VARCHAR  |
| status      | ENUM('pending','approved') |

### Table: `bill`

| Column     | Type     |
|------------|----------|
| id         | INT (PK) |
| meter_no   | VARCHAR  |
| month      | VARCHAR  |
| units      | INT      |
| amount     | DOUBLE   |
| status     | ENUM('paid','unpaid','pending') |

---

## 📸 Screenshots

| Page | Description |
|-----|--------------|
| Login | Glassmorphism styled login page for Admin and Customers |
| Customer Dashboard | Bill summary with Download & Pay options |
| Admin Dashboard | Customer approval and bill management |

---

## ✨ Author

> **Created by Danesh Kadappa Hosur**
