package com.mleczey.todo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.stream.Collectors.toMap;

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException x, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    final var errors = x.getBindingResult().getAllErrors().stream()
        .filter(Objects::nonNull)
        .map(ErrorWrapper::new)
        .collect(toMap(ErrorWrapper::field, ErrorWrapper::message, CommonExceptionHandler::combine));
    return new ResponseEntity<>(new ErrorResponse(errors), status);
  }

  private static List<String> combine(final List<String> left, final Collection<String> right) {
    final List<String> list = new ArrayList<>(left);
    list.addAll(right);
    return list;
  }

  @Value
  private static final class ErrorWrapper {

    @NotNull
    private final ObjectError error;

    private ErrorWrapper(final ObjectError error) {
      this.error = Objects.requireNonNull(error);
    }

    String field() {
      return ((FieldError) error).getField();
    }

    List<String> message() {
      final var message = error.getDefaultMessage();
      return List.of(null == message ? "" : message);
    }
  }
}
