import { dirname } from "path";
import { fileURLToPath } from "url";
import { createRequire } from "module";
import { FlatCompat } from "@eslint/eslintrc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);
const require = createRequire(import.meta.url);

const compat = new FlatCompat({
  baseDirectory: __dirname,
  resolvePluginsRelativeTo: dirname(require.resolve("eslint-config-next/package.json")),
});

const eslintConfig = [
  {
    ignores: [".next/**", "node_modules/**"],
  },
  ...compat.extends("next/core-web-vitals", "next/typescript"),
];

export default eslintConfig;
