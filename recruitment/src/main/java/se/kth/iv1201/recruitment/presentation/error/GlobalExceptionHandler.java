package se.kth.iv1201.recruitment.presentation.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handling for the presentation layer.
 *
 * Shows a more nice error page instead of the Whitelabel page/stacktrace. 
 * Also logs the error together with request method + URI.
 *
 * Handles 503 (DB/data access issues), 404 (missing routes/resources) and a fallback for unexpected errors (500).
 *
 * All handlers return the same Thymeleaf view: {@code error/error}.
 */


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Handles DB-related failures, like when the DB is down or a query fails.
     *
     * @param exception The thrown database-related exception.
     * @param request   The current HTTP request (used for logging and showing the path).
     * @param model     Model attributes used by the error view.
     * @return The Thymeleaf view name for the error page.
     */
    @ExceptionHandler({ CannotGetJdbcConnectionException.class, DataAccessException.class })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleDB(Exception exception, HttpServletRequest request, Model model) {
        log.error("DB-problem at {} {}", request.getMethod(), request.getRequestURI(), exception);

        model.addAttribute("status", 503);
        model.addAttribute("title", "Databasen är nere!");
        model.addAttribute("message", "Just nu går det inte att nå databasen. Försök igen om en stund.");
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }


    /**
     * Handles cases where the requested route or static resource does not exist.
     *
     * @param exception The thrown not-found exception.
     * @param request   The current HTTP request (used for logging and showing the path).
     * @param model     Model attributes used by the error view.
     * @return The Thymeleaf view name for the error page.
     */
    @ExceptionHandler({ NoHandlerFoundException.class, NoResourceFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception exception, HttpServletRequest request, Model model) {
        log.debug("404 at {} {}", request.getMethod(), request.getRequestURI());

        model.addAttribute("status", 404);
        model.addAttribute("title", "Sidan finns inte bror!");
        model.addAttribute("message", "Kolla att länken/stavningen stämmer och försök igen.");
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }

    /**
     * Fallback handler for unexpected errors that ain't handled elsewhere.
     *
     * @param exception      The thrown exception.
     * @param request Current HTTP request (used for logging and showing the path).
     * @param model   Model attributes used by the error view.
     * @return The Thymeleaf view name for the error page.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOther(Exception exception, HttpServletRequest request, Model model) {
        log.error("Unexpected error at {} {}", request.getMethod(), request.getRequestURI(), exception);

        model.addAttribute("status", 500);
        model.addAttribute("title", "Nåt gick fel");
        model.addAttribute("message", "Ett oväntat fel inträffade. Försök igen.");
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }
}