package com.resolution.school.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingResult {
	private int totalRows;
	private int processedRows;
	private List<ErrorDetail> errors;
}
