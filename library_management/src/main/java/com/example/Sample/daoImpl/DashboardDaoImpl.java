package com.example.Sample.daoImpl;

import org.springframework.stereotype.Repository;

import com.example.Sample.dao.DashboardDao;
import com.example.Sample.model.entity.Book;
import com.example.Sample.model.entity.Member;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.model.entity.RentalTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class DashboardDaoImpl implements DashboardDao {

    @PersistenceContext
    private EntityManager entityManager;

    private long countAll(Class<?> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<?> root = cq.from(entityClass);
        cq.select(cb.count(root));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public long countBooks() {
        return countAll(Book.class);
    }

    @Override
    public long countAvailableBooks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Book> root = cq.from(Book.class);

        cq.select(cb.count(root))
          .where(cb.equal(root.get("bookStatus"), "Available")); // <-- field is bookStatus

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public long countBorrowedBooks() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Book> root = cq.from(Book.class);

        cq.select(cb.count(root))
          .where(cb.equal(root.get("bookStatus"), "Borrowed")); // <-- field is bookStatus

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public long countMembers() {
        return countAll(Member.class);
    }

    @Override
    public long countActiveMembers() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Member> root = cq.from(Member.class);

        cq.select(cb.count(root))
          .where(cb.equal(root.get("membershipStatus"), "Active")); // use entity field name

        return entityManager.createQuery(cq).getSingleResult();
    }


    @Override
    public long countTransactions() {
        return countAll(RentalTransaction.class);
    }

    @Override
    public long countOverdueTransactions() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<RentalTransaction> root = cq.from(RentalTransaction.class);

        cq.select(cb.count(root)).where(
                cb.and(
                        cb.lessThan(root.get("returnDate"), cb.currentDate()),
                        cb.isNull(root.get("actualReturnDate"))
                )
        );
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public long countPenalties() {
        return countAll(PenaltyTransaction.class);
    }

    @Override
    public long countPendingPenalties() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PenaltyTransaction> root = cq.from(PenaltyTransaction.class);
        cq.select(cb.count(root)).where(cb.equal(root.get("status"), "Pending"));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public long countPaidPenalties() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PenaltyTransaction> root = cq.from(PenaltyTransaction.class);
        cq.select(cb.count(root)).where(cb.equal(root.get("status"), "Paid"));
        return entityManager.createQuery(cq).getSingleResult();
    }
}

