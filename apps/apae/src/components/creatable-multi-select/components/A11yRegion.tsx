import * as React from "react";

interface A11yRegionProps {
  politeMessage: string;
  assertiveMessage: string;
}

export function A11yRegion({ politeMessage, assertiveMessage }: A11yRegionProps) {
  return (
    <div className="sr-only">
      <div aria-live="polite" aria-atomic="true" role="status">
        {politeMessage}
      </div>
      <div aria-live="assertive" aria-atomic="true" role="alert">
        {assertiveMessage}
      </div>
    </div>
  );
}
