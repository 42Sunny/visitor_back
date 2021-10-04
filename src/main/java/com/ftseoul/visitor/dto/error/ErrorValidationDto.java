package com.ftseoul.visitor.dto.error;

import com.ftseoul.visitor.dto.payload.ValidationErrorResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorValidationDto {
    private List<ValidationErrorResponse> error;
}
