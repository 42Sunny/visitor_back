package com.ftseoul.visitor.dto.visitor;

import com.ftseoul.visitor.dto.payload.VisitorStatusInfo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateVisitorStatusDto implements Serializable {
   private VisitorStatusInfo visitor;
}
