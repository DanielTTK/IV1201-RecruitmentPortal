package se.kth.iv1201.recruitment.util;

/**
 * Utility class for validating the data of an account before it is persisted to the database.
 * This class is used by the AccountService to ensure that the account data meets the necessary criteria before it is saved.
 */
public final class AccountPersistenceValidator {

    private AccountPersistenceValidator() {
    }

    /**
     * Validates the registration data for a new account.
     *
     * @param firstName    the first name of the user
     * @param lastName     the last name of the user
     * @param username     the username for the account
     * @param email          the email address for the account
     * @param rawPassword  the raw password for the account
     * @throws IllegalArgumentException if any of the provided data is invalid
     */

    public static void validateRegistrationData(String firstName, String lastName,
                                                String username, String email, String rawPassword) {

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (email == null || email.isBlank()
                || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (rawPassword == null || rawPassword.length() < 6 || rawPassword.length() > 100) {
            throw new IllegalArgumentException("Password must be 6-100 characters");
        }
    }

    /**
     * Validates and normalizes a swedish personal identification number (personnummer).
     * The input can be in various formats (like "YYYYMMDD-XXXX", "YYMMDDXXXX", "YYYYMMDDXXXX") and will be normalized to the format "YYYYMMDD-XXXX"
     * Client-side validation may be more strict. 
     * 
     * @param personNumber the personal identification number to validate and normalize
     * @return the normalized personal identification number in the format "YYYYMMDD-XXXX"
     * @throws IllegalArgumentException if the input is null, blank, or cannot be parsed into a valid personnummer format
     */
    public static String validateAndNormalizePnr(String personNumber) {
        if (personNumber == null || personNumber.isBlank()) {
            throw new IllegalArgumentException("Person number is required");
        }

        String digits = personNumber.replaceAll("\\D", "");

        if (digits.length() != 12) {
            throw new IllegalArgumentException("Person number must be YYYYMMDD-XXXX");
        }

        String normalizedPnr = digits.substring(0, 8) + "-" + digits.substring(8);

        if (!normalizedPnr.matches("\\d{8}-\\d{4}")) {
            throw new IllegalArgumentException("Person number must be YYYYMMDD-XXXX");
        }

        return normalizedPnr;
    }
}