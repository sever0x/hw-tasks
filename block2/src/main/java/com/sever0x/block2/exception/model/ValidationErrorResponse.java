package com.sever0x.block2.exception.model;

import java.util.List;

public record ValidationErrorResponse(List<FieldValidationError> errors) {
}
