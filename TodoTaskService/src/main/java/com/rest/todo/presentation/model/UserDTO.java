package com.rest.todo.presentation.model;

import com.rest.todo.presentation.validation.PhoneNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO{
        private Long id;

        @Pattern(regexp = "^[a-zA-Z]{1,10} [a-zA-Z]{1,10}$")
        @NotNull
        private String name;

        @PhoneNumber
        private String phoneNumber;
}
