package com.sokol.scheduler.web;

import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.domain.dto.SlotStatus;
import com.sokol.scheduler.repository.UserRepository;
import com.sokol.scheduler.service.BookingService;
import com.sokol.scheduler.service.CalendarService;
import com.sokol.scheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final CalendarService calendarService;
    private final BookingService bookingService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/schedule";
    }

    @GetMapping("/schedule")
    public String viewSchedule(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null) return "redirect:/";
        
        String clerkId = principal.getAttribute("sub");
        System.out.println("DEBUG: Logged in user Clerk ID: " + clerkId);
        
        return userRepository.findByClerkId(clerkId)
            .map(user -> {
                model.addAttribute("remainingHours", user.getRemainingHours());
                model.addAttribute("user", user);
                return "schedule";
            })
            .orElseGet(() -> {
                System.out.println("ERROR: User not found in DB for Clerk ID: " + clerkId);
                // In a real app, we might force a re-sync or logout
                return "redirect:/logout";
            });
    }

    // API endpoint for FullCalendar
    @GetMapping("/api/schedule")
    @ResponseBody
    public List<Map<String, Object>> getScheduleEvents(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start) {

        if (principal == null) return List.of();
        
        String clerkId = principal.getAttribute("sub");
        User user = userRepository.findByClerkId(clerkId).orElse(null);
        if (user == null) {
             System.out.println("ERROR: API User not found in DB for Clerk ID: " + clerkId);
             return List.of();
        }

        LocalDate weekStart = (start != null) ? start : LocalDate.now();


        Map<LocalDateTime, SlotStatus> schedule = calendarService.getWeeklySchedule(weekStart, user);
        
        List<Map<String, Object>> events = new ArrayList<>();
        
        schedule.forEach((dateTime, status) -> {
            String color;
            String title;
            boolean clickable = false;

            switch (status) {
                case FREE -> {
                    color = "#28a745"; // Green
                    title = "Wolne";
                    clickable = true;
                }
                case MY_BOOKING -> {
                    color = "#007bff"; // Blue
                    title = "Moja Lekcja";
                }
                case TAKEN -> {
                    color = "#6c757d"; // Gray
                    title = "Zajęte";
                }
                case BLOCKED -> {
                    color = "#343a40"; // Dark
                    title = "Niedostępne";
                }
                default -> { // PAST
                     color = "#dee2e6"; // Light Gray
                     title = "-";
                }
            }
            
            // Only add if relevant (e.g. skip past if you want, but PAST status handles visuals)
            events.add(Map.of(
                "title", title,
                "start", dateTime.toString(),
                "backgroundColor", color,
                "borderColor", color,
                "extendedProps", Map.of("status", status.name(), "clickable", clickable)
            ));
        });

        return events;
    }

    @PostMapping("/schedule/book")
    public String bookSlot(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            RedirectAttributes redirectAttributes) {
        
        try {
            String clerkId = principal.getAttribute("sub");
            User user = userRepository.findByClerkId(clerkId).orElseThrow();
            
            bookingService.bookSlot(user, dateTime);
            redirectAttributes.addFlashAttribute("success", "Pomyślnie zarezerwowano lekcję na: " + dateTime);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/schedule";
    }
}
