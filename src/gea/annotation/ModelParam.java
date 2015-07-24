package gea.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModelParam {
	String tableName();
	boolean Find() default true;
	boolean FindOne() default true;
	boolean Update() default true;
	boolean Delete() default true;
	boolean ToJSON() default true;
	boolean Insert() default true;
	boolean NextVal() default true;
}
