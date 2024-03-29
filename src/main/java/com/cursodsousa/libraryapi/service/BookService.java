package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.model.entity.Book;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Book save(Book any);

    Optional<Book> getById(Long id); //Retorna um Optional, pq pode ser que exista um book com esse ID ou não.

    void delete(Book book);

    Book update(Book book);

    Page<Book> find(Book filter, Pageable pageRequest);

    Optional<Book> getBookByIsbn(String isbn);
}
