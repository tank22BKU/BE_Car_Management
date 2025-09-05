package com.wbsrisktaskerx.wbsrisktaskerx.utils;

import com.wbsrisktaskerx.wbsrisktaskerx.exception.AppException;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.ErrorCode;
import org.apache.commons.lang3.ObjectUtils;

import java.util.regex.Pattern;

public class PhoneNumberUtils {

    private static final Pattern VIETNAM_PHONE_PATTERN = Pattern.compile("^(?:\\+84|0)([35789][0-9]{8})$");

    /**
     * Formats a phone number to the +84 (Vietnam) format.
     *
     * @param phoneNumber The input phone number (e.g., "0901234567", "+84901234567", "09 0123 4567")
     * @return Formatted phone number (e.g., "+84901234567")
     * @throws AppException if the phone number is invalid
     */
    public static String formatToPlus84(String phoneNumber) {
        if (ObjectUtils.isEmpty(phoneNumber) || phoneNumber.trim().isEmpty()) {
            throw new AppException(ErrorCode.PHONE_NUMBER_INVALID, "Phone number cannot be null, empty, or whitespace");
        }

        // Remove spaces, dashes, and other non-digit characters except the leading +
        String cleanedNumber = phoneNumber.replaceAll("[^0-9+]", "");

        // Handle common input formats
        if (cleanedNumber.startsWith("+84")) {
            if (VIETNAM_PHONE_PATTERN.matcher(cleanedNumber).matches()) {
                return cleanedNumber;
            }
        } else if (cleanedNumber.startsWith("0")) {
            cleanedNumber = "+84" + cleanedNumber.substring(1);
            if (VIETNAM_PHONE_PATTERN.matcher(cleanedNumber).matches()) {
                return cleanedNumber;
            }
        } else {
            cleanedNumber = "+84" + cleanedNumber;
            if (VIETNAM_PHONE_PATTERN.matcher(cleanedNumber).matches()) {
                return cleanedNumber;
            }
        }

        throw new AppException(ErrorCode.PHONE_NUMBER_INVALID,
                String.format("Invalid phone number format: %s. Must be a valid Vietnamese mobile number (e.g., +84901234567)", phoneNumber));
    }
}