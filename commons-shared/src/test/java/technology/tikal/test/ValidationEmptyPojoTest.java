package technology.tikal.test;

import technology.tikal.hibernate.validation.NotEmptyPojoValidator;

public class ValidationEmptyPojoTest {

    public static void main(String[] args) {
        NotEmptyPojoValidator obj = new NotEmptyPojoValidator();
        PojoTest pojo = new PojoTest();
        System.out.println(obj.isValid(pojo, null));
        pojo.setUno("");
        System.out.println(obj.isValid(pojo, null));
    }

}
