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
    // maven-checkstyle-plugin's `checkstyle.includes` is resolved relative to
    // ${project.build.sourceDirectory} (src/main/java), not to the module root —
    // passing a module-relative path silently matches zero files (no violations
    // "found", check passes vacuously). maven-pmd-plugin's `includes`, on the
    // other hand, accepts a module-relative path just fine.
    const checkstyleIncludes = files.map((f) => relative("apps/api/src/main/java", f)).join(",");
    const pmdIncludes = files.map((f) => relative("apps/api", f)).join(",");
    return [
      `apps/api/mvnw -q -f apps/api/pom.xml checkstyle:check -Dcheckstyle.includes=${checkstyleIncludes}`,
      `apps/api/mvnw -q -f apps/api/pom.xml pmd:check -Dincludes=${pmdIncludes}`,
    ];
  },
  "*.{md,yml,yaml}": ["prettier --write"],
};
