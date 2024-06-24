package com.example.demo.dao;

import com.example.demo.dto.DetailsApartmentDtoSql;
import com.example.demo.entity.DetailsApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetailsApartmentDAO extends JpaRepository<DetailsApartment,Integer> {

    @Query(value = """
            SELECT COALESCE(SUM(CAST(da.amount AS NUMERIC)), 0)
            FROM postgres.account.details_apartment da
            INNER JOIN account.apartment a ON da.apartment_id = a.id
            WHERE da.apartment_id = :apartmentId
            """, nativeQuery = true)
    double getLastTotalAmount(int apartmentId);

    @Query(value = """
            select
             da.id as "detailsApartmentId",
             a.apartment_code as "apartmentCode" ,
             da.amount as "amount",
             st.lookup_type as "type",
            CASE
               WHEN da.establishing = true THEN 'تأسيس'
               ELSE ''
            END AS "establishing",
            CASE
               WHEN da.finishing = true THEN 'تشطيب'
               ELSE ''
            END AS "finishing",
                 TO_CHAR(da.creation_time, 'YYYY-MM-DD') AS "date",
                 da."comments"  as "comments"
            from account.details_apartment da
            left join account.apartment a on a.id =da.apartment_id
            left join account.system_type st on st.id = da.system_type_id
            where a.apartment_code = :apartmentCode
            """, nativeQuery = true)
    List<DetailsApartmentDtoSql> findAllByApartmentCode(@Param("apartmentCode") String apartmentCode);


}


