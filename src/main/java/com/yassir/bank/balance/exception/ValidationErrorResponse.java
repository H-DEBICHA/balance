package com.yassir.bank.balance.exception;

import java.util.Date;
import java.util.Map;

public record ValidationErrorResponse(int statusCode,
                                      Date timeStamp,
                                      String message,
                                      Map<String, String> errors) {
}
