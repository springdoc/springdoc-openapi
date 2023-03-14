package test.org.springdoc.api.app169;

/**
 * simple outer class
 *
 * @param name the name of the outer class
 * @param simpleInnerClass the inner class
 */
public record SimpleOuterClass(String name, SimpleInnerClass simpleInnerClass) {
}
