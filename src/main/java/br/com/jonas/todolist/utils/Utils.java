
package br.com.jonas.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/*
* Aqui nessa classe iremos validar os atributos vazios que forem passados
* durante o update de algum objeto.
* Essa classe vai servir pra qualquer objeto que precisar de um update.
*/

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) {
        // Vai pegar tudo que tiver vindo nulo e fazer a mescla das informações
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        // Método criado pra acessar todas propriedades de um objeto
        final BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        // Vai ser criado um vetor de atributos vazios
        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                // Pra cada atributo, se tiver vazio, vai ser adicionado no vetor
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
