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
    private final String PAYMENT_METHOD_PREFIX = "Método: ";
    private final String CARD_NUMBER_PREFIX = "\nNúmero do cartão: ";
    private final String CARD_NAME_PREFIX = "\nNome impresso no cartão: ";
    private final String CARD_EXPIRATION_MONTH_PREFIX = "\nMês de expiração: ";
    private final String CARD_EXPIRATION_YEAR_PREFIX = "\nAno de expiração: ";
    private final String MONEY_CHANGE_PREFIX = "\nTroco: ";
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

    public String printBrief() {
        String printMethod;

        if (method.equals(METHOD_CREDIT_CARD)) {
            printMethod = "Cartão de crédito";

            if (expirationMonthIndex >= 0)
                return PAYMENT_METHOD_PREFIX + printMethod +
                        CARD_NUMBER_PREFIX + number +
                        CARD_NAME_PREFIX + name +
                        CARD_EXPIRATION_MONTH_PREFIX + MONTHS[expirationMonthIndex] +
                        CARD_EXPIRATION_YEAR_PREFIX + expirationYear;
            else
                return PAYMENT_METHOD_PREFIX + printMethod +
                        CARD_NUMBER_PREFIX + number +
                        CARD_NAME_PREFIX + name +
                        CARD_EXPIRATION_MONTH_PREFIX + "no month setted" +
                        CARD_EXPIRATION_YEAR_PREFIX + expirationYear;
        }
        else {
            printMethod = "Dinheiro";

            return PAYMENT_METHOD_PREFIX + printMethod +
                    MONEY_CHANGE_PREFIX + moneyChange;
        }
    }
}
