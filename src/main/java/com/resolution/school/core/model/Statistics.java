package com.resolution.school.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Statistics {
	private int total;
	private double decimalSum;
	private double decimalAvg;
	private double rangeSum;
	private double rangeAvg;
}
