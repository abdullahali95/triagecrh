package com.health.application.data.service;

import com.health.application.data.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WardRepository extends JpaRepository<Ward, Long> {

    @Query("select w from Ward w " +
            "where (w.wardName) like (concat('%', 'EMU', '%')) ") //
    Ward findEmu ();
}
