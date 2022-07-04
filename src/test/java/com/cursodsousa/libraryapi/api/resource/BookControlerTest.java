package com.cursodsousa.libraryapi.api.resource;

import com.cursodsousa.libraryapi.api.dto.BookDTO;
import com.cursodsousa.libraryapi.api.model.entity.Book;
import com.cursodsousa.libraryapi.api.exception.BusinessException;
import com.cursodsousa.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//RunWith //Versão antiga do Junit... o 4
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControlerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean // Mock especializado utilizado pelo spring pra criar essa instancia mockada e colocar dentro da Injeção de Dependências...
    BookService service;

    @Test
    @DisplayName("POST - Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception{

        BookDTO dto = createNewBook();
        Book savedBook = Book.builder().id(10L).author("Artur").title("As aventuras").isbn("001").build();
        //Criou um livro salvo, para simular o retorno do service...

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders //Definindo uma requisição.
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc // Aqui é a chamada da requisição.
                .perform(request) // Recebe o objeto que preparamos acima.
                .andExpect(status().isCreated()) // Verifica se Retornou 201
                .andExpect(jsonPath("id").value(10L)) // Verifica se retornou um json populado.
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
                ;
    }

    @Test
    @DisplayName("POST - Deve lançar erro de validação quando não houver dados suficientes para criação do livro.")
    public void createInvalidBookTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders //Definindo uma requisição.
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3)));//hasSize(3)));
    }
    
    @Test
    @DisplayName("POST - Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro.")
    public void createBookWithDuplicatedIsbn() throws Exception{
        //arranje
        BookDTO dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "Isbn já cadastrado.";
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                    .willThrow(new BusinessException(mensagemErro));

        //act
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders //Definindo uma requisição.
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //assert
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));
    }

    @Test
    @DisplayName("GET - Deve obter informações de um livro.")
    public void getBookDetailsTest() throws Exception {
        //arrange ou given "dado"
        Long id = 1L;

        Book book = Book.builder()
                .id(id)
                .title(createNewBook().getTitle())
                .author(createNewBook().getAuthor())
                .isbn((createNewBook().getIsbn()))
                .build();

        BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

        //act
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        //assert
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(createNewBook().getTitle()))
                .andExpect(jsonPath("author").value(createNewBook().getAuthor()))
                .andExpect(jsonPath("isbn").value(createNewBook().getIsbn()))
        ;
    }

    @Test
    @DisplayName("GET - Deve retornar resource not found quando o livro procurado não existir.")
    public void bookNotFoundTest() throws Exception{

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        //act
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1)) //Qualquer Id, não vai retornar nada...
                .accept(MediaType.APPLICATION_JSON);

        //assert
        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE - Deve deletar um livro.")
    public void deleteBookTest() throws Exception {

        //arrange
        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));

        //act
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1));

        mvc
                .perform(request)
                .andExpect(status().isNoContent()); //OK ou NoContent. O Segundo é padrão RestFull.
    }

    private BookDTO createNewBook() {
        return BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
    }

}
