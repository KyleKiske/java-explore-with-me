package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.Compilation;

@Component
public class CompilationMapper {

    public Compilation newCompilationToCompilation(NewCompilationDto newCompilationDto) {

        if (newCompilationDto == null) {
            return null;
        }
        Compilation compilation = new Compilation();
        if (newCompilationDto.getPinned() == null) {
            compilation.setPinned(false);
        } else {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        compilation.setTitle(newCompilationDto.getTitle());

        return compilation;
    }

    public CompilationDto compilationToCompilationDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        CompilationDto compilationDto = new CompilationDto();

        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());

        return compilationDto;
    }
}
