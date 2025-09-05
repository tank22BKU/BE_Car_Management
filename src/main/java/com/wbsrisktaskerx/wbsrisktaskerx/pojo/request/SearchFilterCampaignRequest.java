package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchFilterCampaignRequest {
    String searchKey; 
    List<Boolean> isActive;
    OffsetDateTime startDate;
    OffsetDateTime endDate;
}
