package beer.happy_hour.drinking.model;

import java.util.ArrayList;

/**
 * Created by brcon on 15/03/2017.
 */

public class User {
    private static User instance;

    private String name;
    private String email;
    private String phone;

    private String USER_NAME_PREFIX = "Nome: ";
    private String USER_PHONE_PREFIX = "\nTelefone: ";
    private String USER_EMAIL_PREFIX = "\nEmail: ";

    private User() {
        name = "";
        email = "";
        phone = "";
    }

    public static User getInstance() {
        if (instance == null) {
            synchronized (User.class) {
                if (instance == null) {
                    instance = new User();
                }
            }
        }

        return instance;
    }

    public void setInstance(User instance) {
        this.instance = instance;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isValidPhone(String phone) {
        //retira todos os caracteres menos os numeros
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");

        //verifica se tem a qtde de numero correto
        if (phone.length() != 11) return false;

        //Se tiver 11 caracteres, verificar se começa com 9 o celular
        if (Integer.parseInt(phone.substring(2, 3)) != 9) return false;

        //DDDs validos
        int[] codigosDDD = {11, 12, 13, 14, 15, 16, 17, 18, 19,
                21, 22, 24, 27, 28, 31, 32, 33, 34,
                35, 37, 38, 41, 42, 43, 44, 45, 46,
                47, 48, 49, 51, 53, 54, 55, 61, 62,
                64, 63, 65, 66, 67, 68, 69, 71, 73,
                74, 75, 77, 79, 81, 82, 83, 84, 85,
                86, 87, 88, 89, 91, 92, 93, 94, 95,
                96, 97, 98, 99};

        ArrayList DDDs_validos = new ArrayList();
        for (int i : codigosDDD)
            DDDs_validos.add(i);

        //verifica se o DDD é valido (sim, da pra verificar rsrsrs)
        if (!DDDs_validos.contains(Integer.parseInt(phone.substring(0, 2)))) return false;

        //se passar por todas as validações acima, então está tudo certo
        return true;
    }

    @Override
    public String toString() {
        return "\nNome: " + name +
                "\nTelefone: " + phone +
                "\nEmail: " + email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email))
            this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (isValidPhone(phone))
            this.phone = phone;
    }

    public String printBrief() {
        return USER_NAME_PREFIX + name
                + USER_PHONE_PREFIX + phone
                + USER_EMAIL_PREFIX + email;
    }
}
