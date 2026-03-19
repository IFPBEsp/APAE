This is a [Next.js](https://nextjs.org) project bootstrapped with [`create-next-app`](https://nextjs.org/docs/app/api-reference/cli/create-next-app).

## Navegando até a pasta

O repositório `APAE` contém vários projetos (é um "monorepo"). O frontend Next.js está na pasta `apps/apae`.

```bash
# Pelo terminal: entre na pasta que você acabou de clonar
cd APAE

# Agora, entre na pasta do Front
cd apps/apae
```

## Rodando o projeto

Execute o servidor de desenvolvimento após navegar até a pasta do frontend:

```bash
npm run dev
# ou
yarn dev
# ou
pnpm dev
# ou
bun dev
##OBS: Através desse comando é possível rodar o frontend pelo terminal da pasta raiz /APAE :
pnpm dev:apae
```

Você pode começar a editar a página modificando o arquivo `app/page.tsx`. A página é atualizada automaticamente conforme você edita o arquivo.

Este projeto utiliza [`next/font`](https://nextjs.org/docs/app/building-your-application/optimizing/fonts) para otimizar e carregar automaticamente a fonte [Geist](https://vercel.com/font), uma nova família de fontes para Vercel.

## Saiba Mais

Para saber mais sobre o Next.js, consulte os seguintes recursos:

- [Documentação do Next.js](https://nextjs.org/docs) - saiba mais sobre os recursos e a API do Next.js.

- [Aprenda Next.js](https://nextjs.org/learn) - um tutorial interativo do Next.js.

Você pode conferir [o repositório do Next.js no GitHub](https://github.com/vercel/next.js) - seus comentários e contribuições são bem-vindos!

## Implantação no Vercel

A maneira mais fácil de implantar seu aplicativo Next.js é usar a [Plataforma Vercel](https://vercel.com/new?utm_medium=default-template&filter=next.js&utm_source=create-next-app&utm_campaign=create-next-app-readme) dos criadores do Next.js.

Confira nossa [documentação de implantação do Next.js](https://nextjs.org/docs/app/building-your-application/deploying) para mais detalhes.
