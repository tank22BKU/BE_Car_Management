package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActiveRequest {
    Integer id;

    @NotNull
    Boolean isActive;
}
