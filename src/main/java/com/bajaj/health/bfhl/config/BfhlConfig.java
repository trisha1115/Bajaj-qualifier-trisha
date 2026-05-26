package com.bajaj.health.bfhl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to read user metadata from properties.
 * Provides derived values such as format-compliant user_id.
 */
@Configuration
public class BfhlConfig {

    @Value("${app.user.fullname}")
    private String fullName;

    @Value("${app.user.dob}")
    private String dob;

    @Value("${app.user.email}")
    private String email;

    @Value("${app.user.rollnumber}")
    private String rollNumber;

    public BfhlConfig() {
    }

    public BfhlConfig(String fullName, String dob, String email, String rollNumber) {
        this.fullName = fullName;
        this.dob = dob;
        this.email = email;
        this.rollNumber = rollNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    /**
     * Generates a unique user ID following the full_name_ddmmyyyy format.
     * Sanitizes whitespaces by converting to underscores and forces lowercase.
     */
    public String getUserId() {
        if (fullName == null) {
            return "";
        }
        String sanitizedName = fullName.toLowerCase().trim().replaceAll("\\s+", "_");
        return sanitizedName + "_" + dob;
    }
}
