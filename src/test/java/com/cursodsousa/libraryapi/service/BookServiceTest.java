package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.exception.BusinessException;
import com.cursodsousa.libraryapi.model.entity.Book;
import com.cursodsousa.libraryapi.model.repository.BookRepository;
import com.cursodsousa.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("POST - Deve salvar o livro.")
    public void saveBookTest(){
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
    @DisplayName("POST - Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void sholdNotSaveABookWithDuplicatedISBN(){
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

    @Test
    @DisplayName("GET - Deve obter um livro por Id.")
    public void getByIdTest(){
        //arrange
        Long id = 1L;
        Book book = createValidBook(Book.builder());
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        //act
        Optional<Book> foundBook = service.getById(id);

        //assert
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    @DisplayName("GET - Deve retornar vazio ao obter um livro por Id quando ele não existe na base.")
    public void bookNotFoundByIdTest(){
        //arrange
        Long id = 1L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //act
        Optional<Book> book = service.getById(id);

        //assert
        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("DELETE - Deve deletar um livro.")
    public void deleteBookTest(){
        //arrange
        Book book = Book.builder().id(1L).build();

        //act
        assertDoesNotThrow(() -> service.delete(book));

        //assert
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("DELETE - Deve ocorrer um erro ao tentar deletar um livro existente.")
    public void deleteInvalidBookTest(){
        //arrange
        Book book = new Book();

        //act
        assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        //assert
        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("PUT - Deve atualizar um livro.")
    public void updateBookTest(){
        Long id = 1L;

        //arrange
        Book updatingBook = Book.builder().id(id).build();
        Book updatedBook = createValidBook(Book.builder());
        updatedBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updatedBook);

        //act
        Book book = service.update(updatingBook);

        //assert
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
    }

    @Test
    @DisplayName("PUT - Deve ocorrer um erro ao tentar atualizar um livro existente.")
    public void updateInvalidBookTest(){
        //arrange
        Book book = new Book();

        //act
        assertThrows(IllegalArgumentException.class, () -> service.update(book));

        //assert
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("FIND - Deve filtrar livros pelas propriedades.")
    public void findBookTest(){
        //arrange
        Book book = createValidBook(Book.builder());

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> lista = Arrays.asList(book);
        Page<Book> page = new PageImpl<Book>(lista, pageRequest, 1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //act
        Page<Book> result = service.find(book, pageRequest);

        //assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

}
