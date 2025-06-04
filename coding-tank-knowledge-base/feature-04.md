### Desafio - Knowledge Base CLI

Sua equipe foi incumbida de criar uma aplicação para gerenciar uma base de conhecimento (Knowledge Base).
Esta base de conhecimento é composta por múltiplos arquivos dispostos numa pasta.

### [FEATURE-004] [25pt] CLI para manuseio destas funções

A aplicação recebe comandos pela entrada padrão (stdin). Cada comando deve ser escrito em uma única linha.

Os arquivos utilizados são fixos e esperados no diretório do programa:

- fictional_books.txt: base de dados dos livros (linhas de 200 caracteres fixos, ordenadas por ID).
- map.bin: grafo representando caminhos físicos paginados.
- books/: pasta contendo arquivos .txt com conteúdos dos livros (linhas de até 200 caracteres).

Os comandos serão:

1. `busca-livro 123`
2. `busca-livro 123-456`


#### Descrição detalhada:

1. `busca-livro 123`
   Busca binária exata pelo ID ID-000123.

Entrada:
```
busca-livro 123
```

Saída:
```
id: 000123
----
titulo: <Título>
autor: <Autor>
code: <Texto aleatório>
```

2. `busca-livro 123-456`
   Busca por intervalo de IDs de ID-000123 até ID-000456, inclusive.

Entrada:
```
busca-livro 123-456
```

Saída:
```
ids: 000123-000456
----
id: 000123
titulo: <Título>
autor: <Autor>
code: <Texto aleatório>
----
id: 000456
titulo: <Título>
autor: <Autor>
code: <Texto aleatório>
```

Não encontrado:

```
id: 000123
----
nao encontrado
```

Você deve ignorar os zeros a esquerda.

3. `busca-texto "frase com . como coringa"`
   Busca por uma frase nos arquivos .txt da pasta livros/. O caractere . é curinga e representa qualquer letra.
   A frase pode estar em uma ou duas linhas seguidas. A busca é normalizada (sem acento, pontuação, tudo minúsculo)
   e usa uma Trie para eficiência.

Entrada:
busca-texto "flores no nosso jardim"

Saída (caso encontrado):
```
frase: "flores no nosso jardim"
----
arquivo: nome_do_arquivo.txt
linha: 123-124
```

Saída (caso não encontrado):
```
frase: "flores no nosso jardim"
```
----
nao encontrado

4. `busca-caminho "A" "B"`
   Busca caminho físico entre o ponto A e o ponto B usando o arquivo map.bin.

Entrada:
```
busca-caminho "A" "B"
```

Saída:
```
de: A
para: B
----
caminho:
1. A
2. P1
3. P2
4. B
```

Todos os comandos devem seguir este formato exatamente. A CLI não deve imprimir mensagens extras.