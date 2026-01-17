package br.org.apae.api.suite.patient;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("br.org.apae.api.controller.disorder")
@IncludeTags("patient")

public class PatientUnitTestSuite {
}
