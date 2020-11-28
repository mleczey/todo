package com.mleczey.todo.arch;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.base.Predicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaEnumConstant;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.data.jpa.repository.JpaRepository;

@AnalyzeClasses(packages = "com.mleczey.todo", importOptions = {ImportOption.DoNotIncludeJars.class, ImportOption.DoNotIncludeTests.class})
class RepositoryRulesTest {

  @ArchTest
  static final ArchRule repositoriesShouldResideInBoundary = classes()
      .that().implement(JpaRepository.class)
      .should().resideInAnyPackage("..boundary..");

  @ArchTest
  static final ArchRule entitiesShouldResideInEntity = classes()
      .that().areAnnotatedWith(Entity.class)
      .should().resideInAPackage("..entity..");

  @ArchTest
  static final ArchRule entitiesShouldHaveId = fields()
      .that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
      .and().haveName("id")
      .should().beAnnotatedWith(Id.class);

//  @ArchTest
//  static final ArchRule entitiesWithLombokEqualsAndHashCode
//      = fields().that().areDeclaredInClassesThat().areAnnotatedWith(DescribedPredicate.<JavaAnnotation<?>>describe("", input -> {
//        return false;
//      }))
//          .and().areDeclaredInClassesThat().areAnnotatedWith(EqualsAndHashCode.class)
//          .and().haveName("uuid")
//          .should().beAnnotatedWith(EqualsAndHashCode.Include.class);
//
//  @ArchTest
//  static final ArchRule entitiesWithLombokEqualsAndHashCode2
//      = fields().that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
//          .and().areDeclaredInClassesThat().areAnnotatedWith(EqualsAndHashCode.class)
//          .and().doNotHaveName("uuid")
//          .should().notBeAnnotatedWith(EqualsAndHashCode.Include.class);
  @ArchTest
  static final ArchRule fieldsWithOneToOneShouldBeLazyLoaded
      = fields().that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
          .and().areAnnotatedWith(OneToOne.class)
          .should().beAnnotatedWith(fetchLazyAndCascadeMergeOrPersistOn(OneToOne.class));

  @ArchTest
  static final ArchRule fieldsWithOneToManyShouldBeLazyLoaded
      = fields().that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
          .and().areAnnotatedWith(OneToMany.class)
          .should().beAnnotatedWith(fetchLazyAndCascadeMergeOrPersistOn(OneToMany.class));

  @ArchTest
  static final ArchRule fieldsWithManyToOneShouldBeLazyLoaded
      = fields().that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
          .and().areAnnotatedWith(ManyToOne.class)
          .should().beAnnotatedWith(fetchLazyAndCascadeMergeOrPersistOn(ManyToOne.class));

  @ArchTest
  static final ArchRule fieldsWithManyToManyShouldBeLazyLoaded
      = fields().that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
          .and().areAnnotatedWith(ManyToMany.class)
          .should().beAnnotatedWith(fetchLazyAndCascadeMergeOrPersistOn(ManyToMany.class));

  private static DescribedPredicate<JavaAnnotation<?>> fetchLazyAndCascadeMergeOrPersistOn(final Class<?> annotation) {
    return DescribedPredicate.<JavaAnnotation<?>>describe(
        "@javax.persistence.ManyToMany and values: fetch=javax.persistence.FetchType.Lazy (mandatory), cascade=[javax.persistence.CascadeType.MEREGE, javax.persistence.CascadeType.PERSIST] (optional)",
        new AssociationAnnotationPredicate(annotation));
  }

  private static class AssociationAnnotationPredicate implements Predicate<JavaAnnotation<?>> {

    private final Class<?> annotation;

    AssociationAnnotationPredicate(final Class<?> annotation) {
      this.annotation = annotation;
    }

    @Override
    public boolean apply(JavaAnnotation<?> input) {
      if (input.getRawType().getFullName().equals(annotation.getName())) {
        final var properties = input.getProperties();
        return hasToContainOnly(properties, "fetch", Set.of(FetchType.LAZY.name()))
            && containsOnly(properties, "cascade", Set.of(CascadeType.MERGE.name(), CascadeType.PERSIST.name()));
      } else {
        return false;
      }
    }

    public boolean hasToContainOnly(final Map<String, Object> properties, final String propertyName, final Set<String> propertyValues) {
      boolean result = false;
      if (properties.containsKey(propertyName)) {
        Set<String> values = retrieve(properties, propertyName);
        values.removeAll(propertyValues);
        result = values.isEmpty();
      }
      return result;
    }

    private Set<String> retrieve(final Map<String, Object> properties, final String key) {
      final var result = new HashSet<String>();

      if (properties.containsKey(key)) {
        final var value = properties.get(key);
        if (JavaEnumConstant.class.isAssignableFrom(value.getClass())) {
          result.add(JavaEnumConstant.class.cast(value).name());
        } else if (value.getClass().isArray()) {
          result.addAll(Arrays.stream((JavaEnumConstant[]) value)
              .map(JavaEnumConstant::name)
              .collect(toSet()));
        }
      }

      return result;
    }

    public boolean containsOnly(final Map<String, Object> properties, final String propertyName, final Set<String> propertyValues) {
      boolean result = true;
      if (properties.containsKey(propertyName)) {
        Set<String> values = retrieve(properties, propertyName);
        values.removeAll(propertyValues);
        result = values.isEmpty();
      }
      return result;
    }
  }
}
