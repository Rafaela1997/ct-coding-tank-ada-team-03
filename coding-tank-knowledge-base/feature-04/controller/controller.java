package controller;

import java.io.*;
import java.util.*;

public class Controller {

    public void executar(String comando) {
        if (comando.startsWith("busca-livro")) {
            executarBuscaLivro(comando);
        } else if (comando.startsWith("busca-texto")) {
            executarBuscaTexto(comando);
        } else if (comando.startsWith("busca-caminho")) {
            executarBuscaCaminho(comando);
        }
    }

    private void executarBuscaLivro(String comando) {
        String[] partes = comando.split(" ");
        String argumento = partes[1];
        try (RandomAccessFile file = new RandomAccessFile(new File("fictional_books.txt"), "r")) {
            int tamanhoLinha = 200;
            long totalLinhas = file.length() / tamanhoLinha;

            if (argumento.contains("-")) {
                int inicio = Integer.parseInt(argumento.split("-")[0]);
                int fim = Integer.parseInt(argumento.split("-")[1]);
                System.out.println("ids: " + String.format("%06d", inicio) + "-" + String.format("%06d", fim));
                System.out.println("----");
                for (int i = inicio; i <= fim; i++) {
                    buscarLivro(file, i, totalLinhas, tamanhoLinha);
                    if (i != fim) System.out.println("----");
                }
            } else {
                int id = Integer.parseInt(argumento);
                System.out.println("id: " + String.format("%06d", id));
                System.out.println("----");
                buscarLivro(file, id, totalLinhas, tamanhoLinha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo");
        }
    }

    private void buscarLivro(RandomAccessFile file, int id, long total, int tamanhoLinha) throws IOException {
        int low = 0, high = (int) total - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            file.seek(mid * tamanhoLinha);
            byte[] linhaBytes = new byte[tamanhoLinha];
            file.readFully(linhaBytes);
            String linha = new String(linhaBytes);
            int idLinha = Integer.parseInt(linha.substring(3, 9));
            if (idLinha == id) {
                System.out.println("titulo: " + linha.substring(10, 60).trim());
                System.out.println("autor: " + linha.substring(60, 110).trim());
                System.out.println("code: " + linha.substring(110).trim());
                return;
            } else if (idLinha < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        System.out.println("nao encontrado");
    }

    private String normalizar(String input) {
        return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .toLowerCase();
    }

    private static class Trie {
        static class Node {
            Map<Character, Node> filhos = new HashMap<>();
            boolean terminal = false;
        }

        private final Node raiz = new Node();

        public void inserir(String palavra) {
            Node atual = raiz;
            for (char c : palavra.toCharArray()) {
                atual = atual.filhos.computeIfAbsent(c, k -> new Node());
            }
            atual.terminal = true;
        }

        public boolean buscar(String padrao) {
            return buscarRecursivo(padrao.toCharArray(), 0, raiz);
        }

        private boolean buscarRecursivo(char[] padrao, int i, Node no) {
            if (i == padrao.length) return no.terminal;

            char c = padrao[i];
            if (c == '.') {
                for (Node filho : no.filhos.values()) {
                    if (buscarRecursivo(padrao, i + 1, filho)) return true;
                }
                return false;
            } else {
                Node proximo = no.filhos.get(c);
                return proximo != null && buscarRecursivo(padrao, i + 1, proximo);
            }
        }
    }

    private void executarBuscaTexto(String comando) {
        String frase = comando.substring(comando.indexOf('\"') + 1, comando.lastIndexOf('\"'));
        System.out.println("frase: \"" + frase + "\"");
        System.out.println("----");

        File pasta = new File("books");
        if (!pasta.exists() || !pasta.isDirectory()) {
            System.out.println("nao encontrado");
            return;
        }

        String normalizada = normalizar(frase);
        String[] palavras = normalizada.split("\\s+");
        String padraoBusca = String.join(" ", palavras);

        for (File arquivo : Objects.requireNonNull(pasta.listFiles((dir, name) -> name.endsWith(".txt")))) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                List<String> linhas = new ArrayList<>();
                String linha;
                while ((linha = reader.readLine()) != null) {
                    linhas.add(normalizar(linha));
                }

                for (int i = 0; i < linhas.size(); i++) {
                    String atual = linhas.get(i);
                    String duasLinhas = atual;
                    if (i + 1 < linhas.size()) {
                        duasLinhas += " " + linhas.get(i + 1);
                    }

                    Trie trie = new Trie();
                    for (int j = 0; j <= duasLinhas.length() - padraoBusca.length(); j++) {
                        trie.inserir(duasLinhas.substring(j, j + padraoBusca.length()));
                    }

                    if (trie.buscar(padraoBusca)) {
                        System.out.println("arquivo: " + arquivo.getName());
                        System.out.println("linha: " + (i + 1) + (duasLinhas.length() > atual.length() ? "-" + (i + 2) : ""));
                        return;
                    }
                }
            } catch (IOException e) {
                // Ignora erro individual
            }
        }

        System.out.println("nao encontrado");
    }

    private void executarBuscaCaminho(String comando) {
        String[] partes = comando.split("\"");
        if (partes.length < 5) {
            System.out.println("de: -\npara: -\n----\ncaminho:\nnao encontrado");
            return;
        }

        String origem = partes[1];
        String destino = partes[3];

        System.out.println("de: " + origem);
        System.out.println("para: " + destino);
        System.out.println("----");

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("map.bin"))) {
            model.Model.Grafo grafo = (model.Model.Grafo) ois.readObject();
            List<String> caminho = grafo.buscarCaminho(origem, destino);

            if (caminho != null) {
                System.out.println("caminho:");
                int i = 1;
                for (String p : caminho) {
                    System.out.println(i++ + ". " + p);
                }
            } else {
                System.out.println("caminho:\nnao encontrado");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("caminho:\nnao encontrado");
        }
    }
}
