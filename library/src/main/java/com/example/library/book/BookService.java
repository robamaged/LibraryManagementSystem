package com.example.library.book;

import com.example.library.Borrowing.BorrowingRecordRepository;
import com.example.library.Borrowing.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookService {
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository ;

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setIsbn(bookDetails.getIsbn());

        return bookRepository.save(book);
    }



    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        boolean isBookBorrowed = borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(bookId);

        if (isBookBorrowed) {
            throw new IllegalStateException("Cannot delete book that is currently borrowed.");
        }

        bookRepository.deleteById(bookId);
    }
}
