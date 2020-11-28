package com.mleczey.todo.arch;

import com.tngtech.archunit.base.DescribedPredicate;
import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaModifier.ABSTRACT;
import com.tngtech.archunit.core.domain.properties.HasModifiers;
import static com.tngtech.archunit.core.domain.properties.HasModifiers.Predicates.modifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import org.junit.jupiter.api.Test;

@AnalyzeClasses(packages = "com.mleczey.todo", importOptions = IncludeOnlyTestsOption.class)
class TestRulesTest {

  @ArchTest
  static final ArchRule testClassesShouldHavePackageScope = classes()
          .that().haveSimpleNameEndingWith("Test")
          .or().haveSimpleNameEndingWith("IntegrationTest")
          .and(areNotAbstract())
          .should().bePackagePrivate();

  @ArchTest
  static final ArchRule testMethodsShouldHavePackageScope = methods()
          .that().areAnnotatedWith(Test.class)
          .should().bePackagePrivate();

  @ArchTest
  static final ArchRule fieldsInTestsShouldHavePackageOrPrivateScope = fields().
          that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Test")
          .should().bePackagePrivate()
          .orShould().bePrivate();

  @ArchTest
  static final ArchRule fieldsInIntegrationTestsShouldHavePackageOrPrivateScope = fields()
          .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("IntegrationTest")
          .should().bePackagePrivate()
          .orShould().bePrivate();

  static DescribedPredicate<HasModifiers> areNotAbstract() {
    return not(modifier(ABSTRACT)).as("are abstract");
  }
}
