package com.example.synergy.controller;

import com.example.synergy.common.Result;
import com.example.synergy.entity.Meeting;
import com.example.synergy.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping
    public Result<List<Meeting>> getAllMeetings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Meeting> meetings;
        if (status != null && !status.isEmpty()) {
            meetings = meetingService.findByStatus(status);
        } else if (type != null && !type.isEmpty()) {
            meetings = meetingService.findByType(type);
        } else if (date != null) {
            meetings = meetingService.findByDate(date);
        } else {
            meetings = meetingService.findAll();
        }
        return Result.success(meetings);
    }

    @GetMapping("/{id}")
    public Result<Meeting> getMeetingById(@PathVariable Long id) {
        return meetingService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "会议不存在"));
    }

    @PostMapping
    public Result<Meeting> createMeeting(@RequestBody Meeting meeting) {
        Meeting saved = meetingService.save(meeting);
        return Result.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    public Result<Meeting> updateMeeting(@PathVariable Long id, @RequestBody Meeting meeting) {
        return meetingService.findById(id)
                .map(existing -> {
                    if (meeting.getTitle() != null) existing.setTitle(meeting.getTitle());
                    if (meeting.getType() != null) existing.setType(meeting.getType());
                    if (meeting.getDate() != null) existing.setDate(meeting.getDate());
                    if (meeting.getTime() != null) existing.setTime(meeting.getTime());
                    if (meeting.getLocation() != null) existing.setLocation(meeting.getLocation());
                    if (meeting.getParticipants() != null) existing.setParticipants(meeting.getParticipants());
                    if (meeting.getAgenda() != null) existing.setAgenda(meeting.getAgenda());
                    Meeting saved = meetingService.save(existing);
                    return Result.success("更新成功", saved);
                })
                .orElse(Result.error(404, "会议不存在"));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteMeeting(@PathVariable Long id) {
        if (!meetingService.findById(id).isPresent()) {
            return Result.error(404, "会议不存在");
        }
        meetingService.deleteById(id);
        return Result.success("删除成功");
    }
}