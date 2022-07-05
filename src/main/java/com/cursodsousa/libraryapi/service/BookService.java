package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.api.model.entity.Book;

import java.util.Optional;

public interface BookService {

    Book save(Book any);

    Optional<Book> getById(Long id); //Retorna um Optional, pq pode ser que exista um book com esse ID ou não.

    void delete(Book book);

    Book update(Book book);
}
