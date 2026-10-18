import * as React from "react";

interface UseA11yAnnouncerReturn {
  politeMessage: string;
  assertiveMessage: string;
  announce: (message: string, priority?: "polite" | "assertive") => void;
}

export function useA11yAnnouncer(): UseA11yAnnouncerReturn {
  const [politeMessage, setPoliteMessage] = React.useState("");
  const [assertiveMessage, setAssertiveMessage] = React.useState("");

  const announce = React.useCallback(
    (message: string, priority: "polite" | "assertive" = "polite") => {
      if (priority === "assertive") {
        setAssertiveMessage(message);
        setTimeout(() => setAssertiveMessage(""), 100);
      } else {
        setPoliteMessage(message);
        setTimeout(() => setPoliteMessage(""), 100);
      }
    },
    [],
  );

  return { politeMessage, assertiveMessage, announce };
}
