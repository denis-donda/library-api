package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.api.exception.BusinessException;
import com.cursodsousa.libraryapi.api.model.entity.Book;
import com.cursodsousa.libraryapi.model.repository.BookRepository;
import com.cursodsousa.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest { //Apenas testes unitários

    BookService service;
    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar o livro.")
    public void sabeBookTest(){
        //arrange
        Book book= createValidBook(Book.builder());
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book))
                .thenReturn(createValidBook(Book.builder()
                        .id(1L))
                );

        //act
        Book savedBook = service.save(book);

        //assert
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    private Book createValidBook(Book.BookBuilder builder) {
        return builder.isbn("123").author("Fulano").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void sholdNotSabeABookWithDuplicatedISBN(){
        //arranje
        Book book = createValidBook(Book.builder());
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //act
        Throwable exception = Assertions.catchThrowable( () -> service.save(book) );

        //assert
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book); //Nunca deve chamar o save(book).
    }

}
