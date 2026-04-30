package in.amrobasyouni.CashPilot.service;

import in.amrobasyouni.CashPilot.dto.CategoryDTO;
import in.amrobasyouni.CashPilot.entity.CategoryEntity;
import in.amrobasyouni.CashPilot.entity.ProfileEntity;
import in.amrobasyouni.CashPilot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    //save category
    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            throw new RuntimeException("Category Already Exists");
        }

        CategoryEntity newCategory = toEntity(categoryDTO, profile);
        newCategory = categoryRepository.save(newCategory);
        return toDto(newCategory);

    }

    public List<CategoryDTO> findCategoriesForCurrentUser(Long id){
        ProfileEntity profile = profileService.getCurrentProfile();
        if (profile.getId()!=id){throw new RuntimeException("Access Denied");}
        List<CategoryEntity> AllCategories = categoryRepository.findByProfileId(id);
        return AllCategories.stream().map(this::toDto).toList();
    }

    public List<CategoryDTO> getCategoriesByType(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> newList = categoryRepository.findByTypeAndProfileId(type,profile.getId());
        return newList.stream().map(this::toDto).toList();
    }

    public CategoryDTO updateCategoriesById(Long categoryId ,CategoryDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity newCategory = categoryRepository.findByIdAndProfileId(categoryId,profile.getId());
        newCategory.setName(dto.getName());
        newCategory.setType(dto.getType());
        newCategory.setIcon(dto.getIcon());

        newCategory = categoryRepository.save(newCategory);
        return toDto(newCategory);
    }

    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .type(categoryDTO.getType())
                .profile(profileEntity)
                .build();
    }

    private CategoryDTO toDto(CategoryEntity categoryEntity){
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() != null?categoryEntity.getProfile().getId():null)
                .name(categoryEntity.getName())
                .type(categoryEntity.getType())
                .icon(categoryEntity.getIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();
    }
}
