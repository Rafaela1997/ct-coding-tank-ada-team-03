package utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {

    // Remove acentos, pontuação e converte para minúsculas
    public static String normalizar(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", ""); // remove acentos
        normalized = normalized.replaceAll("[^a-zA-Z0-9\\s]", ""); // remove pontuação
        return normalized.toLowerCase();
    }

    // Verifica se a string contém somente letras (usado por Trie se quiser)
    public static boolean apenasLetras(String s) {
        return Pattern.matches("^[a-zA-Z]+$", s);
    }
}
