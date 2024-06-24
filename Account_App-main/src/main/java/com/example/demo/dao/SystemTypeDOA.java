package com.example.demo.dao;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.SystemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemTypeDOA extends JpaRepository<SystemType,Integer> {

    Optional<SystemType> findByLookupType(String lookupType);

    @Query(value = "SELECT * FROM account.system_type WHERE lookup_type = ?1 AND (?2 IS NULL OR status = ?2)", nativeQuery = true)
     Optional<SystemType> findByLookupTypeAndStatus(String lookupType, String status);

    @Query(value = "SELECT st.lookup_type FROM account.system_type st WHERE st.status = 'ACT' ORDER BY st.id DESC", nativeQuery = true)
    List<String> findAllActiveName();

    @Query(value = "SELECT * FROM account.system_type WHERE status = 'ACT' ORDER BY creation_time DESC", nativeQuery = true)
    List<SystemType> findAllActiveSystemType();

}
