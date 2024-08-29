package com.resolution.school.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {
	private long timestamp;
	private double decimal;
	private long rangeOf;
}
