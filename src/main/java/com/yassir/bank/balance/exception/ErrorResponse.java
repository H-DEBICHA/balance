package com.yassir.bank.balance.exception;

import java.util.Date;

public record ErrorResponse(int statusCode, Date timeStamp, String message, String description) {
}
