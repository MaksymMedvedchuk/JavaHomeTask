package com.resolution.school.controller;

import com.resolution.school.core.model.PayloadWrapper;
import com.resolution.school.core.model.Statistics;
import com.resolution.school.core.service.EventService;
import com.resolution.school.core.service.Impl.EventServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PostMapping("/event")
	public ResponseEntity<String> handleEvent(@RequestBody PayloadWrapper payloadWrapper) {
		String payload = payloadWrapper.getPayload();
		eventService.processEvent(payload);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Data is successfully processed");
	}

	@GetMapping("/stats")
	public ResponseEntity<?> getStats() {
		Statistics stats = eventService.getStatistics();
		if (stats.getTotal() != 0) {
			return new ResponseEntity<>(stats, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("There weren't any writings since the last 60 seconds", HttpStatus.NOT_FOUND);
		}
	}
}

