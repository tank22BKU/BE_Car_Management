package com.wbsrisktaskerx.wbsrisktaskerx.repository;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SparepartDetailRepository extends JpaRepository<SparepartDetail, Integer> {
    @EntityGraph(attributePaths = {"sparepartBranches", "sparepart"})
    Optional<SparepartDetail> findById(Integer id);
}
