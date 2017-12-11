package com.ubsoft.framework.core.dal.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String name() default "";
	
	String dataType() default "";
	
	boolean unique() default false;

	boolean nullable() default true;
	
	int length() default 255;

	int precision() default 0;

	int scale() default 0;
}
