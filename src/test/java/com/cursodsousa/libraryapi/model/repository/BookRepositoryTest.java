package com.cursodsousa.libraryapi.model.repository;

import com.cursodsousa.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists(){
        //arranje
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        //act
        boolean exists = repository.existsByIsbn(isbn);

        //assert
        assertThat(exists).isTrue();
    }

    public static Book createNewBook(String isbn) {
        return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnDoesntExist(){
        //arranje
        String isbn = "123";

        //act
        boolean exists = repository.existsByIsbn(isbn);

        //assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id.")
    public void findByIdTest(){
        //arranje
        Book book = createNewBook("123");
        entityManager.persist(book);

        //act
        Optional<Book> foundBook = repository.findById(book.getId());

        //assert
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro.")
    public void saveBookTest(){
        //arrange
        Book book = createNewBook("123");

        //act
        Book savedBook = repository.save(book);

        //assert
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest(){
        //arranje
        Book book = createNewBook("123");
        entityManager.persist(book);

        //act
        Book foundBook = entityManager.find(Book.class, book.getId());
        repository.delete(foundBook);

        //assert
        Book deletedBook = entityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }

}
