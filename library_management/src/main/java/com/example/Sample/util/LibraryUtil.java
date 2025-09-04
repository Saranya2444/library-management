package com.example.Sample.util;

import java.util.Arrays;
import java.util.List;

public final class LibraryUtil {

    // App wide date format
    public static final String APP_DATE_FORMAT = "yyyy-MM-dd";

    // Book statuses
    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_BORROWED = "Borrowed";
    public static final String STATUS_DELETED = "Deleted";

    // Allowed statuses for validation
    public static final List<String> ALLOWED_BOOK_STATUSES =
            Arrays.asList(STATUS_AVAILABLE, STATUS_BORROWED, STATUS_DELETED);

    // Validation error messages
    public static final String BOOK_NOT_FOUND = "Book not found with ID: ";
    public static final String BOOK_ALREADY_EXISTS = "Book with the same title and author already exists!";
    public static final String INVALID_BOOK_STATUS =
            "Book status is wrong. Only (Available, Borrowed, Deleted) are allowed.";
    public static final String NO_BOOKS_AVAILABLE = "No books available in the library.";
    public static final String NO_BOOKS_FOUND_BY_STATUS = "No books found with status: ";

    // Success messages
    public static final String BOOK_ADDED_SUCCESS = "Book added successfully!";
    public static final String BOOK_STATUS_UPDATED = "Book status updated successfully!";
    
    
    //members
    
    public static final String MEMBER_REGISTERED_SUCCESS = "Member registered successfully!";
    public static final String MEMBER_UPDATED_SUCCESS = "Member updated successfully!";
    public static final String MEMBER_NOT_FOUND = "Member not found with id: ";
    public static final String MEMBER_EMAIL_EXISTS = "Email already exists!";
    public static final String MEMBER_MOBILE_EXISTS = "Phone number already exists!";
    public static final String MEMBER_ACTIVATED_SUCCESS = "Member activated successfully with id: ";
    public static final String MEMBER_DEACTIVATED_SUCCESS = "Member deactivated successfully with id: ";
    public static final String MEMBER_INVALID_PERIOD = "Membership period is invalid. Only allowed values: 1, 3, 6, 1Y";
    
    // ---- Lists ----
    public static final String NO_MEMBERS_FOUND = "No members found.";
    public static final String NO_STATUS_MEMBERS_FOUND = "No %s members found."; 
    // (%s will be replaced with Active/Inactive in service)

    // ---- Membership expiry ----
    public static final String MEMBERSHIPS_DEACTIVATED_EXPIRY = " memberships deactivated due to expiry.";

    // ---- Status values ----
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_INACTIVE = "Inactive";
    // penalty message
    public static final String PENALTY_RENTAL_NOT_FOUND = "RentalTransaction not found!";
    public static final String PENALTY_ALREADY_EXISTS = "Penalty already exists!";
    public static final String PENALTY_ADDED_SUCCESS = "Penalty added successfully!";
    public static final String PENALTY_NOT_FOUND = "Penalty not found!";
    public static final String PENALTY_INVALID_PAYMENT_MODE = "Invalid payment mode!";
    public static final String PENALTY_PAYMENT_SUCCESS = "Payment successfully done!";
    public static final String PENALTY_CANNOT_PAY_UNTIL_RETURN = "Cannot pay penalty until the book is returned!";
    public static final String PENALTY_NO_PENALTIES_FOUND = "No penalties found for rental ID: ";
    public static final String PENALTY_UPDATED_SUCCESS = "Penalty updated successfully!";
    
 // ---- Penalty Status ----
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_PAID = "Paid";

    // ---- Payment Modes ----
    public static final String PAYMENT_MODE_CASH = "cash";
    public static final String PAYMENT_MODE_UPI = "upi";
    public static final String PAYMENT_MODE_ONLINE = "online";
    public static final String PAYMENT_MODE_CARD = "card";

    // ---- Penalty Reasons ----
    public static final String REASON_MISSING = "missing";
    public static final String REASON_DAMAGE = "damage";
 // ---- Rental messages ----
    public static final String MEMBER_NOT_FOUND_WITH_ID = "Member not found with ID: ";
    public static final String BOOK_NOT_FOUND_WITH_ID = "Book not found with ID: ";
    public static final String BOOK_NOT_AVAILABLE = "Book is not available for borrowing!";
    public static final String BOOK_ALREADY_RETURNED = "Book already returned!";
    public static final String BOOK_RETURNED_SUCCESS = "Book '%s' returned successfully.";
    public static final String BOOK_BORROWED_SUCCESS = "Book '%s' borrowed successfully by %s";
    public static final String TRANSACTION_NOT_FOUND = "Transaction Id is invalid";
    public static final String TRANSACTION_ALREADY_OVERDUE = "Cannot renew. The book is already overdue.";
    public static final String TRANSACTION_ALREADY_RETURNED = "Cannot renew. The book is already returned.";
    public static final String TRANSACTION_ALREADY_RENEWED = "Renewal not allowed. You have already renewed once.";
    public static final String BOOK_RENEWED_SUCCESS = "Book renewed successfully";

	public static final String STATUS_RETURNED = "Returned";

	 public static final String NO_RENTAL_HISTORY = "No rental history for member: %d";
}
