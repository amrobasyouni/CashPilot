package in.amrobasyouni.CashPilot.service;


import in.amrobasyouni.CashPilot.dto.ExpenseDTO;
import in.amrobasyouni.CashPilot.dto.IncomeDTO;
import in.amrobasyouni.CashPilot.entity.CategoryEntity;
import in.amrobasyouni.CashPilot.entity.ExpenseEntity;
import in.amrobasyouni.CashPilot.entity.IncomeEntity;
import in.amrobasyouni.CashPilot.entity.ProfileEntity;
import in.amrobasyouni.CashPilot.repository.CategoryRepository;
import in.amrobasyouni.CashPilot.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final IncomeRepository incomeRepository;

    public IncomeDTO addIncome(IncomeDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category not found"));

        if(profile.getId() != category.getProfile().getId()){
            throw new RuntimeException("Category not found bcz of user");
        }
        IncomeEntity newIncome = toEntity(dto,profile,category);
        incomeRepository.save(newIncome);
        return toDTO(newIncome);
    }

    //Retrieve all incomes for the current profile between two dates
    public List<IncomeDTO> getCurrentMonthIncomes(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> incomes = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return incomes.stream().map(this::toDTO).toList();

    }

    //Delete Expense By Id for current user
    public void deleteIncome(Long incomeId){
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
                .orElseThrow(()->new RuntimeException("Cannot Find Expense"));
        IncomeEntity IncomeForDeletion = incomeRepository.findById(incomeId)
                .orElseThrow(()->new RuntimeException("cannot find the expense"));
        if(entity.getProfile().getId().equals(profile.getId())){
            incomeRepository.delete(IncomeForDeletion);
        }else {throw new RuntimeException("Unauthorized Deletion");}

    }

    private IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .amount(incomeDTO.getAmount())
                .date(incomeDTO.getDate())
                .profile(profile)
                .category(category)
                .build();

    }

    private IncomeDTO toDTO(IncomeEntity entity){
        return IncomeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory()!=null? entity.getCategory().getId():null)
                .categoryName(entity.getCategory()!=null?entity.getCategory().getName():"N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
