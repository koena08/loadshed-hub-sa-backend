package com.jumpstart.loadshedhub.repository;

import com.jumpstart.loadshedhub.entity.Report;
import com.jumpstart.loadshedhub.entity.Location;
import com.jumpstart.loadshedhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    List<Report> findByLocationOrderByCreatedAtDesc(Location location);

    List<Report> findByUserOrderByCreatedAtDesc(User user);


    @Query("SELECT r FROM Report r WHERE r.location = :location " +
            "AND r.createdAt > :since ORDER BY r.createdAt DESC")
    List<Report> findRecentReportsByLocation(
            @Param("location") Location location,
            @Param("since") LocalDateTime since
    );


    @Query("SELECT COUNT(r) FROM Report r WHERE r.location.id = :locationId " +
            "AND r.user.id = :userId AND r.createdAt > :todayStart")
    long countUserReportsToday(
            @Param("locationId") Long locationId,
            @Param("userId") Long userId,
            @Param("todayStart") LocalDateTime todayStart
    );


    @Query("SELECT r FROM Report r WHERE r.createdAt = " +
            "(SELECT MAX(r2.createdAt) FROM Report r2 WHERE r2.location = r.location)")
    List<Report> findLatestReportPerLocation();
}