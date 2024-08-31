package com.resolution.school.core.service.impl;

import com.resolution.school.core.model.ErrorDetail;
import com.resolution.school.core.model.Event;
import com.resolution.school.core.model.ProcessingResult;
import com.resolution.school.core.model.Statistics;
import com.resolution.school.core.service.EventService;
import com.resolution.school.exception.NotDecimalException;
import com.resolution.school.exception.NotRangeOfException;
import com.resolution.school.exception.NotTenDigitsAfterDotException;
import com.resolution.school.exception.NotThreeCommaSeparatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
	private static final Pattern DECIMAL_PATTERN = Pattern.compile("^[01]\\.\\d{10}$");
	private static final Integer PAYLOAD_LENGTH = 3;
	private static final String PAYLOAD_SPLIT = "\r\n";
	private static final String LINE_SPLIT = ",";
	private static final Integer MIN_DECIMAL_NUMBER = 0;
	private static final Integer MAX_DECIMAL_NUMBER = 1;
	private static final Integer MIN_RANGE_NUMBER = 1073741823;
	private static final Integer MAX_RANGE_NUMBER = 2147483647;
	private static final long TRACK_TIME = 60000;
	private final Deque<Event> events = new ConcurrentLinkedDeque<>();
	private final List<ErrorDetail> errors = new ArrayList<>();

	@Override
	public synchronized ProcessingResult processEvent(String payload) {
		errors.clear();
		String[] lines = payload.split(PAYLOAD_SPLIT);
		int processedRows = 0;

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			try {
				String[] parts = line.split(LINE_SPLIT);
				if (parts.length != PAYLOAD_LENGTH) {
					throw new NotThreeCommaSeparatedException(
						"Payload must contain exactly three comma-separated values");
				}

				long timestamp = Long.parseLong(parts[0]);
				String stringDecimal = parts[1].trim();
				long rangeNumber = Long.parseLong(parts[2]);
				double decimalNumber = Double.parseDouble(stringDecimal);

				if (decimalNumber < MIN_DECIMAL_NUMBER || decimalNumber > MAX_DECIMAL_NUMBER) {
					throw new NotDecimalException("Second number should be decimal within 0 to 1");
				}

				if (stringDecimal.isEmpty() || !DECIMAL_PATTERN.matcher(stringDecimal).matches()) {
					throw new NotTenDigitsAfterDotException("Second number should have 10 digits after dot");
				}

				if (rangeNumber < MIN_RANGE_NUMBER || rangeNumber > MAX_RANGE_NUMBER) {
					throw new NotRangeOfException("Third number should be within 1073741823 to 2147483647");
				}

				Event event = new Event(timestamp, decimalNumber, rangeNumber);
				events.addLast(event);
				processedRows++;
			} catch (Exception e) {
				errors.add(new ErrorDetail(i + 1, line, e.getMessage()));
			}
		}
		return new ProcessingResult(lines.length, processedRows, errors);
	}

	@Override
	public synchronized Statistics getStatistics() {
		cleanOldEvents();
		int total = 0;
		double decimalSum = 0;
		long rangeSum = 0;
		for (Event event : events) {
			total++;
			decimalSum += event.getDecimal();
			rangeSum += event.getRangeOf();
		}
		double decimalAvg = total > 0 ? decimalSum / total : 0;
		long rangeAvg = total > 0 ? rangeSum / total : 0;

		return new Statistics(total, decimalSum, decimalAvg, rangeSum, rangeAvg);
	}

	public void cleanOldEvents() {
		long currentTime = System.currentTimeMillis();
		while (!events.isEmpty() && (currentTime - events.peekFirst().getTimestamp() > TRACK_TIME)) {
			events.pollFirst();
		}
	}
}

