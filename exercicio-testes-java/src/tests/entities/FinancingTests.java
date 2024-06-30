package tests.entities;

import entities.Financing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.factory.FinancingFactory;

public class FinancingTests {

    @Test
    public void constructorShouldCreateObjectWhenValidData(){
        Financing financing = FinancingFactory.createFinancingValitData();

        Assertions.assertEquals(100000.0, financing.getTotalAmount());
        Assertions.assertEquals(2000.0, financing.getIncome());
        Assertions.assertEquals(80, financing.getMonths());
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionWhenInvalidData(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Financing financing = FinancingFactory.createFinancingInvalitData();
        });
    }

    @Test
    public void setTotalAmountShouldUpdateDataWhenValidData(){
        Financing financing = FinancingFactory.createFinancingValitData();

        financing.setTotalAmount(10000.0);

        Assertions.assertEquals(10000.0, financing.getTotalAmount());
    }

    @Test
    public void setTotalAmountShouldThrowIllegalArgumentExceptionWhenInvalidData(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Financing financing = FinancingFactory.createFinancingValitData();
            financing.setTotalAmount(100001.0);
        });
    }

    @Test
    public void setIncomeShouldUpdateDataWhenValidData(){
        Financing financing = FinancingFactory.createFinancingValitData();

        financing.setIncome(2001.0);

        Assertions.assertEquals(2001.0, financing.getIncome());
    }
    
    @Test
    public void setIncomeShouldThrowIllegalArgumentExceptionWhenInvalidData(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Financing financing = FinancingFactory.createFinancingValitData();
            financing.setIncome(1999.9);
        });
    }
}
