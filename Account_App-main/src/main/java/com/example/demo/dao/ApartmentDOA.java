package com.example.demo.dao;

import com.example.demo.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentDOA extends JpaRepository<Apartment,Integer> {
     Optional<Apartment> findByApartmentCode(String apartmentCode);

     @Query(value = "SELECT * FROM account.apartment WHERE apartment_code = ?1 AND (?2 IS NULL OR status = ?2)", nativeQuery = true)
     Optional<Apartment> findByApartmentCodeAndStatus(String apartmentCode, String status);

     @Query(value = "SELECT * FROM account.apartment WHERE status = 'ACT' ORDER BY creation_time DESC", nativeQuery = true)
     List<Apartment> findAllActiveApartments();
     @Query(value = "SELECT a.apartment_code FROM account.apartment a WHERE a.status = 'ACT' ORDER BY creation_time DESC", nativeQuery = true)
     List<String> findAllActiveApartmentsName();

     @Query(value = "SELECT a.id FROM account.apartment a WHERE a.apartment_code = :apartmentCode AND a.status = 'ACT' ORDER BY creation_time DESC LIMIT 1", nativeQuery = true)
     Optional<Integer> findApartmentIdByApartmentCodeAndStatus(@Param("apartmentCode") String apartmentCode);
}

