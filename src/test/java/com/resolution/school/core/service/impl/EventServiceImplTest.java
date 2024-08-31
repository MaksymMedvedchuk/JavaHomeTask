package com.resolution.school.core.service.impl;

import com.resolution.school.core.model.Event;
import com.resolution.school.core.model.ProcessingResult;
import com.resolution.school.core.model.Statistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Deque;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

	@InjectMocks
	private EventServiceImpl eventService;

	@Mock
	private Deque<Event> events;

	@BeforeEach
	public void setUp() {
	}

	@Test
	public void givenValidPayload_whenProcessEvent_thenShouldProcessSuccessfully() {
		String payload = """
			1724958223880,0.0442672968,1282509067\r
			1724958224880,0.0473002568,1282509067\r
			1724958225880,0.0899538547,1852154378""";
		ProcessingResult result = eventService.processEvent(payload);

		Assertions.assertEquals(3, result.getTotalRows());
		Assertions.assertEquals(3, result.getProcessedRows());
		Assertions.assertTrue(result.getErrors().isEmpty());
	}

	@Test
	public void givenInvalidPayload_whenProcessEvent_thenShouldReturnErrors() {
		String payload = """
			1724958223880,0.0442672968,1282509067\r
			1724958224880,0.0473002568,12345667889\r
			1724958225880,0.0899538547,1852154378""";
		ProcessingResult result = eventService.processEvent(payload);

		Assertions.assertEquals(3, result.getTotalRows());
		Assertions.assertEquals(1, result.getErrors().size());
		Assertions.assertEquals(
			"Third number should be within 1073741823 to 2147483647",
			result.getErrors().get(0).getMessage()
		);
	}

	@Test
	public void givenPayloadWithMissingFields_whenProcessEvent_thenShouldReturnErrors() {
		String payload = """
			1724958223880,0.0442672968\r
			1724958224880,0.0473002568,1785397644""";

		ProcessingResult result = eventService.processEvent(payload);

		Assertions.assertEquals(2, result.getTotalRows());
		Assertions.assertEquals(1, result.getErrors().size());
		Assertions.assertEquals(
			"Payload must contain exactly three comma-separated values",
			result.getErrors().get(0).getMessage()
		);
	}

	@Test
	public void givenEmptyPayload_whenProcessEvent_thenShouldReturnNoErrors() {
		String payload = "";

		ProcessingResult result = eventService.processEvent(payload);

		Assertions.assertEquals(1, result.getTotalRows());
		Assertions.assertEquals(0, result.getProcessedRows());
		Assertions.assertEquals(
			"Payload must contain exactly three comma-separated values",
			result.getErrors().get(0).getMessage()
		);
	}

//	@Test
//	public void givenEvents_whenGetStatistics_thenShouldReturnCorrectStats() {
//		String payload = """
//			1724958223880,0.0442672968,1282509067\r
//			1724958224880,0.0473002568,1282509067\r
//			""";
//		eventService.processEvent(payload);
//
//		doNothing().when(eventService).cleanOldEvents();
//		Statistics stats = eventService.getStatistics();
//
//		Assertions.assertEquals(2, stats.getTotal());
//		Assertions.assertEquals(0.0915675536, stats.getDecimalSum());
//		Assertions.assertEquals(0.0457837768, stats.getDecimalAvg());
//		Assertions.assertEquals(2565018134L, stats.getRangeSum());
//		Assertions.assertEquals(1282509067L, stats.getRangeAvg());
//	}
}
