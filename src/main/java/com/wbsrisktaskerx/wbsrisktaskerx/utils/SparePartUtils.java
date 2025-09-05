package com.wbsrisktaskerx.wbsrisktaskerx.utils;

import com.wbsrisktaskerx.wbsrisktaskerx.exception.AppException;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public class SparePartUtils {

    public static  <T, Long> T getSparePartById(JpaRepository<T, Long> repository, Long id, ErrorCode errorCode) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(errorCode));
    }
}
