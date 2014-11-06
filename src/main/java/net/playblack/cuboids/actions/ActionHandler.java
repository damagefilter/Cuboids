package net.playblack.cuboids.actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate Cuboid actions as such, to make sure they are called when required.
 *
 * @author chris
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionHandler {
    Priority priority() default Priority.MEDIUM;

    public enum Priority {LOW, MEDIUM, HIGH}
}
