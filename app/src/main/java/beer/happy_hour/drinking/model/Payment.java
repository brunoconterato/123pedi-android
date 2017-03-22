package beer.happy_hour.drinking.model;

/**
 * Created by brcon on 22/03/2017.
 */

public class Payment {

    public static final String METHOD_CREDIT_CARD = "credit_card";
    public static final String METHOD_MONEY = "money";
    private static final String[] MONTHS = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
            "Outubro", "Novembro", "Dezembro"};
    private static Payment instance;
    private String method = "";
    private String number = "";
    private String name = "";
    private int expirationMonthIndex = -1;
    private int expirationYear = -1;
    private String securityCode = "";
    private String moneyChange = "";

    private Payment() {
    }

    public static Payment getInstance() {
        if (instance == null) {
            synchronized (Payment.class) {
                if (instance == null) {
                    instance = new Payment();
                }
            }
        }
        return instance;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpirationMonthIndex() {
        return expirationMonthIndex;
    }

    public void setExpirationMonthIndex(int expirationMonthIndex) {
        this.expirationMonthIndex = expirationMonthIndex;
    }

    public String getExpirationMonthName() {
        return MONTHS[expirationMonthIndex - 1];
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getMoneyChange() {
        return moneyChange;
    }

    public void setMoneyChange(String moneyChange) {
        this.moneyChange = moneyChange;
    }

    @Override
    public String toString() {
        if (expirationMonthIndex >= 0)
            return "Method: " + method +
                    "\nNumber: " + number +
                    "\nName: " + name +
                    "\nExpiration month índex: " + expirationMonthIndex +
                    "\nExpiration month name: " + MONTHS[expirationMonthIndex] +
                    "\nExpiration year: " + expirationYear +
                    "\nMoney change: " + moneyChange;
        else
            return "Method: " + method +
                    "\nNumber: " + number +
                    "\nName: " + name +
                    "\nExpiration month índex: " + expirationMonthIndex +
                    "\nExpiration month name: " + "no month setted" +
                    "\nExpiration year: " + expirationYear +
                    "\nMoney change: " + moneyChange;
    }
}
