package com.resolution.school.controller;

import com.resolution.school.core.model.ProcessingResult;
import com.resolution.school.core.model.Statistics;
import com.resolution.school.core.service.EventService;
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

	@PostMapping(value = "/event")
	public ResponseEntity<?> handleEvent(@RequestBody String payload) {
		ProcessingResult result = eventService.processEvent(payload);
		if (result.getErrors().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(result, HttpStatus.PARTIAL_CONTENT);
		}
	}

	@GetMapping("/stats")
	public ResponseEntity<?> getStats() {
		Statistics stats = eventService.getStatistics();
		if (stats.getTotal() != 0) {
			String response = String.format(
				"%d,%.10f,%.10f,%d,%d",
				stats.getTotal(),
				stats.getDecimalSum(),
				stats.getDecimalAvg(),
				stats.getRangeSum(),
				stats.getRangeAvg()
			);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("There weren't any writings from the last 60 seconds", HttpStatus.NOT_FOUND);
		}
	}
}

