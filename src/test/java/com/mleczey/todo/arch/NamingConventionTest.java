package com.mleczey.todo.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.mleczey.todo", importOptions = {ImportOption.DoNotIncludeJars.class, ImportOption.DoNotIncludeTests.class})
class NamingConventionTest {

  @ArchTest
  static final ArchRule interfacesNamingConvention = noClasses()
          .that().areInterfaces()
          .should().haveSimpleNameContaining("Interface");

  @ArchTest
  static final ArchRule interfaceImplementationNamingConvention = noClasses()
          .should().haveSimpleNameContaining("Impl");

  @ArchTest
  static final ArchRule resourcesShouldBeProperlyNamed = classes()
          .that().areAnnotatedWith(RestController.class)
          .should().haveSimpleNameEndingWith("Resource");

  @ArchTest
  static final ArchRule repositoriesShouldBeProperlyNamed = classes()
          .that().implement(JpaRepository.class)
          .should().haveSimpleNameEndingWith("Repository");

  @ArchTest
  static final ArchRule configurationsShouldBeProperlyNamed = classes()
          .that().implement(Configuration.class)
          .should().haveSimpleNameEndingWith("Configuraton");
}
