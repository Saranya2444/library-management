package com.example.Sample.dao;

public interface DashboardDao {
    long countBooks();
    long countAvailableBooks();
    long countBorrowedBooks();

    long countMembers();
    long countActiveMembers();

    long countTransactions();
    long countOverdueTransactions();

    long countPenalties();
    long countPendingPenalties();
    long countPaidPenalties();
}

