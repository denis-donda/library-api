package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.api.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest { //Apenas testes unitários

    BookService service;

    @Test
    @DisplayName("Deve salvar o livro.")
    public void sabeBookTest(){
        //arrange
        Book book= Book.builder().isbn("123").author("Fulano").title("As aventuras").build();


        //act
        Book savedBook = service.save(book);


        //assert
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }
}
