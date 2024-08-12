package Borrowing;

import com.example.library.Patron.Patron;
import com.example.library.book.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
@Setter
@Getter
@Entity
public class BorrowingRecord {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

// Constructors, Getters, Setters
}
