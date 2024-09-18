package com.resolution.school.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {
	private int total;
	private double decimalSum;
	private double decimalAvg;
	private long rangeSum;
	private long rangeAvg;
}
