import { relative } from "path";

export default {
  "apps/(apae|management-app)/**/*.{ts,tsx,js,jsx}": ["prettier --write", "eslint --fix"],
  "apps/(apae|management-app)/**/*.{json,css}": ["prettier --write"],
  "apps/api/**/*.java": (files) => {
    const includes = files.map((f) => relative("apps/api", f)).join(",");
    return [
      `apps/api/mvnw -q -f apps/api/pom.xml checkstyle:check -Dcheckstyle.includes=${includes}`,
      `apps/api/mvnw -q -f apps/api/pom.xml pmd:check -Dincludes=${includes}`,
    ];
  },
  "*.{md,yml,yaml}": ["prettier --write"],
};
