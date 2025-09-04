package com.example.Sample.daoImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.Sample.dao.BookDao;
import com.example.Sample.model.entity.Book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
@Repository
@Transactional
public class BookDaoImpl implements BookDao {

	 @PersistenceContext
	    private EntityManager em;

	 @Override
	 public Book save(Book book) {
	     if (book.getBookId() == null) {
	         em.persist(book);
	         return book;
	     } else {
	         Book managed = em.find(Book.class, book.getBookId());
	         if (managed != null) {
	             managed.setTitle(book.getTitle());
	             managed.setAuthor(book.getAuthor());
	             managed.setPrice(book.getPrice());
	             managed.setLanguage(book.getLanguage());
	             managed.setGenre(book.getGenre());
	             managed.setBookStatus(book.getBookStatus());
	             managed.setUpdatedAt(book.getUpdatedAt());
	             return managed; 
	         } else {
	             throw new IllegalArgumentException("Book with ID " + book.getBookId() + " not found.");
	         }
	     }
	 }

	    
	
	    public boolean existsByTitleAndAuthor(String title, String author) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

	        Root<Book> book = cq.from(Book.class);

	        cq.select(cb.count(book))
	          .where(
	              cb.equal(book.get("title"), title),
	              cb.equal(book.get("author"), author)
	          );

	        Long count = em.createQuery(cq).getSingleResult();
	        return count > 0;
	    }

	    
	    @Override
	    public Book findById(Integer id) {
	        return em.find(Book.class, id);
	    }

	    @Override
	    public List<Book> findByTitle(String title) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

	        Root<Book> book = cq.from(Book.class);

	       
	        cq.select(book)
	          .where(cb.like(book.get("title"), "%" + title + "%"));

	        return em.createQuery(cq).getResultList();
	    }


	    @Override
	    public List<Book> findByAuthor(String author) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

	        Root<Book> book = cq.from(Book.class);

	        
	        cq.select(book)
	          .where(cb.like(book.get("author"), "%" + author + "%"));

	        return em.createQuery(cq).getResultList();
	    }

	    
	    @Override
	    public List<Book> findAll() {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

	        Root<Book> book = cq.from(Book.class);

	        cq.select(book);

	        return em.createQuery(cq).getResultList();
	    }

	    
	    @Override
	    public Book updateBookStatus(Integer id, String status) {
	        Book book = em.find(Book.class, id);
	        if (book != null) {
	        	
	            book.setBookStatus(status);
	            book.setUpdatedAt(LocalDateTime.now());

	            if ("Deleted".equalsIgnoreCase(status)) {
	                book.setBookDeletedDate(LocalDateTime.now());
	            }
	            else {
	            	book.setBookDeletedDate(null);
	            }

	            em.merge(book);
	        }
	        return book;
	
	    }
	    
	    @Override
	    public List<Book> findByStatus(String status) {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

	        Root<Book> book = cq.from(Book.class);

	        cq.select(book)
	          .where(cb.equal(book.get("bookStatus"), status));

	        return em.createQuery(cq).getResultList();
	    }

	    
	    
	    
}
