package com.example.library.Borrowing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    Optional<BorrowingRecord> findByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);

    boolean existsByPatronIdAndReturnDateIsNull(Long patronId);

    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
}

