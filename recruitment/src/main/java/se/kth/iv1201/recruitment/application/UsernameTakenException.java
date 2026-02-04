/**
 * Thrown when user tries to register with a username that already exists.
 * Shown on form
 */

package se.kth.iv1201.recruitment.application;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String username) {
        super("Username already taken: " + username);
    }
}