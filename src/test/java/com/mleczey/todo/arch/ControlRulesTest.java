package com.mleczey.todo.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import org.springframework.transaction.annotation.Transactional;

@AnalyzeClasses(packages = "com.mleczey.todo", importOptions = {ImportOption.DoNotIncludeJars.class, ImportOption.DoNotIncludeTests.class})
class ControlRulesTest {

  @ArchTest
  static final ArchRule noCycles = slices()
      .matching("..control.(*).")
      .should().beFreeOfCycles();

  @ArchTest
  static final ArchRule transactionalMethodsShouldBePublic = methods()
      .that().areAnnotatedWith(Transactional.class)
      .and().areDeclaredInClassesThat().resideInAPackage("..control..")
      .should().bePublic();

  @ArchTest
  static final ArchRule classesInControlPackagesShouldHavePackageScope = classes()
      .that().resideInAPackage("..control..")
      .and().areNotInterfaces()
      .should().bePackagePrivate();
}
