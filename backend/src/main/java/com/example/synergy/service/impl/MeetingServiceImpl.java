package com.example.synergy.service.impl;

import com.example.synergy.entity.Meeting;
import com.example.synergy.repository.MeetingRepository;
import com.example.synergy.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Override
    public List<Meeting> findAll() {
        return meetingRepository.findAll();
    }

    @Override
    public Optional<Meeting> findById(Long id) {
        return meetingRepository.findById(id);
    }

    @Override
    public Meeting save(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    @Override
    public void deleteById(Long id) {
        meetingRepository.deleteById(id);
    }

    @Override
    public List<Meeting> findByStatus(String status) {
        return meetingRepository.findByStatus(status);
    }

    @Override
    public List<Meeting> findByType(String type) {
        return meetingRepository.findByType(type);
    }

    @Override
    public List<Meeting> findByDate(LocalDate date) {
        return meetingRepository.findByDate(date);
    }
}