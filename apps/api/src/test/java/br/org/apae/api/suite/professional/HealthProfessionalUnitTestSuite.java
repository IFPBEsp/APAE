package br.org.apae.api.suite.professional;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("br.org.apae.api.unit.professional")
@IncludeTags("health-professional")
public class HealthProfessionalUnitTestSuite {
}
