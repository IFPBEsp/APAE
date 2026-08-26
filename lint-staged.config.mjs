import { relative } from "path";

const FRONTEND_APPS = ["apps/apae", "apps/management-app"];

function scoped(files, command) {
  return FRONTEND_APPS.flatMap((app) => {
    const relativeFiles = files
      .filter((f) => f.includes(`/${app}/`) || f.startsWith(`${app}/`))
      .map((f) => relative(app, f));
    if (relativeFiles.length === 0) return [];
    return [`pnpm --filter ./${app} exec ${command} ${relativeFiles.join(" ")}`];
  });
}

export default {
  "apps/(apae|management-app)/**/*.{ts,tsx,js,jsx}": (files) => [
    ...scoped(files, "prettier --write"),
    ...scoped(files, "eslint --fix"),
  ],
  "apps/(apae|management-app)/**/*.{json,css}": (files) => scoped(files, "prettier --write"),
  "apps/api/**/*.java": (files) => {
    const includes = files.map((f) => relative("apps/api", f)).join(",");
    return [
      `apps/api/mvnw -q -f apps/api/pom.xml checkstyle:check -Dcheckstyle.includes=${includes}`,
      `apps/api/mvnw -q -f apps/api/pom.xml pmd:check -Dincludes=${includes}`,
    ];
  },
  "*.{md,yml,yaml}": ["prettier --write"],
};
