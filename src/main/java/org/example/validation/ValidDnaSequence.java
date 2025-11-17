package org.example.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidDnaSequenceValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDnaSequence {
    //mensaje de error por defecto
    String message() default "La secuencia de ADN es inválida (no es cuadrada, tiene un tamaño menor al mínimo o contiene" +
            " valores incorrectos)";
    // 2. El "boilerplate" estándar (requerido)
    Class<?>[] groups() default {};

    // 3. El "boilerplate" estándar (requerido)
    Class<? extends Payload>[] payload() default {};

}
