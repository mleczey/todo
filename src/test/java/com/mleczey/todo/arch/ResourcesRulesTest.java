package com.mleczey.todo.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.mleczey.todo", importOptions = {ImportOption.DoNotIncludeJars.class, ImportOption.DoNotIncludeTests.class})
class ResourcesRulesTest {

  @ArchTest
  static final ArchRule resourcesShouldBeProperlyStructured = classes()
      .that().areAnnotatedWith(RestController.class)
      .should().bePublic()
      .andShould().haveOnlyFinalFields()
      .andShould().resideInAPackage("..boundary..");

  @ArchTest
  static final ArchRule resourcesShouldHaveMethodsAnnotatedWithRestOperation = methods()
      .that().arePublic()
      .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
      .should().beAnnotatedWith(GetMapping.class)
      .orShould().beAnnotatedWith(PostMapping.class)
      .orShould().beAnnotatedWith(PutMapping.class)
      .orShould().beAnnotatedWith(DeleteMapping.class);

  @ArchTest
  static final ArchRule resourceMethodsShouldAlwaysReturnResponseEntity = methods()
      .that().areNotPrivate()
      .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
      .should().haveRawReturnType(ResponseEntity.class);
//
//   why not working?
//  @ArchTest
//  static final ArchRule resourcesShouldHaveOpenApiDocumentation
//      = methods().that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
//          .should().beAnnotatedWith(ApiResponse.class)
//          .andShould().beAnnotatedWith(Operation.class);

  // check if produces is filled in annotation
  @ArchTest
  static final ArchRule getOperations = methods()
      .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
      .and().areAnnotatedWith(GetMapping.class)
      .should().haveNameMatching("get\\w+");
}
