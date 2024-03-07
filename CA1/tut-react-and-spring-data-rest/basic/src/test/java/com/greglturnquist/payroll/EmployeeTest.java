package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void createValidEmployee() throws InstantiationException {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 0;
        String email = "frodobaggs.provider.com";
        assertDoesNotThrow(() -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void invalidEmployeeFirstNameNull() {
        String firstName = null;
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 5;
        String email = "frodobaggs@provider.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));
    }

    @Test
    void invalidEmployeeFirstNameBlank() {
        String firstName = " ";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 5;
        String email = "frodobaggs@provider.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }

    @Test
    void invalidEmployeeLastNameNull() {
        String firstName = "Frodo";
        String lastName = null;
        String description = "Ring Bearer";
        int jobYears = 5;
        String email = "frodobaggs@provider.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }

    @Test
    void invalidEmployeeLastNameBlank() {
        String firstName = "Frodo";
        String lastName = "";
        String description = "Ring Bearer";
        int jobYears = 5;
        String email = "frodobaggs@provider.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }

    @Test
    void invalidEmployeeDescriptionNull() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = null;
        int jobYears = 5;
        String email = "frodobaggs@provider.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }

    @Test
    void invalidEmployeeDescriptionBlank() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = " ";
        int jobYears = 5;
        String email = "frodobaggs@provider.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }

    @Test
    void invalidEmployeeJobYears() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = -1;
        String email = "frodobaggs@provider.com";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }
    @Test
    void invalidEmployeeEmailNull() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 5;
        String email = null;
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }

    @Test
    void invalidEmployeeEmailBlank() {
        String firstName = "Frodo";
        String lastName = "Baggins";
        String description = "Ring Bearer";
        int jobYears = 5;
        String email = " ";
        assertThrows(InstantiationException.class, () -> new Employee(firstName, lastName, description, jobYears, email));    }

}