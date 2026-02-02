package com.sokol.scheduler.web;

import com.sokol.scheduler.domain.Lesson;
import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.repository.UserRepository;
import com.sokol.scheduler.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    @GetMapping("")
    public String index() {
        return "redirect:/admin/schedule";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("students", adminService.getAllStudents());
        return "admin/dashboard";
    }

    @PostMapping("/student/update-hours")
    public String updateHours(@RequestParam Long studentId, @RequestParam int hours, RedirectAttributes redirectAttributes) {
        try {
            adminService.updateStudentHours(studentId, hours);
            redirectAttributes.addFlashAttribute("success", "Zaktualizowano godziny studenta.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/schedule")
    public String adminSchedule(Model model) {
        return "admin/schedule";
    }

    @GetMapping("/api/schedule")
    @ResponseBody
    public List<Map<String, Object>> getAdminSchedule(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start) {
        
        LocalDate weekStart = (start != null) ? start : LocalDate.now();
        LocalDateTime startDateTime = weekStart.atStartOfDay();
        LocalDateTime endDateTime = weekStart.plusDays(6).atTime(23, 59, 59);

        List<Lesson> lessons = adminService.getLessonsForRange(startDateTime, endDateTime);
        Map<LocalDateTime, Lesson> lessonMap = lessons.stream()
                .collect(Collectors.toMap(Lesson::getStartTime, l -> l));

        List<Map<String, Object>> events = new ArrayList<>();
        
        // We generate the grid for 8:00 - 20:00
        for (int day = 0; day < 7; day++) {
            LocalDate date = weekStart.plusDays(day);
            for (int hour = 8; hour < 20; hour++) {
                LocalDateTime slotTime = date.atTime(hour, 0);
                Lesson lesson = lessonMap.get(slotTime);
                
                String color;
                String title;
                String statusName;
                boolean clickable = true;

                if (slotTime.isBefore(LocalDateTime.now())) {
                    color = "#dee2e6";
                    title = "-";
                    statusName = "PAST";
                    clickable = false;
                } else if (lesson == null) {
                    color = "#28a745"; // Green
                    title = "Wolne (ZABLOKUJ)";
                    statusName = "FREE";
                } else {
                    switch (lesson.getStatus()) {
                        case BLOCKED -> {
                            color = "#343a40"; // Dark
                            title = "ZABLOKOWANE (ODBLOKUJ)";
                            statusName = "BLOCKED";
                        }
                        case BOOKED -> {
                            color = "#dc3545"; // Red for admin to see it's taken
                            User s = lesson.getStudent();
                            title = (s != null ? s.getFirstName() + " " + s.getLastName() : "Zajęte") + " (ANULUJ)";
                            statusName = "BOOKED";
                        }
                        default -> {
                            color = "#6c757d";
                            title = "Zajęte";
                            statusName = "UNKNOWN";
                        }
                    }
                }

                events.add(Map.of(
                    "title", title,
                    "start", slotTime.toString(),
                    "backgroundColor", color,
                    "borderColor", color,
                    "extendedProps", Map.of("status", statusName, "clickable", clickable)
                ));
            }
        }

        return events;
    }

    @PostMapping("/block")
    public String block(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime, RedirectAttributes redirectAttributes) {
        try {
            adminService.blockSlot(dateTime);
            redirectAttributes.addFlashAttribute("success", "Zablokowano termin.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/schedule";
    }

    @PostMapping("/unblock")
    public String unblock(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime, RedirectAttributes redirectAttributes) {
        try {
            adminService.unblockSlot(dateTime);
            redirectAttributes.addFlashAttribute("success", "Odblokowano termin.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/schedule";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime, RedirectAttributes redirectAttributes) {
        try {
            adminService.cancelLesson(dateTime);
            redirectAttributes.addFlashAttribute("success", "Anulowano rezerwację.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/schedule";
    }
}
