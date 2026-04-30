package in.amrobasyouni.CashPilot.controller;

import in.amrobasyouni.CashPilot.dto.CategoryDTO;
import in.amrobasyouni.CashPilot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);

    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CategoryDTO>> findCategoriesByProfileId(@PathVariable Long id){
        List<CategoryDTO> categories = categoryService.findCategoriesForCurrentUser(id);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/byType")
    public ResponseEntity<List<CategoryDTO>> findAllTypesByProfileId(@RequestBody CategoryDTO dto){
        List<CategoryDTO> types = categoryService.getCategoriesByType(dto.getType());
        return ResponseEntity.ok(types);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO dto){
       CategoryDTO updatedCategory = categoryService.updateCategoriesById(id,dto);
       return ResponseEntity.ok(updatedCategory);
    }

}
