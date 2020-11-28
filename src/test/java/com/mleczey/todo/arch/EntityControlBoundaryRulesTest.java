package com.mleczey.todo.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.mleczey.todo", importOptions = {ImportOption.DoNotIncludeJars.class, ImportOption.DoNotIncludeTests.class})
class EntityControlBoundaryRulesTest {

  private static final String BOUNDARY = "Boundary";

  private static final String CONTROL = "Control";

  private static final String ENTITY = "Entity";

  @ArchTest
  static final ArchRule generalRule
      = layeredArchitecture()
          .layer(ENTITY).definedBy("..entity..")
          .layer(CONTROL).definedBy("..control..")
          .layer(BOUNDARY).definedBy("..boundary..")
          .whereLayer(ENTITY).mayOnlyBeAccessedByLayers(CONTROL, BOUNDARY)
          .whereLayer(CONTROL).mayOnlyBeAccessedByLayers(BOUNDARY)
          .whereLayer(BOUNDARY).mayNotBeAccessedByAnyLayer();

  @ArchTest
  static final ArchRule controlBoundaryRule
      = noClasses().that().resideInAPackage("..control..")
          .should().dependOnClassesThat().resideInAPackage("..boundary..");

  @ArchTest
  static final ArchRule entityBoundaryRule
      = noClasses().that().resideInAPackage("..entity..")
          .should().dependOnClassesThat().resideInAPackage("..boundary..");

  static final ArchRule entityControlRule
      = noClasses().that().resideInAPackage("..entity..")
          .should().dependOnClassesThat().resideInAPackage("..control..");
}
