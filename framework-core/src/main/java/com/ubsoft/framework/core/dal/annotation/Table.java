package com.ubsoft.framework.core.dal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	String name() default "";

	String primarykey() default "id";
	
	String pkType() default "String";

	
	String versionkey() default "version";
	
	String vkType() default "Integer";

	
	String catalog() default "";

	
	String schema() default "";

}
