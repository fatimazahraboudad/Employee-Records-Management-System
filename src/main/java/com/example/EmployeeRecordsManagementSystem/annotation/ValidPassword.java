package com.example.EmployeeRecordsManagementSystem.annotation;

import com.example.EmployeeRecordsManagementSystem.validation.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "The password must contain at least one uppercase letter, one lowercase letter, one number, one special character and be at least 8 characters long.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
