
package model;

import java.util.*;

public class Model {

    // Representa um Livro com ID, título, autor e código
    public static class Livro {
        private String id;
        private String titulo;
        private String autor;
        private String code;

        public Livro(String id, String titulo, String autor, String code) {
            this.id = id;
            this.titulo = titulo;
            this.autor = autor;
            this.code = code;
        }

        public String getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getAutor() { return autor; }
        public String getCode() { return code; }

        @Override
        public String toString() {
            return "id: " + id + "\n" +
                    "titulo: " + titulo + "\n" +
                    "autor: " + autor + "\n" +
                    "code: " + code;
        }
    }

    // Representa um grafo com lista de adjacência e busca de caminhos
    public static class Grafo {
        private Map<String, List<String>> adjacencias = new HashMap<>();

        public void adicionarAresta(String origem, String destino) {
            adjacencias.putIfAbsent(origem, new ArrayList<>());
            adjacencias.get(origem).add(destino);
        }

        public List<String> buscarCaminho(String origem, String destino) {
            Queue<List<String>> fila = new LinkedList<>();
            Set<String> visitados = new HashSet<>();
            fila.add(List.of(origem));

            while (!fila.isEmpty()) {
                List<String> caminho = fila.poll();
                String atual = caminho.get(caminho.size() - 1);
                if (atual.equals(destino)) return caminho;
                if (visitados.contains(atual)) continue;

                visitados.add(atual);
                for (String vizinho : adjacencias.getOrDefault(atual, new ArrayList<>())) {
                    List<String> novo = new ArrayList<>(caminho);
                    novo.add(vizinho);
                    fila.add(novo);
                }
            }

            return null;
        }
    }
}

