package pl.agh.po.diskrak.network;

import java.math.BigDecimal;

public class BudgetManager {
    public boolean canAfford(BigDecimal amount){
        //sprawdza czy currentBalance.compareTo(amount) >= 0 .
    }
    public void addRevenue(BigDecimal amount, String title){
        //podaje przychód z biletów/dotacji
    }
    public void spend(BigDecimal amount, String reason){
        //odemuje kwotę z konta (chyba, że brakuje środków)
    }
}