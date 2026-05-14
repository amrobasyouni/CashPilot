package in.amrobasyouni.CashPilot.service;

import in.amrobasyouni.CashPilot.dto.ExpenseDTO;
import in.amrobasyouni.CashPilot.entity.CategoryEntity;
import in.amrobasyouni.CashPilot.entity.ExpenseEntity;
import in.amrobasyouni.CashPilot.entity.ProfileEntity;
import in.amrobasyouni.CashPilot.repository.CategoryRepository;
import in.amrobasyouni.CashPilot.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final ExpenseRepository expenseRepository;

    public ExpenseDTO addExpense(ExpenseDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category not found"));
        if(!(profile.getId().equals(category.getProfile().getId()))){
            throw new RuntimeException("Category not found bcz of user");
        }
        ExpenseEntity newExpense = toEntity(dto,profile,category);
        expenseRepository.save(newExpense);
        return toDTO(newExpense);
    }

    //Retrieve all expenses for the current profile between two dates
    public List<ExpenseDTO> getCurrentMonthExpenses(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> expenses = expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return expenses.stream().map(this::toDTO).toList();

    }

    //Delete Expense By Id for current user
    public void deleteExpense(Long expenseId){
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity entity = expenseRepository.findById(expenseId)
                .orElseThrow(()->new RuntimeException("Cannot Find Expense"));
        ExpenseEntity expenseForDeletion = expenseRepository.findById(expenseId)
                        .orElseThrow(()->new RuntimeException("cannot find the expense"));
        if(entity.getProfile().getId().equals(profile.getId())){
            expenseRepository.delete(expenseForDeletion);
        }else {throw new RuntimeException("Unauthorized Deletion");}

    }

    //get latest 5 expenses for current user
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> topFive = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return topFive.stream().map(this::toDTO).toList();
    }

    //get total expenses for current User
    public BigDecimal totalExpenses(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total != null ? total: BigDecimal.ZERO;
    }

    //filter expenses
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort ){
       ProfileEntity profile = profileService.getCurrentProfile();
       List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
       return list.stream().map(this::toDTO).toList();
    }

    //Notifications
    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date){
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDate(profileId,date);
        return list.stream().map(this::toDTO).toList();
    }

    private ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profile, CategoryEntity category){
        return ExpenseEntity.builder()
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .profile(profile)
                .category(category)
                .build();

    }

    private ExpenseDTO toDTO(ExpenseEntity entity){
        return ExpenseDTO.builder()
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
