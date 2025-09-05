package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.data.DepartmentName;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    String fullName;
    String email;
    String phoneNumber;
    String password;
    String profileImg; // Can be null if not provided
    Integer roleId;
    DepartmentName departmentName;
}
