package com.jacobIbrom.finance_tracker.service;

import com.jacobIbrom.finance_tracker.dto.TransactionRequest;
import com.jacobIbrom.finance_tracker.dto.TransactionResponse;
import com.jacobIbrom.finance_tracker.model.Category;
import com.jacobIbrom.finance_tracker.model.Transaction;
import com.jacobIbrom.finance_tracker.model.User;
import com.jacobIbrom.finance_tracker.repository.CategoryRepository;
import com.jacobIbrom.finance_tracker.repository.TransactionRepository;
import com.jacobIbrom.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    //Create transaction
    public TransactionResponse createTransaction(TransactionRequest request, String email) {
        User user = getUser(email);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!Objects.equals(category.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setCategory(category);
        transaction.setUser(user);

        return TransactionResponse.from(transactionRepository.save(transaction));
    }
    //Get transactions
    public List<TransactionResponse> getUserTransactions(String email) {
        User user = getUser(email);
        return transactionRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }
    //Delete transaction
    public void deleteTransaction(Long id, String email) {
        User user = getUser(email);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!Objects.equals(transaction.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        transactionRepository.delete(transaction);
    }
}