package com.cursodsousa.libraryapi.api.resource;

import com.cursodsousa.libraryapi.api.dto.BookDTO;
import com.cursodsousa.libraryapi.api.dto.LoanDTO;
import com.cursodsousa.libraryapi.model.entity.Book;
import com.cursodsousa.libraryapi.model.entity.Loan;
import com.cursodsousa.libraryapi.service.BookService;
import com.cursodsousa.libraryapi.service.LoanService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Api("Book API")
@Slf4j
public class BookController {

    private final BookService service;
    private final ModelMapper modelMapper;
    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) { //@Valid é para validar o @NotEmpyt
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id) {
        return service.getById(id).map(book -> modelMapper.map(book, BookDTO.class)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(book);
    }

    @PutMapping("{id}")
    public BookDTO update(@PathVariable Long id, BookDTO dto) {
        return service.getById(id).map(book -> {

            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            book = service.update(book);
            return modelMapper.map(book, BookDTO.class);

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    public Page<BookDTO> find(BookDTO dto, Pageable pageRequest) {
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent().stream().map(entity -> modelMapper.map(entity, BookDTO.class)).collect(Collectors.toList());

        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }

    @GetMapping("{id}/loans")
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<Loan> result = loanService.getLoansByBook(book, pageable);
        List<LoanDTO> list = result.getContent().stream().map(loan -> {
            Book loanBook = loan.getBook();
            BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
            LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
            loanDTO.setBook(bookDTO);
            return loanDTO;
        }).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
    }

}
