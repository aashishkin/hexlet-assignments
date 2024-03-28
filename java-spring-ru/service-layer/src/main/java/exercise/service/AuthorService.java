package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorMapper authorMapper;


    public List<AuthorDTO> all() {
        return authorRepository.findAll().stream()
                .map(authorMapper::map)
                .toList();
    }

    public AuthorDTO one(long id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        return authorMapper.map(author);
    }

    public AuthorDTO create(AuthorCreateDTO dto) {
        var entity = authorMapper.map(dto);
        authorRepository.save(entity);
        return authorMapper.map(entity);
    }

    public AuthorDTO update(AuthorUpdateDTO dto, long id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        authorMapper.update(dto, author);
        authorRepository.save(author);
        return authorMapper.map(author);
    }

    public void delete(long id) {
        try {
            authorRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Author not found");
        }
    }
    // END
}
