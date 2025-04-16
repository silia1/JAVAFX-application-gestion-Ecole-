# School Management System

## Description

The School Management System is a JavaFX application designed to manage various aspects of a school's operations, including students, professors, modules, and enrollments. It utilizes Java, JavaFX for the user interface, and JDBC with MySQL for database interactions. The application follows the MVC (Model-View-Controller) architecture and implements design patterns like DAO for data access.

## Features

1. **Student Management:**
    - Add, edit, and delete student records.
    - Search students by name, matricule, or promotion.
    - Display a list of students with detailed information.
    - View modules enrolled by a student.

2. **Professor Management:**
    - Add, edit, and delete professor records.
    - Search professors by name or specialty.
    - Display professor information and assigned modules.

3. **Module Management:**
    - Add, edit, and delete modules.
    - Assign professors to modules.
    - Associate students with modules.

4. **Enrollment Management:**
    - Enroll students in modules.
    - Cancel enrollments.
    - Display students enrolled in a module.

5. **Dashboard:**
    - View statistics about the school, including total students, professors, modules, most followed modules, and professors with the most modules.

6. **User Authentication & Authorization:**
    - Secure login system with roles:
        - **Administrator:** Full access.
        - **Secretary:** Manage students and enrollments.
        - **Professor:** View assigned modules and enrolled students.

7. **Additional Features:**
    - Advanced search functionality.
    - Data export in CSV or PDF formats.
    - Notifications for important events.
    - Multilingual support (French and English).
    - Integration with external APIs for data exchange.

## Technologies Used

- **Java 17:** Core programming language.
- **JavaFX:** Building the graphical user interface.
- **JDBC:** Database connectivity.
- **MySQL:** Relational database management system.
- **Maven:** Dependency management and build automation.
- **Log4j2:** Logging framework.
- **JUnit 5:** Unit testing.

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/school-management-system.git
