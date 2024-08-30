package com.resolution.school.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetail {
	private int lineNumber;
	private String lineView;
	private String message;
}
