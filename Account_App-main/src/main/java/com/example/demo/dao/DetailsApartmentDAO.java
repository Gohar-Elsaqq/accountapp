package com.example.demo.dao;


import com.example.demo.entity.DetailsApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsApartmentDAO extends JpaRepository<DetailsApartment,Integer> {

    @Query(value = """
            SELECT COALESCE(SUM(CAST(da.amount AS NUMERIC)), 0)
            FROM postgres.account.details_apartment da
            INNER JOIN account.apartment a ON da.apartment_id = a.id
            WHERE da.apartment_id = :apartmentId
            """, nativeQuery = true)
    double getLastTotalAmount(int apartmentId);

}


