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

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * DB-issues
     */
    @ExceptionHandler({ CannotGetJdbcConnectionException.class, DataAccessException.class })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) //503
    public String handleDB(Exception exception, HttpServletRequest request, Model model) {
        log.error("DB-problem at {} {}", request.getMethod(), request.getRequestURI(), exception);

        model.addAttribute("status", 503);
        model.addAttribute("title", "Databasen är nere!");
        model.addAttribute("message", "Just nu går det inte att nå databasen. Försök igen om en stund.");
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }

    /**
     * 404, missing route/resource
     */
    @ExceptionHandler({ NoHandlerFoundException.class, NoResourceFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public String handleNotFound(Exception ex, HttpServletRequest request, Model model) {
        log.debug("404 at {} {}", request.getMethod(), request.getRequestURI());

        model.addAttribute("status", 404);
        model.addAttribute("title", "Sidan finns inte bror!");
        model.addAttribute("message", "Kolla att länken/stavningen stämmer och försök igen.");
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }

    /**
     * 500, fallback for everything else
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public String handleOther(Exception ex, HttpServletRequest request, Model model) {
        log.error("Unexpected error at {} {}", request.getMethod(), request.getRequestURI(), ex);

        model.addAttribute("status", 500);
        model.addAttribute("title", "Nåt gick fel");
        model.addAttribute("message", "Ett oväntat fel inträffade. Försök igen.");
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }
}