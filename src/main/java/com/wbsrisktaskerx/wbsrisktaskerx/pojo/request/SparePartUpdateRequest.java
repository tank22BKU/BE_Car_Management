package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartBranchDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartDetailDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparePartUpdateRequest {
      SparePartDTO sparePart;
      SparePartDetailDTO sparePartDetail;
      List<SparePartBranchDTO> sparePartBranches;
}
