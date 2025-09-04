package com.example.Sample.daoImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.Sample.dao.RentalTransactionDao;
import com.example.Sample.model.entity.Book;
import com.example.Sample.model.entity.Member;
import com.example.Sample.model.entity.PenaltyTransaction;
import com.example.Sample.model.entity.RentalTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class RentalTransactionDaoImpl implements RentalTransactionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public RentalTransaction save(RentalTransaction rentalTransaction) {
        if (rentalTransaction.getTransactionId() == null) {
            entityManager.persist(rentalTransaction);
            return rentalTransaction;
        } else {
            return entityManager.merge(rentalTransaction);
        }
    }
    
    @Override
    public RentalTransaction findById(Integer id) {
        return entityManager.find(RentalTransaction.class, id);
    }
    
    @Override
    public void updateFinesForOverdueBooks() {
        LocalDateTime now = LocalDateTime.now();

       
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalTransaction> cq = cb.createQuery(RentalTransaction.class);
        Root<RentalTransaction> root = cq.from(RentalTransaction.class);

        cq.select(root).where(cb.equal(root.get("rentalStatus"), "Borrowed"));

        List<RentalTransaction> borrowedTransactions = entityManager.createQuery(cq).getResultList();

        for (RentalTransaction transaction : borrowedTransactions) {
            if (transaction.getReturnDate() != null
                && transaction.getActualReturnDate() == null   // not yet returned
                && now.isAfter(transaction.getReturnDate())) { // overdue

                Double currentFine = transaction.getFineAccrued() != null ? transaction.getFineAccrued() : 0.0;
                Double newFine = currentFine + 10.0;

                transaction.setFineAccrued(newFine);
                transaction.setUpdatedAt(now);
                entityManager.merge(transaction);

                
                CriteriaBuilder cb2 = entityManager.getCriteriaBuilder();
                CriteriaQuery<PenaltyTransaction> cq2 = cb2.createQuery(PenaltyTransaction.class);
                Root<PenaltyTransaction> pRoot = cq2.from(PenaltyTransaction.class);
                Join<PenaltyTransaction, RentalTransaction> rentalJoin = pRoot.join("rentalTransaction");

                cq2.select(pRoot).where(
                    cb2.and(
                        cb2.equal(rentalJoin.get("transactionId"), transaction.getTransactionId()),
                        cb2.equal(pRoot.get("status"), "Pending")
                    )
                );

                List<PenaltyTransaction> resultList = entityManager.createQuery(cq2).getResultList();

                PenaltyTransaction penalty = null;
                if (!resultList.isEmpty()) {
                    
                    penalty = resultList.get(0);
                }

                if (penalty != null) {
                    Double currentPenaltyAmount = penalty.getAmount() != null ? penalty.getAmount() : 0.0;
                    penalty.setAmount(currentPenaltyAmount + 10.0);

                    entityManager.merge(penalty);
                }
            }
        }
    }


    @Override
    public List<RentalTransaction> findByMemberAndPeriod(Integer memberId, LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalTransaction> cq = cb.createQuery(RentalTransaction.class);

        Root<RentalTransaction> root = cq.from(RentalTransaction.class);

        Join<Object, Object> memberJoin = root.join("member");

       
        Predicate memberPredicate = cb.equal(memberJoin.get("memberId"), memberId);
        Predicate betweenPredicate = cb.between(root.get("borrowedDate"), start, end);

      
        cq.select(root)
          .where(cb.and(memberPredicate, betweenPredicate))
          .orderBy(cb.desc(root.get("borrowedDate")));

        return entityManager.createQuery(cq).getResultList();
    }
    
    
    @Override
    public long countCurrentBorrowedByMember(Integer memberId, LocalDateTime now) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<RentalTransaction> root = cq.from(RentalTransaction.class);

        
        Join<RentalTransaction, Member> memberJoin = root.join("member");

        Predicate memberPredicate = cb.equal(memberJoin.get("memberId"), memberId);
        Predicate statusPredicate = cb.equal(root.get("rentalStatus"), "Borrowed");

        cq.select(cb.count(root)).where(cb.and(memberPredicate, statusPredicate));

        return entityManager.createQuery(cq).getSingleResult();
    }


    
    @Override
    public long countOverdueByMember(Integer memberId, LocalDateTime now) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<RentalTransaction> root = cq.from(RentalTransaction.class);

        
        Join<RentalTransaction, Member> memberJoin = root.join("member");

        Predicate memberPredicate = cb.equal(memberJoin.get("memberId"), memberId);
        Predicate statusPredicate = cb.equal(root.get("rentalStatus"), "Borrowed");
        Predicate actualReturnDateNull = cb.isNull(root.get("actualReturnDate"));
        Predicate overduePredicate = cb.lessThan(root.get("returnDate"), now);

        cq.select(cb.count(root))
          .where(cb.and(memberPredicate, statusPredicate, actualReturnDateNull, overduePredicate));

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<Object[]> topAuthorsForMember(Integer memberId, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<RentalTransaction> root = cq.from(RentalTransaction.class);

        
        Join<RentalTransaction, Member> memberJoin = root.join("member");
        Join<RentalTransaction, Book> bookJoin = root.join("book");

    
        Predicate memberPredicate = cb.equal(memberJoin.get("memberId"), memberId);

        
        Expression<Long> countExp = cb.count(root);

       
        cq.multiselect(bookJoin.get("author"), countExp)
          .where(memberPredicate)
          .groupBy(bookJoin.get("author"))
          .orderBy(cb.desc(countExp));

        return entityManager.createQuery(cq)
                .setMaxResults(limit)
                .getResultList();
    }

}
