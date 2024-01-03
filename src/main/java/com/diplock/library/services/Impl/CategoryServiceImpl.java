package com.diplock.library.services.Impl;

import com.diplock.library.dataholders.CategoryDh;
import com.diplock.library.dtos.CategoryDTO;
import com.diplock.library.entities.category.Category;
import com.diplock.library.mapper.CategoryMapper;
import com.diplock.library.repositories.CategoryRepository;
import com.diplock.library.services.CategoryService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  @NonNull
  private final CategoryRepository categoryRepository;

  @NonNull
  private CategoryMapper categoryMapper;

  @Override
  public List<CategoryDTO> findAll() {
    final List<Category> categoryList = (List<Category>) categoryRepository.findAll();
    if (CollectionUtils.isEmpty(categoryList)) {
      log.warn("There are not categories en the table categories");
      return Collections.emptyList();
    } else {
      return categoryMapper.asDtoList(categoryList);
    }
  }

  @Override
  public CategoryDTO findById(final Long categoryid) {
    final Optional<Category> category = categoryRepository.findById(categoryid);
    if (category.isPresent()) {
      return categoryMapper.asDTO(category.get());
    } else {
      log.warn("There is not category en the table categories with the id: {}", categoryid);
      return null;
    }
  }

  @Override
  public CategoryDTO save(final CategoryDh categoryDh) {
    final Category category = categoryMapper.asEntity(categoryDh);
    final Category categorySaved = categoryRepository.save(category);
    return categoryMapper.asDTO(categorySaved);
  }

  @Override
  public CategoryDTO update(final Long categoryid, final CategoryDh categoryDh) {
    final Category category = categoryMapper.asEntity(categoryDh);
    if (categoryRepository.existsById(categoryid)) {
        return categoryMapper.asDTO(categoryRepository.save(category));
    }
    log.warn("Update failed.  There is no category en the table categories with the id: {}", categoryid);
    return null;
  }

  @Override
  public Boolean delete(Long categoryid) {
    if (categoryRepository.existsById(categoryid)) {
      categoryRepository.deleteById(categoryid);
      return true;
    }
    log.warn("Delete failed.  There is no category in the table categories with the id: {}", categoryid);
    return false;
  }
}
