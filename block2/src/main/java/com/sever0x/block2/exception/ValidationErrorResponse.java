package com.sever0x.block2.exception;

import java.util.List;

public record ValidationErrorResponse(List<FieldValidationError> errors) {
}
