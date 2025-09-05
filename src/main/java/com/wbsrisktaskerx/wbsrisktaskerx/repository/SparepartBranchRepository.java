package com.wbsrisktaskerx.wbsrisktaskerx.repository;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SparepartBranchRepository extends JpaRepository<SparepartBranch, Integer> {
    List<SparepartBranch> findByLocationCode(String locationCode);
}
