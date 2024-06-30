package tests.factory;

import entities.Financing;

public class FinancingFactory {

    public static Financing createFinancing(){
        return new Financing(100000.0, 2000.0,80);
    }
}
