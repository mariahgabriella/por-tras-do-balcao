# AGENTS.md (camada de dados)

Regras específicas para a pasta `data/`. Complementam o AGENTS.md da raiz.

- Toda `@Entity` fica em `entity/`, todo `@Dao` em `dao/`, e o banco em `database/`.
- Ao criar ou alterar uma entidade: registrar em `AppDatabase`, incrementar `version` e compilar para validar o Room.
- Chaves estrangeiras: sempre `@ForeignKey` com `onDelete` definido e `@Index` na coluna filha.
- Relacionamento muitos-para-muitos exige tabela intermediária (ex.: ItemPedido).
- DAOs: consultas de leitura retornam `Flow`; escritas são `suspend`.
- Não colocar regra de negócio no DAO; ela pertence ao Repository ou ao ViewModel.
- Conferir o nome real da chave primária de `Produto` antes de referenciá-la em `parentColumns`.
