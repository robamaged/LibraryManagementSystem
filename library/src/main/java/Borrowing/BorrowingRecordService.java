package Borrowing;

import com.example.library.Patron.Patron;
import com.example.library.Patron.PatronRepository;
import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingRecordService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new ResourceNotFoundException("Patron not found"));

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());

        return borrowingRecordRepository.save(borrowingRecord);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found for the given book and patron"));

        borrowingRecord.setReturnDate(LocalDate.now());

        return borrowingRecordRepository.save(borrowingRecord);
    }

    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }
}
