package com.example.synergy.service;

import com.example.synergy.entity.Meeting;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeetingService {
    List<Meeting> findAll();
    Optional<Meeting> findById(Long id);
    Meeting save(Meeting meeting);
    void deleteById(Long id);
    List<Meeting> findByStatus(String status);
    List<Meeting> findByType(String type);
    List<Meeting> findByDate(LocalDate date);
}