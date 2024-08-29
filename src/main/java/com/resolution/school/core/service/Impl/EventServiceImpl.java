package com.resolution.school.core.service.Impl;

import com.resolution.school.core.model.Event;
import com.resolution.school.core.model.Statistics;
import com.resolution.school.core.service.EventService;
import com.resolution.school.exception.NotDecimalException;
import com.resolution.school.exception.NotRangeOfException;
import com.resolution.school.exception.NotTenDigitsAfterDotException;
import com.resolution.school.exception.NotThreeCommaSeparatedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class EventServiceImpl implements EventService {
	private static final Pattern DECIMAL_PATTERN = Pattern.compile("^[01]\\.\\d{10}$");
	private final Map<Event, LocalDateTime> eventMap = new ConcurrentHashMap<>();

	@Override
	public synchronized void processEvent(String payload) {
		String[] parts = payload.split(",");
		if (parts.length != 3) {
			throw new NotThreeCommaSeparatedException("Payload must contain exactly three comma-separated parts");
		}

		long timestamp = Long.parseLong(parts[0]);
		String stringDecimal = parts[1].trim();
		long rangeNumber = Long.parseLong(parts[2]);
		double decimalNumber = Double.parseDouble(stringDecimal);

		if (decimalNumber < 0 || decimalNumber > 1) {
			throw new NotDecimalException("Second number should be decimal within 0 to 1");
		}

		if (stringDecimal.isEmpty() || !DECIMAL_PATTERN.matcher(stringDecimal).matches()) {
			throw new NotTenDigitsAfterDotException("Second number should have 10 digits after dot");
		}

		if (rangeNumber < 1073741823 || rangeNumber > 2147483647) {
			throw new NotRangeOfException("Third number should be within from 1073741823 to 2147483647");
		}

		Event event = new Event(timestamp, decimalNumber, rangeNumber);
		eventMap.put(event, LocalDateTime.now());
	}

	@Override
	public synchronized Statistics getStatistics() {
		int total = 0;
		double decimalSum = 0;
		long rangeSum = 0;
		for (final Map.Entry<Event, LocalDateTime> eventDateEntry : eventMap.entrySet()) {
			Event event = eventDateEntry.getKey();
			LocalDateTime storedDate = eventDateEntry.getValue();
			if (LocalDateTime.now().minusSeconds(60).isBefore(storedDate)) {
				total++;
				decimalSum += event.getDecimal();
				rangeSum += event.getRangeOf();
			}
		}
		double decimalAvg = total > 0 ? decimalSum / total : 0;
		double rangeAvg = total > 0 ? (double) rangeSum / total : 0;

		return new Statistics(total, decimalSum, decimalAvg, rangeSum, rangeAvg);
	}
}

