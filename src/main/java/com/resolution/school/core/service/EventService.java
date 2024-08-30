package com.resolution.school.core.service;

import com.resolution.school.core.model.ErrorDetail;
import com.resolution.school.core.model.ProcessingResult;
import com.resolution.school.core.model.Statistics;

import java.util.List;

public interface EventService {

	Statistics getStatistics();

	ProcessingResult processEvent(String payload);
}


