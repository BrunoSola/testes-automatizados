package tests.factory;

import entities.Financing;

public class FinancingFactory {

    public static Financing createFinancingValitData(){
        return new Financing(100000.0, 2000.0,80);
    }
    public static Financing createFinancingInvalitData(){
        return new Financing(100000.0, 2000.0,20);
    }
}
