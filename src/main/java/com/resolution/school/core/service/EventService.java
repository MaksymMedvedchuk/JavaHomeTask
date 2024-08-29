package com.resolution.school.core.service;

import com.resolution.school.core.model.Statistics;

public interface EventService {

	 Statistics getStatistics();

	 void processEvent(String payload);
}
