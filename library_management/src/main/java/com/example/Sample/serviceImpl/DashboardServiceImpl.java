package com.example.Sample.serviceImpl;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Sample.dao.DashboardDao;
import com.example.Sample.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardDao dashboardDao;

    @Override
    public Map<String, Long> getDashboardSummary() {
        Map<String, Long> summary = new TreeMap<>();

        summary.put("totalBooks", dashboardDao.countBooks());
        summary.put("availableBooks", dashboardDao.countAvailableBooks());
        summary.put("borrowedBooks", dashboardDao.countBorrowedBooks());

        summary.put("totalMembers", dashboardDao.countMembers());
        summary.put("activeMembers", dashboardDao.countActiveMembers());

        summary.put("totalTransactions", dashboardDao.countTransactions());
        summary.put("overdueTransactions", dashboardDao.countOverdueTransactions());

        summary.put("totalPenalties", dashboardDao.countPenalties());
        summary.put("pendingPenalties", dashboardDao.countPendingPenalties());
        summary.put("paidPenalties", dashboardDao.countPaidPenalties());

        return summary;
    }
}
