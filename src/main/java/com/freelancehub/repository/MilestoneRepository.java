
package com.freelancehub.repository;

import com.freelancehub.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository
        extends JpaRepository<Milestone, Long> {

    List<Milestone> findByContract_Id(Long contractId);

}
