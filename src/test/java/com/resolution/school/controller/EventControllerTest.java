package com.resolution.school.controller;

import com.resolution.school.core.model.ErrorDetail;
import com.resolution.school.core.model.ProcessingResult;
import com.resolution.school.core.model.Statistics;
import com.resolution.school.core.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private EventService eventService;

	@Test
	public void givenValidPayload_whenHandleEvent_thenReturnAccepted() throws Exception {
		ProcessingResult mockResult = new ProcessingResult(1, 1, Collections.emptyList());
		when(eventService.processEvent(anyString())).thenReturn(mockResult);

		mockMvc.perform(post("/api/event")
				.content("valid data")
				.contentType(MediaType.TEXT_PLAIN))
			.andExpect(status().isAccepted());
	}

	@Test
	public void givenInvalidPayload_whenHandleEvent_thenReturnPartialContent() throws Exception {
		List<ErrorDetail> errors = new ArrayList<>();
		errors.add(new ErrorDetail(1, "1234567890,0.1234567890,1111111111111111", "Invalid range number"));
		ProcessingResult mockResult = new ProcessingResult(1, 0, errors);
		when(eventService.processEvent(anyString())).thenReturn(mockResult);

		mockMvc.perform(post("/api/event")
				.content("1234567890,0.1234567890, 1111111111111111")
				.contentType(MediaType.TEXT_PLAIN))
			.andExpect(status().isPartialContent());
	}

	@Test
	public void givenNoRecentEvents_whenGetStats_thenReturnNotFound() throws Exception {
		Statistics mockStats = new Statistics(0, 0, 0, 0, 0);
		when(eventService.getStatistics()).thenReturn(mockStats);

		mockMvc.perform(get("/api/stats"))
			.andExpect(status().isNotFound())
			.andExpect(content().string("There weren't any writings from the last 60 seconds"));
	}

	@Test
	public void givenRecentEvents_whenGetStats_thenReturnOk() throws Exception {
		Statistics mockStats = new Statistics(1, 0.1234567890, 0.1234567890, 1234567890, 1234567890);
		when(eventService.getStatistics()).thenReturn(mockStats);

		mockMvc.perform(get("/api/stats"))
			.andExpect(status().isOk())
			.andExpect(content().string("1,0.1234567890,0.1234567890,1234567890,1234567890"));
	}
}
