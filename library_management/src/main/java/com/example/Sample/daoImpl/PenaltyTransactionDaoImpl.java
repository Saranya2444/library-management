package com.example.Sample.daoImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.Sample.dao.PenaltyTransactionDao;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.model.entity.RentalTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PenaltyTransactionDaoImpl implements PenaltyTransactionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PenaltyTransaction save(PenaltyTransaction penalty) {
        if (penalty.getPenaltyId() == null) {
            entityManager.persist(penalty);
        } else {
            entityManager.merge(penalty);
        }
        return penalty;
    }

    @Override
    public Optional<PenaltyTransaction> findById(Integer penaltyId) {
        return Optional.ofNullable(entityManager.find(PenaltyTransaction.class, penaltyId));
    }
    @Override
    public PenaltyTransaction findPendingPenaltyByRentalId(Integer rentalId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PenaltyTransaction> cq = cb.createQuery(PenaltyTransaction.class);

        Root<PenaltyTransaction> root = cq.from(PenaltyTransaction.class);

        // Explicit JOIN with RentalTransaction
        Join<Object, Object> rentalJoin = root.join("rentalTransaction");

        Predicate rentalPredicate = cb.equal(rentalJoin.get("transactionId"), rentalId);
        Predicate statusPredicate = cb.equal(root.get("status"), "Pending");

        cq.select(root).where(cb.and(rentalPredicate, statusPredicate));

        return entityManager.createQuery(cq)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    
    @Override
    public List<PenaltyTransaction> findByRentalTransactionId(Integer rentalTransactionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PenaltyTransaction> cq = cb.createQuery(PenaltyTransaction.class);

        Root<PenaltyTransaction> root = cq.from(PenaltyTransaction.class);

        // Explicit JOIN with RentalTransaction
        Join<Object, Object> rentalJoin = root.join("rentalTransaction");

        cq.select(root)
          .where(cb.equal(rentalJoin.get("transactionId"), rentalTransactionId));

        return entityManager.createQuery(cq).getResultList();
    }

    
    @Override
    public List<RentalTransaction> findOverdueRentals() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalTransaction> cq = cb.createQuery(RentalTransaction.class);

        Root<RentalTransaction> root = cq.from(RentalTransaction.class);

        Predicate statusBorrowed = cb.equal(root.get("rentalStatus"), "Borrowed");
        Predicate actualReturnNull = cb.isNull(root.get("actualReturnDate"));
        Predicate overdue = cb.lessThan(root.get("returnDate"), LocalDateTime.now());

        cq.select(root).where(cb.and(statusBorrowed, actualReturnNull, overdue));

        return entityManager.createQuery(cq).getResultList();
    }


}

