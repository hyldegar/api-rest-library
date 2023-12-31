package com.diplock.library.controllers;

import com.diplock.library.dtos.CategoryDTO;
import com.diplock.library.entities.Category;
import com.diplock.library.mapper.CategoryMapper;

import com.diplock.library.services.CategoryService;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;


    @GetMapping("/find/{categoryid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findById(@PathVariable Long categoryid) {
      Optional<Category> categoryOptional = categoryService.findById(categoryid);

      if (categoryOptional.isPresent()) {
        Category category = categoryOptional.get();

        CategoryDTO categoryDTO = categoryMapper.toDTO(category);
        return ResponseEntity.ok(categoryDTO);
      }

      return ResponseEntity.notFound().build();
    }

    @GetMapping("/find/All")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findAll() {
      List<CategoryDTO> categoryList = categoryMapper.toDTOList(categoryService.findAll());

      return ResponseEntity.ok(categoryList);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@RequestBody CategoryDTO categoryDTO) {

      if (categoryDTO.getName().isBlank()) {
          return ResponseEntity.badRequest().build();
      }

      Category category = categoryMapper.toEntity(categoryDTO);
      CategoryDTO categoryReturnDTO = categoryMapper.toDTO(categoryService.save(category));
      return ResponseEntity.ok(categoryReturnDTO);
    }

    @PutMapping("/update/{categoryid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@PathVariable Long categoryid, @RequestBody CategoryDTO categoryDTO) {
      Optional<Category> categoryOptional = categoryService.findById(categoryid);

      if (categoryOptional.isPresent()){
          Category category = categoryOptional.get();
          category.setName(categoryDTO.getName());
          category.setSubtopic(categoryDTO.getSubtopic());
          categoryService.update(category);
          return ResponseEntity.ok("Update registre");
      }

      return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{categoryid}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> deleteById(@PathVariable Long categoryid) {
      if (categoryid != null) {
        Optional<Category> categoryOptional = categoryService.findById(categoryid);

        if (categoryOptional.isPresent()){
          categoryService.delete(categoryid);
          return ResponseEntity.ok("Delete registre");
        }
      }

      return ResponseEntity.badRequest().build();
    }

}
