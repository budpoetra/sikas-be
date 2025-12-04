package com.juaracoding.sikas.service.implement;

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.TransactionDetailResponse;
import com.juaracoding.sikas.dto.response.TransactionResponse;
import com.juaracoding.sikas.dto.validation.TransactionDTO;
import com.juaracoding.sikas.dto.validation.TransactionDetailDTO;
import com.juaracoding.sikas.model.Product;
import com.juaracoding.sikas.model.Transaction;
import com.juaracoding.sikas.model.TransactionDetail;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.repository.TransactionRepository;
import com.juaracoding.sikas.repository.TransactionDetailRepository;
import com.juaracoding.sikas.security.UserDetailsImpl;
import com.juaracoding.sikas.service.TransactionService;
import com.juaracoding.sikas.util.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<Object>> createTransaction(TransactionDTO transaction, HttpServletRequest request, UserDetailsImpl userDetails) {
        try {

            // Check Produck ID exist in DB
            // Optional<Product> product = productRepository.findById()
            // produck isi data produck dari db
            // Check QTY cukup atau gak dari produck optional

            // Extract semua productId dari request
            Set<Long> productIds = new HashSet<>();
            Map<Long, TransactionDetailDTO> productDetailMap = new HashMap<>();

            // List<TransactionDetail> transactionDetailList = new ArrayList<>();

            // String transactionNumber = generateTransactionNumber();
            // Transaction trx = Transaction.builder()
                    // .userId(1)
                    // .transactionNumber()

            for (TransactionDetailDTO detail : transaction.getTransactionDetails()) {
                productIds.add(detail.getProductId());
                // Simpan detail untuk referensi nanti
                productDetailMap.put(detail.getProductId(), detail);
            }

//            System.out.println("============= +++ ==============");
//            System.out.println(productIds);

            // Ambil semua product dari database sekaligus
            List<Product> products = productRepository.findAllById(productIds);

//            products.getFirst();

//            log.info("LIST PRODUCT : {} ",products);
//            System.out.print("==================");
//            System.out.print(products);


            // Validasi: semua productId harus ada di database
            if (products.size() != productIds.size()) {
                // Cari productId yang tidak ada
                Set<Long> foundProductIds = products.stream()
                        .map(Product::getId)
                        .collect(Collectors.toSet());

                Set<Long> missingProductIds = productIds.stream()
                        .filter(id -> !foundProductIds.contains(id))
                        .collect(Collectors.toSet());

                // throw new RuntimeException("Product tidak ditemukan dengan ID: " + missingProductIds);
                // return ResponseFactory.success("Transaction Failed", HttpStatus.NOT_FOUND, "Product tidak ditemukan dengan ID: "+missingProductIds);
                return ResponseFactory.error("Transaction failed", HttpStatus.NOT_FOUND, "Product not found with ID : "+missingProductIds);
            }

            // Validasi stok cukup untuk setiap produk
            // Buat map product untuk akses cepat
            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, p -> p));

            // Hitung total qty per product
            Map<Long, Long> totalQtyPerProduct = new HashMap<>();
            for (TransactionDetailDTO detail : transaction.getTransactionDetails()) {
                long productId = detail.getProductId();
                totalQtyPerProduct.put(productId,
                        totalQtyPerProduct.getOrDefault(productId, 0L) + detail.getQtyTransaction());
            }

            // Cek stok untuk setiap product
            List<String> insufficientStockMessages = new ArrayList<>();
            for (Map.Entry<Long, Long> entry : totalQtyPerProduct.entrySet()) {
                long productId = entry.getKey();
                long requiredQty = entry.getValue();
                Product product = productMap.get(productId);

                if (product.getStock() < requiredQty) {
                    insufficientStockMessages.add(
                            String.format("Product ID %d: Stok available %d, stock transaction %d",
                                    productId, product.getStock(), requiredQty)
                    );
                }
            }

            if (!insufficientStockMessages.isEmpty()) {
//                 throw new RuntimeException("Stok tidak cukup: " + String.join("; ", insufficientStockMessages));
//                return ResponseFactory.success("Transaction Failed", HttpStatus.CONFLICT, "Stock tidak cukup : " + String.join("; ", insufficientStockMessages));
                return ResponseFactory.error("Transaction failed", HttpStatus.CONFLICT, "Insufficient stock : "+String.join("; ", insufficientStockMessages));
            }


            // Generate transaction number
            String transactionNumber = generateTransactionNumber();

            // Calculate total price
            BigDecimal totalPriceTransaction = calculateTotalPrice(transaction.getTransactionDetails());
            // BigDecimal totalPriceTransaction = BigDecimal.valueOf(100000);

            log.info("TOTAL PRICE : "+totalPriceTransaction);

            // Create transaction entity
            Transaction newTransaction = new Transaction();
            newTransaction.setUserId(userDetails.getUser().getId());
            newTransaction.setTransactionNumber(transactionNumber);
            newTransaction.setCreatedDate(LocalDateTime.now());
            newTransaction.setTotalPriceTransaction(totalPriceTransaction);

            // Save transaction
            Transaction savedTransaction = transactionRepository.save(newTransaction);
            log.info("Transaction created with ID: {}", savedTransaction.getId());

            // Create transaction details
            // List<TransactionDetail> transactionDetails = createTransactionDetails(
               //     transaction.getTransactionDetails(), savedTransaction.getId(), userDetails.getUser().getId());

            // Create transaction details dan update stock
            List<TransactionDetail> transactionDetails = new ArrayList<>();
            for (TransactionDetailDTO dto : transaction.getTransactionDetails()) {
                Product product = productMap.get(dto.getProductId());

                log.info("DATA PRODUCT : {} ",product);
                log.info("Product price : "+product.getPrice());

//                BigDecimal totalPriceDetail = BigDecimal.valueOf(dto.getPrice() * dto.getQtyTransaction());
                BigDecimal totalPriceDetail = product.getPrice().multiply(BigDecimal.valueOf(dto.getQtyTransaction()));

                // Create transaction detail
                TransactionDetail detail = new TransactionDetail();
                detail.setTransactionId(savedTransaction.getId());
                detail.setProductId(dto.getProductId());
                detail.setQtyTransaction(dto.getQtyTransaction());
//                detail.setPrice(BigDecimal.valueOf(dto.getPrice()));
                detail.setPrice(product.getPrice());
                detail.setCreatedDate(LocalDateTime.now());
                detail.setCreatedBy(userDetails.getUser().getId());
                detail.setTotalPrice(totalPriceDetail);

                transactionDetails.add(detail);

                // Update stock
                product.setStock(product.getStock() - dto.getQtyTransaction());
            }

            // Save updated products (stock update)
            productRepository.saveAll(products);

            // Save all transaction details
            List<TransactionDetail> savedDetails = transactionDetailRepository.saveAll(transactionDetails);

            // Prepare response
            return ResponseFactory.success("Transaction Success", HttpStatus.CREATED, savedDetails);

        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create transaction: " + e.getMessage());

        }
    }

    @Override
    public TransactionResponse getTransactionById(Integer id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));

        List<TransactionDetail> details = transactionDetailRepository.findByTransactionId(id);

        return mapToTransactionResponse(transaction, details);
    }

    @Override
    public TransactionResponse getTransactionByTransactionNumber(String transactionNumber) {
        Transaction transaction = transactionRepository.findByTransactionNumber(transactionNumber)
                .orElseThrow(() -> new RuntimeException("Transaction not found with number: " + transactionNumber));

        List<TransactionDetail> details = transactionDetailRepository.findByTransactionId(transaction.getId());

        return mapToTransactionResponse(transaction, details);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(transaction -> {
            List<TransactionDetail> details = transactionDetailRepository.findByTransactionId(transaction.getId());
            return mapToTransactionResponse(transaction, details);
        }).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByUserId(Integer userId) {
        List<Transaction> allTransactions = transactionRepository.findAll();
        return allTransactions.stream()
                .filter(transaction -> transaction.getUserId().equals(userId))
                .map(transaction -> {
                    List<TransactionDetail> details = transactionDetailRepository.findByTransactionId(transaction.getId());
                    return mapToTransactionResponse(transaction, details);
                })
                .collect(Collectors.toList());
    }

    private String generateTransactionNumber() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() + "-" + System.currentTimeMillis();
    }

//    private BigDecimal calculateTotalPrice(List<TransactionDetailDTO> details) {
//        return details.stream()
//                .map(detail -> BigDecimal.valueOf(1000 * detail.getQtyTransaction()))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }

    private BigDecimal calculateTotalPrice(List<TransactionDetailDTO> transactionDetails) {
        // Kumpulkan semua productId
        List<Long> productIds = transactionDetails.stream()
                .map(TransactionDetailDTO::getProductId)
                .collect(Collectors.toList());

        // Ambil semua produk sekaligus
        List<Product> products = productRepository.findAllById(productIds);

        // Buat map untuk akses cepat
        Map<Long, BigDecimal> productPriceMap = products.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Product::getPrice
                ));

        // Hitung total
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (TransactionDetailDTO detail : transactionDetails) {
            BigDecimal price = productPriceMap.get(detail.getProductId());
            if (price == null) {
//                throw new ResourceNotFoundException("Product not found with id: " + detail.getProductId());
                throw new RuntimeException("Product not found with id: "+detail.getProductId());
            }

            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(detail.getQtyTransaction()));
            totalPrice = totalPrice.add(subtotal);
        }

        return totalPrice;
    }

//    private List<TransactionDetail> createTransactionDetails(
//            List<TransactionDetailDTO> detailRequests, Integer transactionId, Integer createdBy) {
//
//        return detailRequests.stream().map(detailRequest -> {
//            TransactionDetail detail = new TransactionDetail();
//            detail.setTransactionId(transactionId);
//            detail.setProductId(detailRequest.getProductId());
//            detail.setQtyTransaction(detailRequest.getQtyTransaction());
//            // detail.setPrice(BigDecimal.valueOf(detailRequest.getPrice()));
//            // detail.setTotalPrice(BigDecimal.valueOf(detailRequest.getPrice() * detailRequest.getQtyTransaction()));
//            detail.setPrice(BigDecimal.valueOf(4000));
//            detail.setTotalPrice(BigDecimal.valueOf(4000 * detailRequest.getQtyTransaction()));
//            detail.setCreatedDate(LocalDateTime.now());
//            detail.setCreatedBy(createdBy);
//            return detail;
//        }).collect(Collectors.toList());
//    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction, List<TransactionDetail> details) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setUserId(transaction.getUserId());
        response.setTransactionNumber(transaction.getTransactionNumber());
        response.setCreatedDate(transaction.getCreatedDate());
        response.setTotalPriceTransaction(transaction.getTotalPriceTransaction());

        List<TransactionDetailResponse> detailResponses = details.stream()
                .map(this::mapToTransactionDetailResponse)
                .collect(Collectors.toList());

        response.setTransactionDetails(detailResponses);
        return response;
    }

    private TransactionDetailResponse mapToTransactionDetailResponse(TransactionDetail detail) {
        TransactionDetailResponse response = new TransactionDetailResponse();
        response.setId(detail.getId());
        response.setTransactionId(detail.getTransactionId());
        response.setProductId(detail.getProductId());
        response.setQtyTransaction(detail.getQtyTransaction());
        response.setPrice(detail.getPrice());
        response.setTotalPrice(detail.getTotalPrice());
        response.setCreatedDate(detail.getCreatedDate());
        response.setCreatedBy(detail.getCreatedBy());
        return response;
    }
}