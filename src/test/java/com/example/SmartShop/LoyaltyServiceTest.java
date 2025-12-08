package com.example.SmartShop;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.enums.CustomerTier;
import com.example.SmartShop.service.impl.LoyaltyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoyaltyServiceTest {

    @InjectMocks
    private LoyaltyServiceImpl loyaltyService;

    @BeforeEach
    void setUp() {
        // Configurer les valeurs des propriétés pour les tests
        ReflectionTestUtils.setField(loyaltyService, "silverOrdersThreshold", 3);
        ReflectionTestUtils.setField(loyaltyService, "silverAmountThreshold", 1000);
        ReflectionTestUtils.setField(loyaltyService, "goldOrdersThreshold", 10);
        ReflectionTestUtils.setField(loyaltyService, "goldAmountThreshold", 5000);
        ReflectionTestUtils.setField(loyaltyService, "platinumOrdersThreshold", 20);
        ReflectionTestUtils.setField(loyaltyService, "platinumAmountThreshold", 15000);

        ReflectionTestUtils.setField(loyaltyService, "silverDiscountRate", 5);
        ReflectionTestUtils.setField(loyaltyService, "silverMinimumPurchase", 500);
        ReflectionTestUtils.setField(loyaltyService, "goldDiscountRate", 10);
        ReflectionTestUtils.setField(loyaltyService, "goldMinimumPurchase", 800);
        ReflectionTestUtils.setField(loyaltyService, "platinumDiscountRate", 15);
        ReflectionTestUtils.setField(loyaltyService, "platinumMinimumPurchase", 1200);
    }

    @Test
    void testCalculeTier_ShouldReturnBasic_WhenClientHasNoOrders() {
        // Given
        Client client = Client.builder()
                .nom("Test Client")
                .email("test@example.com")
                .TotalOrders(0)
                .totalSpent(BigDecimal.ZERO)
                .build();

        // When
        CustomerTier tier = loyaltyService.calculeTier(client);

        // Then
        assertEquals(CustomerTier.BASIC, tier);
    }

    @Test
    void testCalculeTier_ShouldReturnSilver_WhenClientHas3Orders() {
        // Given
        Client client = Client.builder()
                .nom("Test Client")
                .email("test@example.com")
                .TotalOrders(3)
                .totalSpent(BigDecimal.valueOf(500))
                .build();

        // When
        CustomerTier tier = loyaltyService.calculeTier(client);

        // Then
        assertEquals(CustomerTier.SILVER, tier);
    }

    @Test
    void testCalculeTier_ShouldReturnGold_WhenClientHas10Orders() {
        // Given
        Client client = Client.builder()
                .nom("Test Client")
                .email("test@example.com")
                .TotalOrders(10)
                .totalSpent(BigDecimal.valueOf(3000))
                .build();

        // When
        CustomerTier tier = loyaltyService.calculeTier(client);

        // Then
        assertEquals(CustomerTier.GOLD, tier);
    }

    @Test
    void testCalculeTier_ShouldReturnPlatinum_WhenClientSpent15000() {
        // Given
        Client client = Client.builder()
                .nom("Test Client")
                .email("test@example.com")
                .TotalOrders(5)
                .totalSpent(BigDecimal.valueOf(15000))
                .build();

        // When
        CustomerTier tier = loyaltyService.calculeTier(client);

        // Then
        assertEquals(CustomerTier.PLATINUM, tier);
    }

    @Test
    void testCalculateDiscountRate_ShouldReturn5_ForSilverWithEligibleAmount() {
        // Given
        CustomerTier tier = CustomerTier.SILVER;
        BigDecimal sousTotal = BigDecimal.valueOf(600);

        // When
        BigDecimal discountRate = loyaltyService.calculateDiscountRate(tier, sousTotal);

        // Then
        assertEquals(BigDecimal.valueOf(5), discountRate);
    }

    @Test
    void testCalculateDiscountRate_ShouldReturnZero_ForSilverWithIneligibleAmount() {
        // Given
        CustomerTier tier = CustomerTier.SILVER;
        BigDecimal sousTotal = BigDecimal.valueOf(400);

        // When
        BigDecimal discountRate = loyaltyService.calculateDiscountRate(tier, sousTotal);

        // Then
        assertEquals(BigDecimal.ZERO, discountRate);
    }

    @Test
    void testCalculateDiscountAmount_ShouldCalculateCorrectly_ForGoldTier() {
        // Given
        CustomerTier tier = CustomerTier.GOLD;
        BigDecimal sousTotal = BigDecimal.valueOf(1000);

        // When
        BigDecimal discountAmount = loyaltyService.calculateDiscountAmount(tier, sousTotal);

        // Then
        assertEquals(new BigDecimal("100.00"), discountAmount);
    }

    @Test
    void testIsEligibleForDiscount_ShouldReturnTrue_ForPlatinumWithSufficientAmount() {
        // Given
        CustomerTier tier = CustomerTier.PLATINUM;
        BigDecimal sousTotal = BigDecimal.valueOf(1500);

        // When
        boolean eligible = loyaltyService.isEligibleForDiscount(tier, sousTotal);

        // Then
        assertTrue(eligible);
    }

    @Test
    void testIsEligibleForDiscount_ShouldReturnFalse_ForBasicTier() {
        // Given
        CustomerTier tier = CustomerTier.BASIC;
        BigDecimal sousTotal = BigDecimal.valueOf(10000);

        // When
        boolean eligible = loyaltyService.isEligibleForDiscount(tier, sousTotal);

        // Then
        assertFalse(eligible);
    }

    @Test
    void testUpdateClientTier_ShouldUpdateTier_WhenClientQualifiesForUpgrade() {
        // Given
        Client client = Client.builder()
                .nom("Test Client")
                .email("test@example.com")
                .niveauFidelite(CustomerTier.BASIC)
                .TotalOrders(3)
                .totalSpent(BigDecimal.valueOf(1500))
                .build();

        // When
        loyaltyService.updateClientTier(client);

        // Then
        assertEquals(CustomerTier.SILVER, client.getNiveauFidelite());
    }

    @Test
    void testUpdateClientTier_ShouldNotChangeTier_WhenClientDoesNotQualify() {
        // Given
        Client client = Client.builder()
                .nom("Test Client")
                .email("test@example.com")
                .niveauFidelite(CustomerTier.BASIC)
                .TotalOrders(1)
                .totalSpent(BigDecimal.valueOf(500))
                .build();

        // When
        loyaltyService.updateClientTier(client);

        // Then
        assertEquals(CustomerTier.BASIC, client.getNiveauFidelite());
    }
}
