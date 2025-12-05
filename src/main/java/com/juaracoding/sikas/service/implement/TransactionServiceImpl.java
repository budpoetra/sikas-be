package com.juaracoding.sikas.service.implement;

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.TransactionDetailResponse;
import com.juaracoding.sikas.dto.response.TransactionResponse;
import com.juaracoding.sikas.dto.validation.TransactionDTO;
import com.juaracoding.sikas.dto.validation.TransactionDetailDTO;
import com.juaracoding.sikas.exception.TrxInsufficientStockException;
import com.juaracoding.sikas.exception.TrxNotFoundException;
import com.juaracoding.sikas.exception.TrxValidationException;
import com.juaracoding.sikas.model.Product;
import com.juaracoding.sikas.model.Transaction;
import com.juaracoding.sikas.model.TransactionDetail;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.repository.TransactionRepository;
import com.juaracoding.sikas.repository.TransactionDetailRepository;
import com.juaracoding.sikas.security.UserDetailsImpl;
import com.juaracoding.sikas.service.TransactionService;
import com.juaracoding.sikas.util.ResponseFactory;
import com.juaracoding.sikas.util.TransactionNumberGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
//    @Autowired
    private final ProductRepository productRepository;
    private final TransactionNumberGenerator transactionNumberGenerator;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> createTransaction(TransactionDTO transactionDTO,
                                                                 HttpServletRequest request,
                                                                 UserDetailsImpl userDetails) {

        validateTransactionRequest(transactionDTO);

        // Fetch products and validate
        Map<Long, Product> productMap = fetchAndValidateProducts(transactionDTO.getTransactionDetails());

        // Check stock availability
        validateStockAvailability(transactionDTO.getTransactionDetails(), productMap);

        // Create and save transaction
        Transaction transaction = createTransactionEntity(transactionDTO, userDetails);
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Create transaction details and update stock
        List<TransactionDetail> transactionDetails = createTransactionDetails(
                transactionDTO.getTransactionDetails(),
                savedTransaction.getId(),
                userDetails.getUser().getId(),
                productMap
        );

        // Update product stock
        updateProductStock(transactionDTO.getTransactionDetails(), productMap);
        productRepository.saveAll(productMap.values());

        // Save transaction details
        List<TransactionDetail> savedDetails = transactionDetailRepository.saveAll(transactionDetails);

        log.info("Transaction created successfully. Transaction ID: {}, User ID: {}",
                savedTransaction.getId(), userDetails.getUser().getId());

        return ResponseFactory.success(
                "Transaction created successfully",
                HttpStatus.CREATED,
                mapToTransactionResponse(savedTransaction, savedDetails)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Integer id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TrxNotFoundException("Transaction not found with ID: " + id));

        List<TransactionDetail> details = transactionDetailRepository.findByTransactionId(id);
        return mapToTransactionResponse(transaction, details);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByTransactionNumber(String transactionNumber) {
        Transaction transaction = transactionRepository.findByTransactionNumber(transactionNumber)
                .orElseThrow(() -> new TrxNotFoundException("Transaction not found with number: " + transactionNumber));

        List<TransactionDetail> details = transactionDetailRepository.findByTransactionId(transaction.getId());
        return mapToTransactionResponse(transaction, details);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAllWithDetails()
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(this::mapToTransactionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUserId(Integer userId) {
        return transactionRepository.findByUserIdWithDetails(userId)
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private void validateTransactionRequest(TransactionDTO transactionDTO) {
        if (transactionDTO == null || transactionDTO.getTransactionDetails() == null) {
            throw new TrxValidationException("Transaction request cannot be null");
        }

        if (transactionDTO.getTransactionDetails().isEmpty()) {
            throw new TrxValidationException("Transaction must have at least one item");
        }

        // Validasi duplicate product
        Set<Long> productIds = new HashSet<>();
        for (TransactionDetailDTO detail : transactionDTO.getTransactionDetails()) {
            if (!productIds.add(detail.getProductId())) {
                throw new TrxValidationException("Duplicate product ID: " + detail.getProductId());
            }

            if (detail.getQtyTransaction() <= 0) {
                throw new TrxValidationException("Quantity must be greater than 0 for product ID: " + detail.getProductId());
            }
        }
    }

    private Map<Long, Product> fetchAndValidateProducts(List<TransactionDetailDTO> details) {
        Set<Long> productIds = details.stream()
                .map(TransactionDetailDTO::getProductId)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            Set<Long> foundIds = products.stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());

            Set<Long> missingIds = productIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new TrxNotFoundException("Products not found with IDs: " + missingIds);
        }

        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private void validateStockAvailability(List<TransactionDetailDTO> details, Map<Long, Product> productMap) {
        List<String> insufficientStockMessages = new ArrayList<>();

        for (TransactionDetailDTO detail : details) {
            Product product = productMap.get(detail.getProductId());
            if (product.getStock() < detail.getQtyTransaction()) {
                insufficientStockMessages.add(
                        String.format("Product '%s' (ID: %d): Available %d, Requested %d",
                                product.getProductName(),
                                product.getId(),
                                product.getStock(),
                                detail.getQtyTransaction())
                );
            }
        }

        if (!insufficientStockMessages.isEmpty()) {
            throw new TrxInsufficientStockException("Insufficient stock: " + String.join("; ", insufficientStockMessages));
        }
    }

    private Map<Long, Product> fetchProductMap(List<TransactionDetailDTO> transactionDetails) {
        // Kumpulkan semua productId dari transactionDetails
        List<Long> productIds = transactionDetails.stream()
                .map(TransactionDetailDTO::getProductId)
                .collect(Collectors.toList());

        // Fetch semua produk sekaligus (lebih efisien daripada loop)
        List<Product> products = productRepository.findAllById(productIds);

        // Convert ke Map untuk akses cepat
        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private Transaction createTransactionEntity(TransactionDTO transactionDTO, UserDetailsImpl userDetails) {

        // Fetch productMap
        Map<Long, Product> productMap = fetchProductMap(transactionDTO.getTransactionDetails());

        Transaction transaction = new Transaction();
        transaction.setUserId(userDetails.getUser().getId());
        transaction.setTransactionNumber(transactionNumberGenerator.generate());
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setTotalPriceTransaction(calculateTotalPrice(transactionDTO.getTransactionDetails(),productMap));
        return transaction;
    }

    private List<TransactionDetail> createTransactionDetails(List<TransactionDetailDTO> detailDTOs,
                                                             Integer transactionId,
                                                             Integer createdBy,
                                                             Map<Long, Product> productMap) {
        return detailDTOs.stream()
                .map(dto -> {
                    Product product = productMap.get(dto.getProductId());
                    BigDecimal subtotal = product.getPrice()
                            .multiply(BigDecimal.valueOf(dto.getQtyTransaction()));

                    return TransactionDetail.builder()
                            .transactionId(transactionId)
                            .productId(dto.getProductId())
                            .qtyTransaction(dto.getQtyTransaction())
                            .price(product.getPrice())
                            .totalPrice(subtotal)
                            .createdDate(LocalDateTime.now())
                            .createdBy(createdBy)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private void updateProductStock(List<TransactionDetailDTO> details, Map<Long, Product> productMap) {
        details.forEach(dto -> {
            Product product = productMap.get(dto.getProductId());
            product.setStock(product.getStock() - dto.getQtyTransaction());
        });
    }

    private BigDecimal calculateTotalPrice(List<TransactionDetailDTO> details, Map<Long, Product> productMap) {
        return details.stream()
                .map(dto -> {
                    Product product = productMap.get(dto.getProductId());
                    if (product == null) {
                        throw new TrxNotFoundException("Product not found with id: " + dto.getProductId());
                    }
                    return product.getPrice().multiply(BigDecimal.valueOf(dto.getQtyTransaction()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        List<TransactionDetail> details = transactionDetailRepository.findByTransactionId(transaction.getId());
        return mapToTransactionResponse(transaction, details);
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction, List<TransactionDetail> details) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .transactionNumber(transaction.getTransactionNumber())
                .createdDate(transaction.getCreatedDate())
                .totalPriceTransaction(transaction.getTotalPriceTransaction())
                .transactionDetails(mapToDetailResponses(details))
                .build();
    }

    private List<TransactionDetailResponse> mapToDetailResponses(List<TransactionDetail> details) {
        return details.stream()
                .map(this::mapToTransactionDetailResponse)
                .collect(Collectors.toList());
    }

    private TransactionDetailResponse mapToTransactionDetailResponse(TransactionDetail detail) {
        return TransactionDetailResponse.builder()
                .id(detail.getId())
                .transactionId(detail.getTransactionId())
                .productId(detail.getProductId())
                .qtyTransaction(detail.getQtyTransaction())
                .price(detail.getPrice())
                .totalPrice(detail.getTotalPrice())
                .createdDate(detail.getCreatedDate())
                .createdBy(detail.getCreatedBy())
                .build();
    }
}