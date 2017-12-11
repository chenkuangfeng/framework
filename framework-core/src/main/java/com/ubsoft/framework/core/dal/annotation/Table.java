package com.ubsoft.framework.core.dal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	String name() default "";

	String primarykey() default "ID";
	
	String versionkey() default "Version";
	
	String catalog() default "";

	
	String schema() default "";

}
