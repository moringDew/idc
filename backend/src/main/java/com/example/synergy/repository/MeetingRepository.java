package com.example.synergy.repository;

import com.example.synergy.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByStatus(String status);
    List<Meeting> findByType(String type);
    List<Meeting> findByDate(LocalDate date);
}