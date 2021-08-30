package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.dto.payload.Response;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto implements Serializable {
   private Response error;
}
